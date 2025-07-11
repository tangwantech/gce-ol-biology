package com.example.gceolmcqs.repository

import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.SubjectsPackages

class SubjectsPackagesDataRepository {
    companion object{
        private var subjectsPackages: SubjectsPackages? = null
        fun initSubjectsPackages(subjectsPackages: SubjectsPackages){
            this.subjectsPackages = subjectsPackages
        }

        fun isSubjectsPackagesInitialised(): Boolean{
            return subjectsPackages != null
        }

        fun getSubjectPackagesList(): List<SubjectPackageData>{
            return subjectsPackages?.subjectsPackages!!
        }

        fun getSubjectPackageAt(subjectIndex: Int): SubjectPackageData{
            return subjectsPackages?.subjectsPackages!![subjectIndex]
        }

        fun updateSubjectPackageAt(subjectIndex: Int, subjectPackageData: SubjectPackageData){
            subjectsPackages?.subjectsPackages!![subjectIndex] = subjectPackageData
        }

        fun getSubjectsPackages(): SubjectsPackages{
            return subjectsPackages!!
        }

        fun getSubscriptionTimeRemainingAt(subjectIndex: Int): Long{
            val subscriptionData = subjectsPackages?.subjectsPackages!![subjectIndex]
            return ActivationExpiryDatesGenerator.getTimeRemaining(subscriptionData.activatedOn!!, subscriptionData.expiresOn!!)
        }

        fun isSubscriptionActiveAt(subjectIndex: Int): Boolean{
            val subscriptionData = subjectsPackages?.subjectsPackages!![subjectIndex]
            return ActivationExpiryDatesGenerator().checkExpiry(subscriptionData.activatedOn!!, subscriptionData.expiresOn!!)
        }
    }
}