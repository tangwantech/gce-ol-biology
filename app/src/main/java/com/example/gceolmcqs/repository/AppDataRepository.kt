package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.AppData
import com.google.gson.Gson

class AppDataRepository {


    companion object{
        private var appData: AppData? = null
    }

    fun updateAppData(appDataString: String?){
        appDataString?.let {
            appData = Gson().fromJson(appDataString, AppData::class.java)
            initRepositories()
        }
    }

    private fun initRepositories(){
        initPaper1DataRepository()
    }

    private fun initPaper1DataRepository(){
        Paper1DataRepository.initPaper1Data(appData?.paper1Data!!)
    }


    fun getPaper1DataString(): String?{
        return appData?.paper1Data
    }

    fun getPaper2DataString(): String? {
        return appData?.paper2Data
    }

    fun getDictionaryDataString(): String?{
        return appData?.dictionaryData
    }

    fun getNotesDataString(): String?{
        return appData?.notesData
    }

    fun getExercisesDataString(): String?{
        return appData?.exercisesData
    }

    fun getAppDataString(): String?{
        return if (appData != null){
            Gson().toJson(appData)
        }else{
            null
        }
    }

}