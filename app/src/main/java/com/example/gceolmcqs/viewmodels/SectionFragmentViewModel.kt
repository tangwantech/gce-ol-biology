package com.example.gceolmcqs.viewmodels

import android.os.CountDownTimer
import android.text.format.Time
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.datamodels.*
import com.example.gceolmcqs.repository.PaperRepository

class SectionFragmentViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    
    companion object {
        private const val KEY_SECTION_INDEX = "sectionIndex"
    }

    private var sectionData: SectionData? = null
    private var userSelections = ArrayList<UserSelection>()
    private val letters: Array<String> = arrayOf("A", "B", "C", "D")
    private val indexPreviousAndCurrentSelectedOptionOfQuestion = IndexPreviousAndCurrentSelectedOptionOfQuestion()
    
    private val _numberOfQuestionsAnswered = MutableLiveData(0)
    val numberOfQuestionsAnswered: LiveData<Int> = _numberOfQuestionsAnswered
    
    private val isQuestionAnswered = MutableLiveData(false)
    private val sectionQuestionsScores = ArrayList<QuestionScore>()
    private var sectionScore: Int = 0
    private var sectionDuration: Long = 0L
    private var timer: CountDownTimer? = null
    private val questionIndex = MutableLiveData(0)
    private val userMarkedAnswerSheet = ArrayList<QuestionWithUserAnswerMarkedData>()

    private var sectionIndex: Int?
        get() = savedStateHandle[KEY_SECTION_INDEX]
        set(value) { savedStateHandle[KEY_SECTION_INDEX] = value }

    private val isTimeOut = MutableLiveData(false)
    private val timeRemaining = MutableLiveData<Time>().apply {
        value = Time().apply { set(0) }
    }
    private val isTimeAlmostOut = MutableLiveData(false)

    fun setSectionIndex(index: Int) {
        if (sectionIndex == index && sectionData != null) return
        
        sectionIndex = index
        initializeData()
    }

    /**
     * Attempts to initialize data from the repository. 
     * Can be called multiple times if the repository wasn't ready initially.
     */
    fun initializeData(): Boolean {
        val index = sectionIndex ?: return false
        val data = PaperRepository.getSectionDataAt(index) ?: return false
        
        sectionData = data
        setSectionDuration()
        shuffleSectionQuestions()
        initUserMarkedAnswerSheet()
        updateUserMarkedAnswerSheet()
        return true
    }

    fun getLetters(): Array<String> = letters

    private fun shuffleSectionQuestions() {
        sectionData?.questions?.shuffle()
    }

    private fun setSectionDuration() {
        sectionDuration = getNumberOfQuestionsInSection() * MCQConstants.MILLI_SEC_PER_QUESTION
    }

    fun startTimer() {
        timer?.cancel()
        if (sectionDuration <= 0) return
        
        timer = object : CountDownTimer(sectionDuration, MCQConstants.COUNT_DOWN_INTERVAL) {
            override fun onTick(p0: Long) {
                val t = Time()
                updateIsTimeAlmostOut(p0)
                t.set(p0)
                timeRemaining.value = t
            }

            override fun onFinish() {
                isTimeOut.value = true
            }
        }.start()
    }

    fun getTimeRemaining(): LiveData<Time> = timeRemaining

    fun updateIsTimeAlmostOut(timeLeft: Long) {
        if (timeLeft < MCQConstants.TIME_TO_ANIMATE_TIMER) {
            isTimeAlmostOut.value = true
        }
    }

    fun getIsTimeAlmostOut(): LiveData<Boolean> = isTimeAlmostOut

    fun getIsTimeOut(): LiveData<Boolean> = isTimeOut

    fun getNumberOfQuestionsInSection(): Int {
        return sectionData?.numberOfQuestions ?: 0
    }

    private fun updateNumberOfQuestionsAnswered() {
        _numberOfQuestionsAnswered.value = (_numberOfQuestionsAnswered.value ?: 0) + 1
    }

    private fun initUserMarkedAnswerSheet() {
        val numQuestions = getNumberOfQuestionsInSection()
        initUserSelections()
        initSectionQuestionsScores()
        userMarkedAnswerSheet.clear()
        for (index in 0 until numQuestions) {
            userMarkedAnswerSheet.add(QuestionWithUserAnswerMarkedData((index + 1).toString()))
        }
    }

    private fun initUserSelections() {
        userSelections.clear()
        for (index in 0 until getNumberOfQuestionsInSection()) {
            userSelections.add(UserSelection())
        }
    }

    private fun initSectionQuestionsScores() {
        sectionQuestionsScores.clear()
        for (index in 0 until getNumberOfQuestionsInSection()) {
            sectionQuestionsScores.add(QuestionScore())
        }
    }

    fun getSectionQuestions(): ArrayList<QuestionData> {
        val data = sectionData ?: return arrayListOf()
        if (data.sectionType == MCQConstants.FOUR_ALTS) {
            for (index in 0 until getNumberOfQuestionsInSection()) {
                data.questions.getOrNull(index)?.selectableOptions?.shuffle()
            }
        }
        return data.questions
    }

    private fun updateUserMarkedAnswerSheet() {
        sectionData?.questions?.forEachIndexed { index, questionDataModel ->
            if (index < userMarkedAnswerSheet.size) {
                userMarkedAnswerSheet[index].apply {
                    questionNumber = (index + 1).toString()
                    question = questionDataModel.question
                    image = questionDataModel.image
                    twoStatements = questionDataModel.twoStatements
                    nonSelectableOptions = questionDataModel.nonSelectableOptions
                    explanation = questionDataModel.explanation
                }
            }
        }
    }

    private fun setQuestionsCorrectAnswers() {
        sectionData?.questions?.forEachIndexed { index, questionDataModel ->
            if (index < userMarkedAnswerSheet.size) {
                questionDataModel.selectableOptions.forEachIndexed { optionIndex, s ->
                    if (s == questionDataModel.wordAnswer) {
                        userMarkedAnswerSheet[index].correctAnswer =
                            "${letters.getOrNull(optionIndex) ?: ""}. ${questionDataModel.wordAnswer}"
                    }
                }
            }
        }
    }

    fun getSectionTitle(): String {
        return sectionData?.title ?: ""
    }

    fun updateUserSelection(questionIndex: Int, optionSelectedIndex: Int) {
        val data = sectionData ?: return
        val questions = data.questions
        if (questionIndex >= questions.size || optionSelectedIndex >= letters.size) return
        
        val selectedOption = questions[questionIndex].selectableOptions.getOrNull(optionSelectedIndex) ?: return
        
        val userSelection = UserSelection(
            letters[optionSelectedIndex],
            selectedOption
        )
        
        if (questionIndex < userSelections.size) {
            userSelections[questionIndex] = userSelection
            appendLetterToFourOptions(questionIndex)
            if (questionIndex < userMarkedAnswerSheet.size) {
                userMarkedAnswerSheet[questionIndex].userSelection = userSelection
            }
            updateNumberOfQuestionsAnswered()
            evaluateUserSelections(questionIndex)
        }
    }

    private fun appendLetterToFourOptions(questionIndex: Int) {
        val data = sectionData ?: return
        val question = data.questions.getOrNull(questionIndex) ?: return
        var optionsWithLetterPrepended = ""
        question.selectableOptions.forEachIndexed { index, s ->
            optionsWithLetterPrepended += "${letters.getOrNull(index) ?: ""}. $s\n"
        }
        if (questionIndex < userMarkedAnswerSheet.size) {
            userMarkedAnswerSheet[questionIndex].fourOptions = optionsWithLetterPrepended
        }
    }

    private fun evaluateUserSelections(questionIndex: Int) {
        val data = sectionData ?: return
        if (questionIndex >= data.questions.size || questionIndex >= userSelections.size) return
        
        if (userSelections[questionIndex].optionSelected == data.questions[questionIndex].wordAnswer) {
            if (questionIndex < sectionQuestionsScores.size) {
                sectionQuestionsScores[questionIndex].score = 1
            }
            userMarkedAnswerSheet.getOrNull(questionIndex)?.userSelection?.remark = true
        } else {
            if (questionIndex < sectionQuestionsScores.size) {
                sectionQuestionsScores[questionIndex].score = 0
            }
            userMarkedAnswerSheet.getOrNull(questionIndex)?.userSelection?.remark = false
        }
        sumSectionQuestionScores()
    }

    private fun sumSectionQuestionScores() {
        sectionScore = sectionQuestionsScores.count { it.score == 1 }
    }

    fun getSectionResultData(): SectionResultData {
        setQuestionsCorrectAnswers()
        val numQuestions = getNumberOfQuestionsInSection()
        val divisor = if (numQuestions > 0) numQuestions.toDouble() else 1.0
        val percentage = ((sectionScore.toDouble() / divisor) * 100).toInt()
        val scoreData = ScoreData(sectionScore, numQuestions, percentage)
        val userMarkedAnswersSheetData = UserMarkedAnswersSheetData(userMarkedAnswerSheet)
        return SectionResultData(sectionIndex ?: 0, scoreData, userMarkedAnswersSheetData)
    }

    fun getSectionDirections(): String {
        return sectionData?.directions ?: ""
    }

    fun getSectionIndex(): String {
        return sectionIndex?.toString() ?: ""
    }
    
    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}
