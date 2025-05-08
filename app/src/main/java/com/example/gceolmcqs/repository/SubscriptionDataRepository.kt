package com.example.gceolmcqs.repository

import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.google.gson.Gson

class SubscriptionDataRepository {
    companion object{
        private var subscriptionData: SubjectPackageData? = null
    }

    fun initSubscriptionData(subjectPackageString: String){
        subscriptionData = Gson().fromJson(subjectPackageString, SubjectPackageData::class.java)
    }

    fun isSubscriptionActive(): Boolean?{
        return if (subscriptionData != null){
            ActivationExpiryDatesGenerator().checkExpiry(subscriptionData?.activatedOn!!, subscriptionData?.expiresOn!!)
        }else{
            null
        }
    }

    fun updateSubscriptionData(subjectPackageData: SubjectPackageData){
        subscriptionData = subjectPackageData
    }

    fun getSubjectPackageList(): List<SubjectPackageData>{
        val temp = ArrayList<SubjectPackageData>()
        temp.add(subscriptionData!!)
        return temp
    }

    fun getSubscription(): SubjectPackageData{
        return subscriptionData!!
    }

    fun getSubscriptionTimeRemaining(): Long{
        return ActivationExpiryDatesGenerator.getTimeRemaining(subscriptionData?.activatedOn!!, subscriptionData?.expiresOn!!)
    }

    interface SubscriptionListener{
        fun onSubscriptionUpdated()
    }

}