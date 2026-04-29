package com.example.gceolmcqs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.datamodels.ExamScoreEntity
import com.example.gceolmcqs.repository.ExamScoreDataRepository
import com.example.gceolmcqs.repository.Paper1DataRepository

class ExamTypeFragmentViewModel: ViewModel() {
    private val _performanceData = MutableLiveData<Pair<String, ExamScoreEntity?>?>()
    val performanceData: LiveData<Pair<String, ExamScoreEntity?>?> = _performanceData

    fun getExamItemTitles(subjectIndex: Int, fragmentIndex: Int): ArrayList<String>{
        val examItemTitles = ArrayList<String>()
        examItemTitles.addAll(Paper1DataRepository.getExamItemTitles(subjectIndex, fragmentIndex))
        return examItemTitles
    }

    fun loadPerformanceData(examItemTitle: String) {
        ExamScoreDataRepository.getScoreByTitle(examItemTitle, object : ExamScoreDataRepository.OnScoreLoadedListener {
            override fun onScoreLoaded(examScore: ExamScoreEntity?) {
                _performanceData.value = Pair(examItemTitle, examScore)
            }
        })
    }

    fun clearPerformanceData() {
        _performanceData.value = null
    }
}
