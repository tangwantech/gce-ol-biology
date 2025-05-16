//package com.example.gceolmcqs.repository
//
//import com.example.gceolmcqs.ActivationExpiryDatesGenerator
//import com.example.gceolmcqs.datamodels.SubjectPackageData
//
//class SubscriptionDataRepository {
//    private var subscriptionData: SubjectPackageData? = null
//
//    fun initSubscriptionData(subjectPackageData: SubjectPackageData){
//        this.subscriptionData = subjectPackageData
//    }
//
//    fun isSubscriptionActive(): Boolean?{
//        return if (subscriptionData != null){
//            ActivationExpiryDatesGenerator().checkExpiry(subscriptionData?.activatedOn!!, subscriptionData?.expiresOn!!)
//        }else{
//            null
//        }
//    }
//
//    fun updateSubscriptionData(subjectPackageData: SubjectPackageData){
//        subscriptionData = subjectPackageData
//    }
//
//    fun getSubjectPackageList(): List<SubjectPackageData>{
//        val temp = ArrayList<SubjectPackageData>()
//        temp.add(subscriptionData!!)
//        return temp
//    }
//
//    fun getSubscription(): SubjectPackageData{
//        return subscriptionData!!
//    }
//
//    fun getSubscriptionTimeRemaining(): Long{
//        return ActivationExpiryDatesGenerator.getTimeRemaining(subscriptionData?.activatedOn!!, subscriptionData?.expiresOn!!)
//    }
//
//    interface SubscriptionUpdateListener{
//        fun onSubscriptionUpdated()
//    }
//
//}