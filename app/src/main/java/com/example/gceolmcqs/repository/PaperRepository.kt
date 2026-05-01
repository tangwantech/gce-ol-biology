package com.example.gceolmcqs.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.datamodels.PaperData
import com.example.gceolmcqs.datamodels.SectionData
import com.example.gceolmcqs.datamodels.SectionResultData
import com.example.gceolmcqs.datamodels.UserMarkedAnswersSheetData

class PaperRepository {
    companion object {
        private var paperData: PaperData? = null
        private var currentSectionIndex = 0

        private val sectionsAnsweredData = mutableListOf<Boolean>()
        private val paperScore = MutableLiveData(0)
        private val sectionsScores = MutableLiveData(mutableListOf<Int>())
        private val sectionsAnsweredCount = MutableLiveData(0)
        private val currentSectionRetryCount = MutableLiveData(MCQConstants.SECTION_RETRY_LIMIT)
        private val unAnsweredSectionIndexes = mutableListOf<Int>()
        private var userMarkedAnswersSheetData: UserMarkedAnswersSheetData? = null
        private var sectionResultData: SectionResultData? = null
        private val paperGrade = MutableLiveData<String?>(null)
        private val paperPercentage = MutableLiveData(0)
        private val areAllSectionsAnswered = MutableLiveData(false)

        fun initPaperData(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int, paperData: PaperData?) {
            this.paperData = paperData
            paperData?.let {
                sectionsScores.value = MutableList(it.numberOfSections) { 0 }
                sectionsAnsweredData.clear()
                sectionsAnsweredData.addAll(List(it.numberOfSections) { false })
                
                unAnsweredSectionIndexes.clear()
                for (i in 0 until it.numberOfSections) {
                    unAnsweredSectionIndexes.add(i)
                }
            }
        }

        fun isPaperDataInitialised(): Boolean{
            return paperData != null
        }

        fun getUnAnsweredSectionIndexes(): List<Int> = unAnsweredSectionIndexes

        fun setCurrentSectionIndex(sectionIndex: Int) {
            currentSectionIndex = sectionIndex
        }

        fun getCurrentSectionIndex(): Int = currentSectionIndex

        fun resetPaperRepo() {
            currentSectionIndex = 0
            sectionsAnsweredData.fill(false)
            paperScore.value = 0
            sectionsScores.value?.fill(0)
            sectionsAnsweredCount.value = 0
            currentSectionRetryCount.value = MCQConstants.SECTION_RETRY_LIMIT
            unAnsweredSectionIndexes.clear()
            paperData?.let {
                for (i in 0 until it.numberOfSections) {
                    unAnsweredSectionIndexes.add(i)
                }
            }
            userMarkedAnswersSheetData = null
            sectionResultData = null
            paperGrade.value = null
            paperPercentage.value = 0
            areAllSectionsAnswered.value = false
        }

        fun getSectionDataAt(position: Int): SectionData? {
            return paperData?.sections?.getOrNull(position)?.copy()?.apply {
                repeat(2) { questions.shuffle() }
                if (questions.size > numberOfQuestions) {
                    questions = ArrayList(questions.take(numberOfQuestions))
                }
            }
        }

        fun getNumberOfSections(): Int = paperData?.numberOfSections ?: 0

        fun updateSectionScoreAt(sectionIndex: Int, score: Int) {
            sectionsScores.value?.let {
                if (sectionIndex < it.size) {
                    it[sectionIndex] = score
                    paperScore.value = it.sum()
                    updateGrade()
                }
            }
        }

        fun getSectionsScores(): List<Int> = sectionsScores.value ?: emptyList()

        fun resetSectionScoreAt(sectionIndex: Int) {
            updateSectionScoreAt(sectionIndex, 0)
        }

        fun getPaperScore(): LiveData<Int> = paperScore

        fun updateSectionsAnsweredAt(sectionIndex: Int) {
            if (sectionIndex < sectionsAnsweredData.size) {
                sectionsAnsweredData[sectionIndex] = true
                sectionsAnsweredCount.value = sectionsAnsweredData.count { it }
                unAnsweredSectionIndexes.remove(sectionIndex)
                updateGrade()
            }
        }

        fun getSectionsAnswered(): List<Boolean> = sectionsAnsweredData

        fun getSectionNumberAt(position: Int): String? = getSectionNames()?.getOrNull(position)

        fun getSectionAnsweredAt(position: Int): Boolean = sectionsAnsweredData.getOrNull(position) ?: false

        fun decrementCurrentSectionRetryCount() {
            currentSectionRetryCount.value = (currentSectionRetryCount.value ?: 0) - 1
        }

        fun resetCurrentSectionRetryCount() {
            currentSectionRetryCount.value = MCQConstants.SECTION_RETRY_LIMIT
        }

        fun getCurrentSectionRetryCount(): LiveData<Int> = currentSectionRetryCount

        fun setUserMarkedAnswerSheet(userMarkedAnswersSheetData: UserMarkedAnswersSheetData) {
            this.userMarkedAnswersSheetData = userMarkedAnswersSheetData
        }

        fun getUserMarkedAnswerSheet(): UserMarkedAnswersSheetData? = userMarkedAnswersSheetData

        fun setSectionResultData(sectionResultData: SectionResultData) {
            this.sectionResultData = sectionResultData
        }

        fun getSectionResultData(): SectionResultData? = sectionResultData

        fun getTotalNumberOfQuestions(): Int = paperData?.numberOfQuestions ?: 0

        fun getNumberOfSectionsAnswered(): LiveData<Int> = sectionsAnsweredCount

        fun getSectionNames(): Array<String>? = paperData?.sections?.map { it.title }?.toTypedArray()

        fun getSectionNameBundleList(): Array<Bundle>? {
            return paperData?.sections?.map { section ->
                Bundle().apply {
                    putString("sectionName", section.title)
                    putString("numberOfQuestions", section.numberOfQuestions.toString())
                }
            }?.toTypedArray()
        }

        private fun updateGrade() {
            val totalQuestions = getTotalNumberOfQuestions()
            if (totalQuestions > 0 && (sectionsAnsweredCount.value ?: 0) > 0) {
                paperPercentage.value = ((paperScore.value ?: 0).toDouble() / totalQuestions * 100).toInt()

                paperGrade.value = when (paperPercentage.value ?: 0) {
                    in 75..100 -> "A Grade"
                    in 65..74 -> "B Grade"
                    in 50..64 -> "C Grade"
                    in 40..49 -> "D Grade"
                    in 30..39 -> "E Grade"
                    else -> "U Grade"
                }
            }
            
            val numSections = getNumberOfSections()
            if (numSections > 0 && sectionsAnsweredCount.value == numSections) {
                areAllSectionsAnswered.value = true
            }
        }

        fun getPaperGrade(): LiveData<String?> = paperGrade

        fun getPaperPercentage(): LiveData<Int> = paperPercentage

        fun getAreAllSectionsAnswered(): LiveData<Boolean> = areAllSectionsAnswered
    }
}
