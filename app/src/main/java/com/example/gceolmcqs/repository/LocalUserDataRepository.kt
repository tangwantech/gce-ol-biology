package com.example.gceolmcqs.repository

import android.content.Context
import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.datamodels.AppData
import com.example.gceolmcqs.datamodels.DictionaryData
import com.example.gceolmcqs.datamodels.Paper1Data
import com.example.gceolmcqs.datamodels.SubjectPackageData
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
                userDataDao?.update(userData!!)
                withContext(Dispatchers.Main){
                    listener.onUpdateSuccessful()
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
                            listener.onUserDataLoaded()
                        }else{
                            listener.onUserDataUnavailable("No data found")
                        }
                    }

                }
            }
        }

        fun updateSubscriptionInUserData(subscription: SubjectPackageData, listener: OnUpdateUserDataListener){
            userData?.subscription = Gson().toJson(subscription)
            updateUserDataInLocaldb(listener)


        }




        fun getSubscriptionData(): SubjectPackageData?{
            return if (userData != null){
                Gson().fromJson<SubjectPackageData>(userData!!.subscription, SubjectPackageData::class.java)
            }else{
                null
            }
        }

        fun getSubscriptionTimeRemaining(): Long{
            val subscriptionData = Gson().fromJson<SubjectPackageData>(userData!!.subscription, SubjectPackageData::class.java)
            return ActivationExpiryDatesGenerator.getTimeRemaining(subscriptionData?.activatedOn!!, subscriptionData.expiresOn!!)
        }

        fun isSubscriptionActive(): Boolean?{
            val subscriptionData = Gson().fromJson<SubjectPackageData>(userData!!.subscription, SubjectPackageData::class.java)
            return if (subscriptionData != null){
                ActivationExpiryDatesGenerator().checkExpiry(subscriptionData.activatedOn!!, subscriptionData.expiresOn!!)
            }else{
                null
            }
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

        fun getPaper1Data(): Paper1Data{
            val paper1Data = Gson().fromJson(getAppData()?.paper1Data, Paper1Data::class.java)
            return paper1Data
        }

        fun isUserDataInitialised(): Boolean{
            return userData != null
        }

        fun initDictionaryRepository(){
            val dictionaryString =  getAppData()?.dictionaryData!!
            val dictionaryJson = JSONObject(dictionaryString).getString("definitions").toString()
            val type = object :  TypeToken<List<DictionaryData>>(){}.type
            val dictionary = Gson().fromJson<List<DictionaryData>>(dictionaryJson, type)
            DictionaryRepository.updateDictionaryData(dictionary)
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