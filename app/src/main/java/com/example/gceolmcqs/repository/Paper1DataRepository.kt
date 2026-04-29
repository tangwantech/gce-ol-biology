package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.Paper1Data
import com.example.gceolmcqs.datamodels.PaperData
import com.google.gson.Gson

class Paper1DataRepository {
    companion object {
        private var paper1Data: Paper1Data? = null
//        fun initPaper1Data(){
//
////            paper1Data = RemoteRepoManager.getOLMCQDataFromParseUser()
////            callBack.onAppDataInitialised()
//        }
        fun initPaper1Data(paper1Data: Paper1Data?){
            Paper1DataRepository.paper1Data = paper1Data
//            println("Paper1Data within initPaper1Data: $paper1Data")
        }

        fun getSubjectNames(): List<String>{
            val subjectNames = ArrayList<String>()
            paper1Data?.subjects?.forEach {
                subjectNames.add(it.title)
            }
            return subjectNames
        }

        fun getSubjectName(subjectIndex: Int): String{
//            println(paper1Data)
            return paper1Data?.subjects?.getOrNull(subjectIndex)?.title ?: ""
        }

        fun getExamTitles(subjectIndex: Int): List<String>{
            val contentTitles = ArrayList<String>()
            paper1Data?.subjects?.getOrNull(subjectIndex)?.examTypes?.forEach { examType ->
                contentTitles.add(examType.title)
            }
            return contentTitles
        }

        fun getExamItemTitles(subjectIndex: Int, examTypeIndex: Int): List<String>{
            val examItemTitles = ArrayList<String>()
            paper1Data?.subjects?.getOrNull(subjectIndex)?.examTypes?.getOrNull(examTypeIndex)?.examItems?.forEach { examItem ->
                examItemTitles.add(examItem.title)
            }
            return examItemTitles
        }

        fun getExamItemTitle(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
            return paper1Data?.subjects?.getOrNull(subjectIndex)?.examTypes?.getOrNull(examTypeIndex)?.examItems?.getOrNull(examItemIndex)?.title ?: ""
        }

        fun getPaperData(subjectIndex: Int, contentIndex: Int, examItemIndex: Int): PaperData?{
            return paper1Data?.subjects?.getOrNull(subjectIndex)?.examTypes?.getOrNull(contentIndex)?.examItems?.getOrNull(examItemIndex)?.paperData
        }

        fun isPaper1DataInitialised(): Boolean{
            return paper1Data != null
        }

        fun isPaper1AvailableAt(subjectIndex: Int): Boolean {
            return paper1Data?.subjects?.getOrNull(subjectIndex) != null
        }

    }

    interface OnAppDataInitialiseListener{
        fun onAppDataInitialised()
    }
}