package com.example.gceolmcqs.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.datamodels.ExamScoreEntity
import com.example.gceolmcqs.repository.ExamScoreDataRepository
import com.example.gceolmcqs.repository.PaperRepository

class SectionNavigationFragmentViewModel : ViewModel() {

//    fun getSectionNames(): Array<String>? {
//        return PaperRepository.getSectionNames()
//    }

    fun getSectionNameBundleList():Array<Bundle>?{
        return PaperRepository.getSectionNameBundleList()
    }

    fun getNumberOfSections(): Int {
        return PaperRepository.getNumberOfSections()
    }

    fun getSectionsAnswered(): List<Boolean> {
        return PaperRepository.getSectionsAnswered()
    }

    fun getNumberOfSectionsAnswered(): LiveData<Int> {
        return PaperRepository.getNumberOfSectionsAnswered()
    }

    fun getPaperScore(): LiveData<Int> {
        return PaperRepository.getPaperScore()
    }

    fun getTotalNumberOfQuestions(): Int {
        return PaperRepository.getTotalNumberOfQuestions()
    }

    fun getPaperGrade():LiveData<String?>{
        return PaperRepository.getPaperGrade()
    }

    fun getPaperPercentage():LiveData<Int>{
        return PaperRepository.getPaperPercentage()
    }

    fun getAreAllSectionsAnswered(): LiveData<Boolean>{
        return PaperRepository.getAreAllSectionsAnswered()
    }

    fun getSectionsScores(): ArrayList<Int>{
        val temp = ArrayList<Int>().apply {
            addAll(PaperRepository.getSectionsScores())
        }
        return temp
    }

    fun resetPaperRepo(){
        PaperRepository.resetPaperRepo()
    }

    fun updateExamScore(examItemTitle: String) {

        val sectionAnsweredCount = getNumberOfSectionsAnswered().value ?: 0
        if (sectionAnsweredCount > 0) {
            ExamScoreDataRepository.getScoreByTitle(examItemTitle, object : ExamScoreDataRepository.OnScoreLoadedListener {
                override fun onScoreLoaded(examScore: ExamScoreEntity?) {
                    val currentScoreValue = getPaperScore().value ?: 0
                    val currentGrade = getPaperGrade().value?.replace(" Grade", "") ?: "U"

                    println("currentScoreValue: $currentScoreValue")
                    
                    val updatedScore = if (examScore == null) {
                        ExamScoreEntity(
                            examItemTitle = examItemTitle,
                            attempts = 1,
                            highScore = currentScoreValue,
                            highGrade = currentGrade,
                            lowScore = currentScoreValue,
                            lowGrade = currentGrade,
                            averageScore = currentScoreValue,
                            recentScore = currentScoreValue,
                            recentGrade = currentGrade
                        )
                    } else {
                        val newAttempts = examScore.attempts + 1
                        val newHighScore = maxOf(examScore.highScore, currentScoreValue)
                        val newHighGrade = if (currentScoreValue >= examScore.highScore) currentGrade else examScore.highGrade
                        
                        val newLowScore = if (examScore.attempts == 0) currentScoreValue else minOf(examScore.lowScore, currentScoreValue)
                        val newLowGrade = if (examScore.attempts == 0 || currentScoreValue <= examScore.lowScore) currentGrade else examScore.lowGrade
                        
                        val newAverage = ((examScore.averageScore * examScore.attempts) + currentScoreValue) / newAttempts
                        
                        examScore.copy(
                            attempts = newAttempts,
                            highScore = newHighScore,
                            highGrade = newHighGrade,
                            lowScore = newLowScore,
                            lowGrade = newLowGrade,
                            averageScore = newAverage,
                            recentScore = currentScoreValue,
                            recentGrade = currentGrade
                        )
                    }
                    
                    if (examScore == null) {
                        ExamScoreDataRepository.insertScore(updatedScore)
                    } else {
                        ExamScoreDataRepository.updateScore(updatedScore)
                    }
                }
            })
        }
    }
}