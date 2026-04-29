package com.example.gceolmcqs.repository

import android.content.Context
import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.datamodels.AppData
import com.example.gceolmcqs.datamodels.DictionaryData
import com.example.gceolmcqs.datamodels.NotesData
import com.example.gceolmcqs.datamodels.Paper1Data
import com.example.gceolmcqs.datamodels.Paper2Data
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.SubjectsPackages
import com.example.gceolmcqs.datamodels.UserData
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
import com.example.gceolmcqs.roomDB.UserDataDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LocalUserDataRepository{
    companion object{
        private var userData: UserData? = null
        private var userDataDao: UserDataDao? = null
        fun initUserDataDao(context: Context){
            this.userDataDao = GceOLMcqDatabase.getDatabase(context).userDataDao()
        }
        fun insertUserDataToLocaldb(userData: UserData, listener: OnInsertUserDataListener){
            CoroutineScope(Dispatchers.IO).launch{
                userDataDao?.insertUserData(userData)
                withContext(Dispatchers.Main){
                    listener.onInsertSuccessful()
                }
            }
        }

        private fun updateUserDataInLocaldb(listener: OnUpdateUserDataListener){
            CoroutineScope(Dispatchers.IO).launch {
                userData?.let {
                    userDataDao?.update(it)
                    withContext(Dispatchers.Main){
                        listener.onUpdateSuccessful()
                    }
                }
            }
        }

        fun loadUserDataFromLocaldb(listener: OnLoadUserDataListener){
            CoroutineScope(Dispatchers.IO).launch{
                val tempList = userDataDao?.getUserData()
                withContext(Dispatchers.Main){
                    tempList?.let {
                        if (it.isNotEmpty()){
                            userData = tempList[0]
                            initSubjectsPackagesDataRepository()
                            listener.onUserDataLoaded()
                        }else{
                            listener.onUserDataUnavailable("No data found")
                        }
                    } ?: run {
                        listener.onUserDataUnavailable("No data found")
                    }
                }
            }
        }

        fun updateSubscriptionInUserData(subjectPackageData: SubjectPackageData, listener: OnUpdateUserDataListener){
            subjectPackageData.subjectIndex?.let { updateSubjectPackageAt(it, subjectPackageData) }
            userData?.subscription = Gson().toJson(getSubjectsPackages())
            updateUserDataInLocaldb(listener)


        }


        fun updateAppData(appData: AppData, listener: OnUpdateUserDataListener){
            userData?.appData = Gson().toJson(appData)
            updateUserDataInLocaldb(listener)

        }

        private fun getAppData(): AppData?{
            return if (userData != null){
                Gson().fromJson(userData!!.appData, AppData::class.java)
            }else{
                null
            }
        }

        fun getPaper1Data(): Paper1Data? {
            val appData = getAppData() ?: return null
            return Gson().fromJson(appData.paper1Data, Paper1Data::class.java)
        }


//        fun getPaper2Data(): Paper2Data{
//            val paper2Data = Gson().fromJson(getAppData()?.paper2Data, Paper2Data::class.java)
//            return paper2Data
//        }

        fun isUserDataInitialised(): Boolean{
            return userData != null
        }

        fun initDictionaryRepository(){
            val appData = getAppData() ?: return
            val dictionaryString = appData.dictionaryData ?: return
            try {
                val dictionaryJson = JSONObject(dictionaryString).getString("definitions").toString()
                val type = object : TypeToken<List<DictionaryData>>() {}.type
                val dictionary = Gson().fromJson<List<DictionaryData>>(dictionaryJson, type)
                DictionaryRepository.updateDictionaryData(dictionary)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getDictionaryKeywords(): List<String>{
            return DictionaryRepository.getKeys()
        }

        fun getDefinition(keyword: String): String{
            return DictionaryRepository.getDefinition(keyword)
        }

        fun getAllMatches(keyword: String): List<String>{
            return DictionaryRepository.getAllMatches(keyword)
        }



        private fun initSubjectsPackagesDataRepository(){
            val subjectsPackages = Gson().fromJson(userData?.subscription, SubjectsPackages::class.java)
            SubjectsPackagesDataRepository.initSubjectsPackages(subjectsPackages)
        }

        private fun getSubjectsPackages(): SubjectsPackages?{
            return SubjectsPackagesDataRepository.getSubjectsPackages()
        }

        fun isSubjectsPackagesDataInitialised(): Boolean{
            return SubjectsPackagesDataRepository.isSubjectsPackagesInitialised()
        }

        fun getSubjectsPackagesList(): List<SubjectPackageData>{
            return SubjectsPackagesDataRepository.getSubjectPackagesList()
        }

        fun getSubjectPackageAt(subjectIndex: Int): SubjectPackageData?{
            return SubjectsPackagesDataRepository.getSubjectPackageAt(subjectIndex)
        }

        fun updateSubjectPackageAt(subjectIndex: Int, subjectPackageData: SubjectPackageData){
            SubjectsPackagesDataRepository.updateSubjectPackageAt(subjectIndex, subjectPackageData)

        }

        fun getSubscriptionTimeRemainingAt(subjectIndex: Int): Long{
            return SubjectsPackagesDataRepository.getSubscriptionTimeRemainingAt(subjectIndex)
        }

        fun isSubscriptionActiveAt(subjectIndex: Int): Boolean{
            return SubjectsPackagesDataRepository.isSubscriptionActiveAt(subjectIndex)
        }

        fun initPaper2DataRepository(){
            val appData = getAppData() ?: return
            val paper2Data = Gson().fromJson(appData.paper2Data, Paper2Data::class.java)
            Paper2DataRepository.initPaper2Data(paper2Data)

        }

        fun getPaper2SubjectNames(): List<String>{
            return Paper2DataRepository.getSubjectNames()
        }

        fun getPaper2SubjectNameAt(subjectIndex: Int): String{
            return Paper2DataRepository.getSubjectName(subjectIndex)
        }

        fun getPaper2ExamTitles(subjectIndex: Int): List<String>{
            return Paper2DataRepository.getExamTitles(subjectIndex)
        }

        fun getPaper2ExamItemTitles(subjectIndex: Int, examTypeIndex: Int): List<String>{
            return Paper2DataRepository.getExamItemTitles(subjectIndex, examTypeIndex)
        }

        fun getPaper2ExamItemTitle(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
            return Paper2DataRepository.getExamItemTitle(subjectIndex, examTypeIndex, examItemIndex)
        }

        fun getPaper2FilePath(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
            return Paper2DataRepository.getPaper2FilePath(subjectIndex, examTypeIndex, examItemIndex)
        }

        fun isPaper2DataInitialised(): Boolean{
            return Paper2DataRepository.isPaper2DataInitialised()
        }




        fun initNotesDataRepository(){
            val appData = getAppData() ?: return
            val notesString = appData.notesData ?: return
            try {
                val notesJson = JSONObject(notesString).getString("chapters").toString()
                val type = object : TypeToken<List<NotesData>>() {}.type
                val notes = Gson().fromJson<List<NotesData>>(notesJson, type)
                NotesDataRepository.initNotesData(notes)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getChapterNames(): List<String>{
            return NotesDataRepository.getChapterNames()
        }

        fun getFilePath(chapterIndex: Int): String{
            return NotesDataRepository.getFilePath(chapterIndex)
        }

        fun getChapterExerciseNumbers(chapterIndex: Int): List<String>{
            return NotesDataRepository.getChapterExerciseNumbers(chapterIndex)
        }

        fun getIsNotesInitialised(): Boolean{
            return NotesDataRepository.isNotesInitialised()
        }

        fun isPaper1Available(subjectIndex: Int): Boolean {
            return Paper1DataRepository.isPaper1AvailableAt(subjectIndex)
        }

        fun isPaper2Available(subjectIndex: Int): Boolean {
            return Paper2DataRepository.isPaper2Available(subjectIndex)
        }
    }

    interface OnLoadUserDataListener{
        fun onUserDataLoaded()
        fun onUserDataUnavailable(error: String)
    }

    interface OnInsertUserDataListener{
        fun onInsertSuccessful()
    }

    interface OnUpdateUserDataListener{
        fun onUpdateSuccessful()
    }

    interface OnSubscriptionDataUpdateListener{
        fun onSubscriptionDataUpdated()
    }


}