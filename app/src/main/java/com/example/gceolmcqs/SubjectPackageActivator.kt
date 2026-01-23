package com.example.gceolmcqs

import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.SubjectsPackages

class SubjectPackageActivator {
    companion object{

        fun activateTrialPackageForAllSubjectsAvailable(availableSubjects: List<String>?): SubjectsPackages{
            val activationExpiryDates =
                ActivationExpiryDatesGenerator.generateActivationExpiryDates(
                    MCQConstants.HOURS,
                    MCQConstants.TRIAL_DURATION
                )

            val packageDataList = ArrayList<SubjectPackageData>()
            availableSubjects?.forEachIndexed { subjectIndex, subject ->

                packageDataList.add(
                    SubjectPackageData(
                        subjectIndex,
                        subject,
                        "TRIAL",
                        activationExpiryDates.activatedOn,
                        activationExpiryDates.expiresOn,
                        isPackageActive = true
                    )
                )

            }
            return SubjectsPackages(packageDataList)
        }
        fun activateTrialPackage(): SubjectPackageData{
            val activationExpiryDates =
                ActivationExpiryDatesGenerator.generateActivationExpiryDates(
                    MCQConstants.HOURS,
                    MCQConstants.TRIAL_DURATION
                )
            return SubjectPackageData(0, MCQConstants.SUBJECTS_AVAILABLE[0], "TRIAL", activationExpiryDates.activatedOn, activationExpiryDates.expiresOn, isPackageActive = true)
        }

        fun activateSubjectPackage(tempSubjectName: String, tempSubjectIndex: Int, packageType: String, packageDuration: Int): SubjectPackageData {
            val activationExpiryDates =
                ActivationExpiryDatesGenerator.generateActivationExpiryDates(
                    MCQConstants.MINUTES,
                    packageDuration
                )

            val subjectPackageData = SubjectPackageData().apply {
                subjectIndex = tempSubjectIndex
                subjectName = tempSubjectName
                packageName = packageType
                activatedOn = activationExpiryDates.activatedOn
                expiresOn = activationExpiryDates.expiresOn
                isPackageActive = true
            }

            return subjectPackageData
        }

        fun activateBonus(subjectPackageData: SubjectPackageData, expiresOn: String): SubjectPackageData{
            subjectPackageData.expiresOn = expiresOn
            subjectPackageData.isPackageActive = true
            return subjectPackageData
        }


    }


}