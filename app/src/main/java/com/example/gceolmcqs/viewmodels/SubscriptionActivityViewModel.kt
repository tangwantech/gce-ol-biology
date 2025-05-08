package com.example.gceolmcqs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.AppSetupManager
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.MomoPayService

import com.example.gceolmcqs.SubjectPackageActivator
import com.example.gceolmcqs.datamodels.PackageFormData
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.SubscriptionFormData
//import com.example.gceolmcqs.repository.RemoteRepoManager
import com.example.gceolmcqs.repository.RestRepository
import com.example.gceolmcqs.repository.SubscriptionDataRepository
import com.google.gson.Gson

class SubscriptionActivityViewModel: ViewModel() {
    private lateinit var momoPay: MomoPayService

    private var _subscriptionData = SubscriptionFormData()

    private val _transactionStatus = MutableLiveData<String?>()
    val transactionStatus: LiveData<String?> = _transactionStatus

    private var _refNumber: String? = null

    private val _packageUpdateStatus = MutableLiveData<Boolean>()
    val packageUpdateStatus: LiveData<Boolean> = _packageUpdateStatus

    private val _momoPartner = MutableLiveData<String>()
    val momoPartner: LiveData<String> = _momoPartner

    val appSetupManager = AppSetupManager()

    fun initSubscriptionFormData(subjectIndex: Int, subjectName: String){
        _subscriptionData.subjectPosition = subjectIndex
        _subscriptionData.subject = subjectName
    }

    fun updateSubscriptionPackageTypePriceAndDuration(packageFormData: PackageFormData){
        _subscriptionData.packageType = packageFormData.packageName
        _subscriptionData.packagePrice = packageFormData.price
        _subscriptionData.packageDuration = packageFormData.duration
    }

    fun updateMoMoPartner(momoPartner: String){
        _subscriptionData.momoPartner = momoPartner
    }

    fun updateMomoNumber(momoNumber: String){
        _subscriptionData.momoNumber = momoNumber
    }


    fun setMomoPayService(momoPayService: MomoPayService){
        momoPay = momoPayService

    }

    fun activateSubjectPackage(id:String) {
        val subjectIndex = _subscriptionData.subjectPosition!!
        val subjectName = _subscriptionData.subject!!
        val packageType = _subscriptionData.packageType!!
        val packageDuration = _subscriptionData.packageDuration!!
        val activatedSubjectPackageData = SubjectPackageActivator.activateSubjectPackage(subjectName, subjectIndex, packageType, packageDuration)
        updateActivatedPackageInRemoteRepo(id, activatedSubjectPackageData)
    }

    fun getSubjectPackageType(): String{
        return _subscriptionData.packageType!!
    }

    fun getSubjectName(): String{
        return _subscriptionData.subject!!
    }

    fun getMomoNumber(): String{
        return _subscriptionData.momoNumber!!
    }

    fun getPackagePrice(): String{
        return _subscriptionData.packagePrice!!
    }

    fun getMomoPartner(): String{
        return _subscriptionData.momoPartner!!
    }

    fun initiatePayment(){

        momoPay.initiatePayment(_subscriptionData, object: MomoPayService.TransactionStatusListener{
            override fun onTransactionTokenAvailable(token: String?) {
                println(token)
//                updateCurrentTransactionToken(token!!)
            }

            override fun onTransactionAvailable(transactionId: String?, ussdCode: String, operator: String) {
//                println("Transaction id: $transactionId")
                _momoPartner.postValue(operator)

            }

            override fun onReferenceNumberAvailable(refNum: String?) {
                _refNumber = refNum
            }

            override fun onTransactionPending() {
//                println("Transaction pending......")
                _transactionStatus.postValue(MCQConstants.PENDING)


            }

            override fun onTransactionFailed() {
//                println("Transaction failed.......")
                _transactionStatus.postValue(MCQConstants.FAILED)
//                updateCurrentTransactionStatus(MCQConstants.FAILED)

            }

            override fun onTransactionSuccessful() {
//                println("Transaction successful.....")
                _transactionStatus.postValue(MCQConstants.SUCCESSFUL)
//                updateCurrentTransactionStatus(MCQConstants.SUCCESSFUL)

            }

            override fun onNetWorkError() {
                _transactionStatus.postValue(MCQConstants.NETWORK_ERROR)
            }

        })

    }

//    fun getIsPaymentSystemAvailable():LiveData<Boolean?>{
//        return momoPay.isPaymentSystemAvailable
//    }

    private fun updateActivatedPackageInRemoteRepo(id: String, activatedSubjectPackageData: SubjectPackageData){

//        val subscription = Gson().toJson(activatedSubjectPackageData)
//        val params = hashMapOf<String, String>(MCQConstants.USER_NAME to id, MCQConstants.PASS_WORD to id, MCQConstants.SUBSCRIPTION to subscription)
//        RestRepository().query(RestRepository.UPDATE_SUBSCRIPTION, params, object: RestRepository.OnQueryListener{
//            override fun onSuccess(result: String) {
//                appSetupManager.updateSubscriptionDataInLocaldb(activatedSubjectPackageData)
//                _packageUpdateStatus.postValue(true)
//            }
//
//            override fun onError(error: String?) {
//                _packageUpdateStatus.postValue(false)
//            }
//        })

        appSetupManager.updateSubscriptionDataInRemoteRepo(activatedSubjectPackageData, object: SubscriptionDataRepository.SubscriptionListener{
            override fun onSubscriptionUpdated() {
                appSetupManager.updateSubscriptionDataInLocaldb(activatedSubjectPackageData)
                _packageUpdateStatus.postValue(true)
            }
        })
    }





}
