package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.Paper2Data


class Paper2DataRepository {
    companion object{
        private var paper2Data: Paper2Data? = null

        fun initPaper2Data(paper2Data: Paper2Data){
            Paper2DataRepository.paper2Data = paper2Data
        }

        fun getSubjectNames(): List<String>{
            val subjectNames = ArrayList<String>()
            paper2Data?.subjects?.forEach {
                subjectNames.add(it.title)
            }
            return subjectNames
        }

        fun getSubjectName(subjectIndex: Int): String{
//            println(paper1Data)
            return paper2Data?.subjects!![subjectIndex].title
        }

        fun getExamTitles(subjectIndex: Int): List<String>{
            val contentTitles = ArrayList<String>()
            paper2Data?.subjects!![subjectIndex].examTypes.forEach { examType ->
                contentTitles.add(examType.title)
            }
            return contentTitles
        }

        fun getExamItemTitles(subjectIndex: Int, examTypeIndex: Int): List<String>{
            val examItemTitles = ArrayList<String>()
            paper2Data?.subjects!![subjectIndex].examTypes[examTypeIndex].examItems.forEach { examItem ->
                examItemTitles.add(examItem.title)
            }
            return examItemTitles
        }

        fun getExamItemTitle(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
            return paper2Data?.subjects!![subjectIndex].examTypes[examTypeIndex].examItems[examItemIndex].title
        }

        fun getPaper2FilePath(subjectIndex: Int, contentIndex: Int, examItemIndex: Int): String {
            return "file:///android_asset/paper2/html/${paper2Data?.subjects!![subjectIndex].examTypes[contentIndex].examItems[examItemIndex].fileName}"
        }

        fun isPaper2DataInitialised(): Boolean{
            return paper2Data != null
        }
    }
}