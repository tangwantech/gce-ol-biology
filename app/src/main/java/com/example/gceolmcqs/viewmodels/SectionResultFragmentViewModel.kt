package com.example.gceolmcqs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.datamodels.*
import com.example.gceolmcqs.repository.PaperRepository

class SectionResultFragmentViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    
    companion object {
        private const val KEY_RESULT_DATA = "sectionResultData"
    }

    private var sectionResultData: SectionResultData?
        get() = savedStateHandle[KEY_RESULT_DATA]
        set(value) { savedStateHandle[KEY_RESULT_DATA] = value }

    private val hasPerfectScore = MutableLiveData<Boolean>()
    private val _nextButtonState = MutableLiveData<Boolean>(false)
    val nextButtonState: LiveData<Boolean> = _nextButtonState

    private var nextSectionIndex = 0

    fun updateNextBtnState() {
        val currentIndex = getSectionIndex()
        nextSectionIndex = currentIndex + 1

        val numSections = PaperRepository.getNumberOfSections()
        while (nextSectionIndex < numSections && PaperRepository.getSectionAnsweredAt(nextSectionIndex)) {
            nextSectionIndex += 1
        }

        if (nextSectionIndex < numSections) {
            _nextButtonState.value = true
        } else {
            _nextButtonState.value = false
        }
    }

    fun getNextSectionIndex(): Int = nextSectionIndex

    fun setResultData(data: SectionResultData) {
        this.sectionResultData = data
        checkForPerfectScore()
        
        val index = getSectionIndex()
        // Repository updates are only safe if the repository is initialized
        if (PaperRepository.isPaperDataInitialised()) {
            updateSectionAnsweredAt(index)
            updateSectionScoreAt(index, getNumberOfCorrectAnswers())
        }
    }

    fun getNumberOfCorrectAnswers(): Int {
        return sectionResultData?.scoreData?.numberOfCorrectAnswers ?: 0
    }

    fun getNumberOfQuestions(): Int {
        return sectionResultData?.scoreData?.numberOfQuestions ?: 0
    }

    fun getScorePercentage(): Int {
        return sectionResultData?.scoreData?.percentage ?: 0
    }

    private fun updateSectionScoreAt(sectionIndex: Int, sectionScore: Int) {
        PaperRepository.updateSectionScoreAt(sectionIndex, sectionScore)
    }

    private fun updateSectionAnsweredAt(sectionIndex: Int) {
        PaperRepository.updateSectionsAnsweredAt(sectionIndex)
    }

    private fun checkForPerfectScore() {
        hasPerfectScore.value = sectionResultData?.scoreData?.percentage == 100
    }

    fun getHasPerfectScore(): LiveData<Boolean> = hasPerfectScore

    fun getSectionIndex(): Int = sectionResultData?.sectionIndex ?: 0

    fun getUserMarkedAnswersSheet(): UserMarkedAnswersSheetData {
        return sectionResultData?.userMarkedAnswersSheet ?: UserMarkedAnswersSheetData(emptyList())
    }

    fun getQuestionsWithCorrectAnswer(): UserMarkedAnswersSheetData {
        val questionsWithCorrectAnswer = ArrayList<QuestionWithUserAnswerMarkedData>()
        sectionResultData?.userMarkedAnswersSheet?.questionsWithUserAnswerMarkedData?.forEach { question ->
            if (question.userSelection == null || question.userSelection?.remark == false) {
                questionsWithCorrectAnswer.add(question)
            }
        }
        return UserMarkedAnswersSheetData(questionsWithCorrectAnswer)
    }
}
