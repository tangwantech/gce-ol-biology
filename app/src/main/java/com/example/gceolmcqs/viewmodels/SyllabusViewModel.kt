package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager
import com.example.gceolmcqs.UtilityFunctions
import com.example.gceolmcqs.datamodels.Lesson
import com.example.gceolmcqs.repository.SyllabusRepository

class SyllabusViewModel: ViewModel() {
    private lateinit var syllabusRepository: SyllabusRepository
    private val userDataManager = UserDataManager()

    fun initSyllabusRepository(context:Context){
        syllabusRepository = SyllabusRepository()
        syllabusRepository.loadSyllabusFromAssert(context)
        initDictionary(context)
    }

    private fun initDictionary(context: Context) {
        if (!userDataManager.isUserDataInitialised()) {
            val id = UtilityFunctions().getDeviceId(context)
            userDataManager.initUserDataDao(context)
            userDataManager.initID(id)
            userDataManager.start(object : UserDataManager.UserDataManagerListener {
                override fun onSuccess() {
                    userDataManager.initDictionaryRepository()
                }
                override fun onUserDataUnavailable() {}
            })
        } else {
            userDataManager.initDictionaryRepository()
        }
    }

    fun getChapterNamesForClassAt(classIndex: Int): List<String>{
        return syllabusRepository.getChapterNamesForClassAt(classIndex)
    }

    fun getChapterLessonsAt(classIndex: Int, chapterIndex: Int): List<Lesson>{
        return syllabusRepository.getChapterLessonsAt(classIndex, chapterIndex)
    }

    fun getClassNameAt(classIndex: Int): String {
        return syllabusRepository.getClassNameAt(classIndex)
    }

    fun getChapterNameAt(classIndex: Int, chapterIndex: Int): String{
        return syllabusRepository.getChapterNameAt(classIndex, chapterIndex)
    }
}