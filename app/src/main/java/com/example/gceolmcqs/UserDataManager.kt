package com.example.gceolmcqs

import android.content.Context
import com.example.gceolmcqs.datamodels.Paper1Data
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.UserData
import com.example.gceolmcqs.repository.LocalUserDataRepository
import com.example.gceolmcqs.repository.RemoteDatabaseManager
import com.example.gceolmcqs.repository.SubjectsPackagesDataRepository

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

//        val subjectPackageData = SubjectPackageActivator.activateTrialPackage()

        val subjectsPackages = SubjectPackageActivator.activateTrialPackageForAllSubjectsAvailable(MCQConstants.SUBJECTS_AVAILABLE)

        val subscriptions = Gson().toJson(subjectsPackages)


//        val subjectPackageDataString = Gson().toJson(subjectPackageData)
        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!, MCQConstants.SUBSCRIPTION to subscriptions)
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


//    fun isSubscriptionActive(): Boolean{
////        println(userDataRepository)
//        return LocalUserDataRepository.isSubscriptionActive()!!
//    }
//
//    fun getSubscriptionTimeRemaining(): Long{
//        return LocalUserDataRepository.getSubscriptionTimeRemaining()
//    }
//
//    fun getSubscriptionData(): SubjectPackageData{
//        return LocalUserDataRepository.getSubscriptionData()!!
//    }

    fun updateSubscriptionDataInLocaldb(subjectPackageData: SubjectPackageData, listener: LocalUserDataRepository.OnUpdateUserDataListener){
        LocalUserDataRepository.updateSubscriptionInUserData(subjectPackageData, object: LocalUserDataRepository.OnUpdateUserDataListener{
            override fun onUpdateSuccessful() {
                listener.onUpdateSuccessful()
            }


        })
    }

   fun updateSubscriptionDataInRemoteRepo(subjectPackageData: SubjectPackageData, listener: RemoteDatabaseManager.OnUpdateListener){
        SubjectsPackagesDataRepository.updateSubjectPackageAt(subjectPackageData.subjectIndex!!, subjectPackageData)
        val subscriptions = Gson().toJson(SubjectsPackagesDataRepository.getSubjectsPackages())
        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!, MCQConstants.SUBSCRIPTION to subscriptions)
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



    fun initNotesDataRepository(){

        LocalUserDataRepository.initNotesDataRepository()
    }

    fun getPaper1ChapterNames(): List<String>{
        return LocalUserDataRepository.getChapterNames()
    }

    fun getFilePath(chapterIndex: Int): String{
        return LocalUserDataRepository.getFilePath(chapterIndex)
    }

    fun getChapterExerciseNumbers(chapterIndex: Int): List<String>{
        return LocalUserDataRepository.getChapterExerciseNumbers(chapterIndex)
    }

    fun getIsNotesInitialised(): Boolean{
        return LocalUserDataRepository.getIsNotesInitialised()
    }




    fun initPaper2DataRepository(){
        LocalUserDataRepository.initPaper2DataRepository()
    }

    fun getPaper2SubjectNames(): List<String>{
        return LocalUserDataRepository.getPaper2SubjectNames()
    }

    fun getPaper2SubjectNameAt(subjectIndex: Int): String{
        return LocalUserDataRepository.getPaper2SubjectNameAt(subjectIndex)
    }

    fun getPaper2ExamTitles(subjectIndex: Int): List<String>{
        return LocalUserDataRepository.getPaper2ExamTitles(subjectIndex)
    }

    fun getPaper2ExamItemTitles(subjectIndex: Int, examTypeIndex: Int): List<String>{
        return LocalUserDataRepository.getPaper2ExamItemTitles(subjectIndex, examTypeIndex)
    }

    fun getPaper2ExamItemTitle(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
        return LocalUserDataRepository.getPaper2ExamItemTitle(subjectIndex, examTypeIndex, examItemIndex)
    }

    fun getPaper2FilePath(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
        return LocalUserDataRepository.getPaper2FilePath(subjectIndex, examTypeIndex, examItemIndex)
    }

    fun getIsPaper2DataInitialised(): Boolean{
        return LocalUserDataRepository.isPaper2DataInitialised()
    }


//    fun initSubjectsPackagesDataRepository(){
//        LocalUserDataRepository.initSubjectsPackagesDataRepository()
//    }

    fun isSubjectsPackagesDataInitialised(): Boolean{
        return LocalUserDataRepository.isSubjectsPackagesDataInitialised()
    }

    fun getSubjectsPackagesList(): List<SubjectPackageData>{
        return LocalUserDataRepository.getSubjectsPackagesList()
    }

    fun getSubjectPackageAt(subjectIndex: Int): SubjectPackageData{
        return LocalUserDataRepository.getSubjectPackageAt(subjectIndex)
    }

    fun updateSubjectPackageAt(subjectIndex: Int, subjectPackageData: SubjectPackageData){
        LocalUserDataRepository.updateSubjectPackageAt(subjectIndex, subjectPackageData)

    }

    fun getSubscriptionTimeRemainingAt(subjectIndex: Int): Long{
        return LocalUserDataRepository.getSubscriptionTimeRemainingAt(subjectIndex)
    }

    fun isSubscriptionActiveAt(subjectIndex: Int): Boolean{
        return LocalUserDataRepository.isSubscriptionActiveAt(subjectIndex)
    }

    fun isPaper1Available(subjectIndex: Int): Boolean {
        return LocalUserDataRepository.isPaper1Available(subjectIndex)
    }

    fun isPaper2Available(subjectIndex: Int): Boolean {
        return LocalUserDataRepository.isPaper2Available(subjectIndex)
    }


    interface UserDataManagerListener{

        fun onSuccess()
        fun onUserDataUnavailable()
    }
}