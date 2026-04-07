package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.repository.SyllabusRepository

class SyllabusViewModel: ViewModel() {
    private lateinit var syllabusRepository: SyllabusRepository
    fun initSyllabusRepository(context:Context){
        syllabusRepository = SyllabusRepository()
        syllabusRepository.loadSyllabusFromAssert(context)
    }

    fun getChapterNamesForClassAt(classIndex: Int): List<String>{
        return syllabusRepository.getChapterNamesForClassAt(classIndex)
    }

    fun getChapterLessonsAt(classIndex: Int, chapterIndex: Int): List<String>{
        return syllabusRepository.getChapterLessonsAt(classIndex, chapterIndex)
    }

    fun getClassNameAt(classIndex: Int): String {
        return syllabusRepository.getClassNameAt(classIndex)
    }

    fun getChapterNameAt(classIndex: Int, chapterIndex: Int): String{
        return syllabusRepository.getChapterNameAt(classIndex, chapterIndex)
    }
}