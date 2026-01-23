package com.example.gceolmcqs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.MomoPayService

import com.example.gceolmcqs.SubjectPackageActivator
import com.example.gceolmcqs.datamodels.CampayCredentials
import com.example.gceolmcqs.datamodels.PackageFormData
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.SubscriptionFormData
import com.example.gceolmcqs.repository.RemoteDatabaseManager
import com.example.gceolmcqs.repository.RemoteDatabaseManager.OnQueryCampayCredentialsListener
import com.example.gceolmcqs.repository.RestRepository
import org.json.JSONObject

//import com.example.gceolmcqs.repository.RemoteRepoManager
//import com.example.gceolmcqs.repository.SubscriptionDataRepository

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

    private val userDataManager = UserDataManager()

    private var packageTypes: String? = null

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

    fun activateSubjectPackage(id:String, listener: RemoteDatabaseManager.OnUpdateListener) {
        val subjectIndex = _subscriptionData.subjectPosition!!
        val subjectName = _subscriptionData.subject!!
        val packageType = _subscriptionData.packageType!!
        val packageDuration = _subscriptionData.packageDuration!!
        val activatedSubjectPackageData = SubjectPackageActivator.activateSubjectPackage(subjectName, subjectIndex, packageType, packageDuration)
        updateSubscriptionDataInRemoteRepo(id, activatedSubjectPackageData, listener)
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

//    private fun getCampayCredentials(id: String, object: ){
//        val params = hashMapOf(MCQConstants.USER_NAME to id!!, MCQConstants.PASS_WORD to id!!)
//    }
    fun queryCampayCredentials(
        params: HashMap<String, String>,
        listener: OnQueryCampayCredentialsListener
    ) {
        RestRepository().query(
            RestRepository.GET_CAMPAY_CREDENTIALS,
            params,
            object : RestRepository.OnQueryListener {
                override fun onSuccess(result: String) {

                    val campayUsername = JSONObject(result).getString("username")
                    val campayPassword = JSONObject(result).getString("password")
                    val campayTokenUri = JSONObject(result).getString("tokenUri")
                    val requestToPayUri = JSONObject(result).getString("requestToPayUri")
                    val transactionStatusUri = JSONObject(result).getString("transactionStatusUri")
                    val campayCredentials = CampayCredentials("", campayUsername, campayPassword, campayTokenUri, requestToPayUri, transactionStatusUri)

//                    initiatePayment(campayCredentials)
                    listener.onSuccess(campayCredentials)
                }

                override fun onError(error: String?) {
                    listener.onError(error)
                }
            })
    }

    fun initiatePayment(campayCredentials: CampayCredentials){

        momoPay.initiatePayment(campayCredentials, _subscriptionData, object: MomoPayService.TransactionStatusListener{
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

    private fun updateSubscriptionDataInRemoteRepo(id: String, activatedSubjectPackageData: SubjectPackageData, listener: RemoteDatabaseManager.OnUpdateListener){

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

        userDataManager.updateSubscriptionDataInRemoteRepo(activatedSubjectPackageData, listener)
    }


    fun queryPackageTypesFromRemoteRepo(params: HashMap<String, String>, listener: RemoteDatabaseManager.OnQueryListener){
        RemoteDatabaseManager.queryPackageTypes(params, object: RemoteDatabaseManager.OnQueryPackageTypesListener{
            override fun onSuccess(result: String) {
//                println("PackageTypes: $result")
                packageTypes = result
                listener.onSuccess()
            }

            override fun onError(error: String?) {
                listener.onError(error)
            }

        })
    }

    fun getPackageTypes(): String{
        return packageTypes!!
    }


}
