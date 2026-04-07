package com.example.gceolmcqs.datamodels

data class Chapter(val chapter:String, val module:String, val categoryOfActions:String, val lessons: String)
data class Class(val className:String, val chapters: List<Chapter>)
data class SyllabusData(val syllabus: List<Class>)
