package com.example.gceolmcqs.repository

import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.SubjectsPackages

class SubjectsPackagesDataRepository {
    companion object{
        private var subjectsPackages: SubjectsPackages? = null
        fun initSubjectsPackages(subjectsPackages: SubjectsPackages?){
            this.subjectsPackages = subjectsPackages
        }

        fun isSubjectsPackagesInitialised(): Boolean{
            return subjectsPackages != null
        }

        fun getSubjectPackagesList(): List<SubjectPackageData>{
            return subjectsPackages?.subjectsPackages ?: emptyList()
        }

        fun getSubjectPackageAt(subjectIndex: Int): SubjectPackageData?{
            return subjectsPackages?.subjectsPackages?.getOrNull(subjectIndex)
        }

        fun updateSubjectPackageAt(subjectIndex: Int, subjectPackageData: SubjectPackageData){
            subjectsPackages?.subjectsPackages?.let {
                if (subjectIndex in it.indices) {
                    it[subjectIndex] = subjectPackageData
                }
            }
        }

        fun getSubjectsPackages(): SubjectsPackages?{
            return subjectsPackages
        }

        fun getSubscriptionTimeRemainingAt(subjectIndex: Int): Long{
            val subscriptionData = subjectsPackages?.subjectsPackages?.getOrNull(subjectIndex)
            return if (subscriptionData?.activatedOn != null && subscriptionData.expiresOn != null) {
                ActivationExpiryDatesGenerator.getTimeRemaining(subscriptionData.activatedOn!!, subscriptionData.expiresOn!!)
            } else {
                0L
            }
        }

        fun isSubscriptionActiveAt(subjectIndex: Int): Boolean{
            val subscriptionData = subjectsPackages?.subjectsPackages?.getOrNull(subjectIndex)
            return if (subscriptionData?.activatedOn != null && subscriptionData.expiresOn != null) {
                ActivationExpiryDatesGenerator().checkExpiry(subscriptionData.activatedOn!!, subscriptionData.expiresOn!!)
            } else {
                false
            }
        }
    }
}