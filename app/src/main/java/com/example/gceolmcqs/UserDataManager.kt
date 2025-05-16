package com.example.gceolmcqs

import android.content.Context
import com.example.gceolmcqs.datamodels.Paper1Data
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.UserData
import com.example.gceolmcqs.repository.DictionaryRepository
//import com.example.gceolmcqs.repository.SubscriptionDataRepository
import com.example.gceolmcqs.repository.LocalUserDataRepository
import com.example.gceolmcqs.repository.RemoteDatabaseManager

import com.google.gson.Gson


class UserDataManager {
    companion object{

        private var id: String? = null
    }

    fun initUserDataDao(context: Context){
        LocalUserDataRepository.initUserDataDao(context)
    }

    fun initID(id: String){
        UserDataManager.id = id
    }


    fun isUserDataInitialised(): Boolean{
        return LocalUserDataRepository.isUserDataInitialised()
    }



    fun start(listener: UserDataManagerListener){
        loadUserDataFromLocaldb(listener)
    }


    private fun loadUserDataFromLocaldb(listener: UserDataManagerListener){
        LocalUserDataRepository.loadUserDataFromLocaldb(object: LocalUserDataRepository.OnLoadUserDataListener{
            override fun onUserDataLoaded() {
//                initAppData()
//                setupSubscriptionData()
               listener.onSuccess()
            }

            override fun onUserDataUnavailable(error: String) {
                signupUser(listener)
            }

        })
//        if app data loaded successfully trigger onSetupSuccessful else sign up user
    }

    private fun signupUser(listener: UserDataManagerListener){

        val subjectPackageData = SubjectPackageActivator.activateTrialPackage()
        val subjectPackageDataString = Gson().toJson(subjectPackageData)
        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!, MCQConstants.SUBSCRIPTION to subjectPackageDataString)
        RemoteDatabaseManager.signupUser(params, object: RemoteDatabaseManager.OnSignupListener{
            override fun onSignupSuccessful(userData: UserData) {
                LocalUserDataRepository.insertUserDataToLocaldb(userData, object:LocalUserDataRepository.OnInsertUserDataListener{
                    override fun onInsertSuccessful() {
                        LocalUserDataRepository.loadUserDataFromLocaldb(object : LocalUserDataRepository.OnLoadUserDataListener{
                            override fun onUserDataLoaded() {
                                listener.onSuccess()
                            }

                            override fun onUserDataUnavailable(error: String) {
                                listener.onUserDataUnavailable()
                            }

                        })
                    }
                })
            }

            override fun onError(error: String?) {

            }
        })
//        restRepository.query(RestRepository.SIGN_UP, params, object: RestRepository.OnQueryListener{
//            override fun onSuccess(result: String) {
//
//                val appData = JSONObject(result).getString("appData")
//                val subscriptionData = JSONObject(result).getString("subscription")
//                val userData = UserData(0, subscriptionData, appData)
//                writeUserDataToLocaldb(userData, listener)
//            }
//
//            override fun onError(error: String?) {
//                listener.onUserDataUnavailable()
//            }
//
//
//        })
    }

    fun getPaper1Data(): Paper1Data{
        return LocalUserDataRepository.getPaper1Data()
    }

    fun isSubscriptionActive(): Boolean{
//        println(userDataRepository)
        return LocalUserDataRepository.isSubscriptionActive()!!
    }

    fun getSubscriptionTimeRemaining(): Long{
        return LocalUserDataRepository.getSubscriptionTimeRemaining()
    }

    fun getSubscriptionData(): SubjectPackageData{
        return LocalUserDataRepository.getSubscriptionData()!!
    }

    fun updateSubscriptionDataInLocaldb(subjectPackageData: SubjectPackageData, listener: LocalUserDataRepository.OnUpdateUserDataListener){
        LocalUserDataRepository.updateSubscriptionInUserData(subjectPackageData, object: LocalUserDataRepository.OnUpdateUserDataListener{
            override fun onUpdateSuccessful() {
                listener.onUpdateSuccessful()
            }


        })
    }

   fun updateSubscriptionDataInRemoteRepo(subjectPackageData: SubjectPackageData, listener: RemoteDatabaseManager.OnUpdateListener){
        val subscriptionDataString = Gson().toJson(subjectPackageData)
        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!, MCQConstants.SUBSCRIPTION to subscriptionDataString)
        RemoteDatabaseManager.updateUserSubscriptionInRemoteDatabase(params, object :RemoteDatabaseManager.OnUpdateListener{
            override fun onUpdateSuccessful() {
                LocalUserDataRepository.updateSubscriptionInUserData(subjectPackageData, object :LocalUserDataRepository.OnUpdateUserDataListener{
                    override fun onUpdateSuccessful() {
                        listener.onUpdateSuccessful()
                    }
                })
            }

            override fun onError(error: String?) {

            }
        })
    }

    fun initDictionaryRepository(){
        LocalUserDataRepository.initDictionaryRepository()
    }

    fun getDictionaryKeywords(): List<String>{
        return LocalUserDataRepository.getDictionaryKeywords()
    }

    fun getDefinition(keyword: String): String{
        return LocalUserDataRepository.getDefinition(keyword)
    }

    fun getAllMatches(keyword: String): List<String>{
        return LocalUserDataRepository.getAllMatches(keyword)
    }


    interface UserDataManagerListener{

        fun onSuccess()
        fun onUserDataUnavailable()
    }
}