package com.example.gceolmcqs

import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.UserData
import com.example.gceolmcqs.repository.AppDataRepository
import com.example.gceolmcqs.repository.RestRepository
import com.example.gceolmcqs.repository.SubscriptionDataRepository
import com.example.gceolmcqs.repository.UserDataRepository
import com.example.gceolmcqs.roomDB.UserDataDao
import com.google.gson.Gson
import org.json.JSONObject

class AppSetupManager {


    companion object{
        private var userDataRepository:UserDataRepository? = null
        private var restRepository: RestRepository = RestRepository()
        private var subscriptionDataRepository = SubscriptionDataRepository()
        private var appDataRepository = AppDataRepository()
        private var id: String? = null
    }

    fun initRepositories(id: String, userDataDao: UserDataDao){
        AppSetupManager.id = id
        userDataRepository = UserDataRepository(userDataDao)
    }

    fun start(listener: AppSetupListener){
        loadUserDataFromLocaldb(listener)
    }

    private fun loadUserDataFromLocaldb(listener: AppSetupListener){
        userDataRepository?.loadUserDataFromLocaldb(object: UserDataRepository.UserDataRepositoryListener{
            override fun onSuccess() {
                setupMainRepositories()
               listener.onSetupSuccessful()
            }

            override fun onError(error: String) {
                signupUser(listener)
            }

        })
//        if app data loaded successfully trigger onSetupSuccessful else sign up user
    }

    private fun signupUser(listener: AppSetupListener){

        val subjectPackageData = SubjectPackageActivator.activateTrialPackage()
        val subjectPackageDataString = Gson().toJson(subjectPackageData)
        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!, MCQConstants.SUBSCRIPTION to subjectPackageDataString)
        restRepository.query(RestRepository.SIGN_UP, params, object: RestRepository.OnQueryListener{
            override fun onSuccess(result: String) {

                val appData = JSONObject(result).getString("appData")
                val subscriptionData = JSONObject(result).getString("subscription")
                val userData = UserData(0, subscriptionData, appData)
                writeUserDataToLocaldb(userData, listener)
            }

            override fun onError(error: String?) {
                listener.onSetupFailed()
            }


        })
    }

    private fun writeUserDataToLocaldb(userData: UserData, listener: AppSetupListener){
        userDataRepository?.insertUserDataToLocaldb(userData, object: UserDataRepository.UserDataRepositoryListener{
            override fun onSuccess() {
//                listener.onSetupSuccessful()
                loadUserDataFromLocaldb(listener)
            }

            override fun onError(error: String) {
                listener.onSetupFailed()

            }

        })
    }

    fun isUserDataInitialised(): Boolean{
        return userDataRepository != null
    }

    private fun setupMainRepositories(){
        setupAppData()
        setupSubscriptionData()
    }

    private fun setupAppData(){
        appDataRepository.updateAppData(userDataRepository?.getAppDataString())
    }

    private fun setupSubscriptionData(){
        subscriptionDataRepository.initSubscriptionData(userDataRepository?.getSubscriptionData()!!)
    }

    fun isSubscriptionActive(): Boolean{
        return subscriptionDataRepository.isSubscriptionActive()!!
    }


    fun getSubscriptionTimeRemaining(): Long{
        return subscriptionDataRepository.getSubscriptionTimeRemaining()
    }

    fun getSubscriptionData(): SubjectPackageData{
        return subscriptionDataRepository.getSubscription()
    }

    fun updateSubscriptionDataInLocaldb(subjectPackageData: SubjectPackageData){
        subscriptionDataRepository.updateSubscriptionData(subjectPackageData)
//        updateSubscriptionDataInRemoteRepo(subjectPackageData)

//        val subscriptionDataString = Gson().toJson(subjectPackageData).toString()
    }

   fun updateSubscriptionDataInRemoteRepo(subjectPackageData: SubjectPackageData, listener: SubscriptionDataRepository.SubscriptionListener){
        val subscriptionDataString = Gson().toJson(subjectPackageData)
        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!, MCQConstants.SUBSCRIPTION to subscriptionDataString)
        restRepository.query(RestRepository.UPDATE_SUBSCRIPTION, params, object :RestRepository.OnQueryListener{
            override fun onSuccess(result: String) {
                updateSubscriptionDataInLocaldb(subjectPackageData)
                listener.onSubscriptionUpdated()
            }

            override fun onError(error: String?) {

            }

        })
    }

    interface AppSetupListener{

        fun onSetupSuccessful()
        fun onSetupFailed()
    }
}