package com.example.gceolmcqs.repository

import android.content.Context
import com.example.gceolmcqs.AssertReader
import com.example.gceolmcqs.datamodels.SyllabusData
import com.google.gson.Gson

class SyllabusRepository {
    private lateinit var syllabusData: SyllabusData
    fun loadSyllabusFromAssert(context: Context){
        val syllabusJson = AssertReader.getJsonFromAssets(context, "syllabus.json")!!
        initSyllabusData(syllabusJson)

    }

    private fun initSyllabusData(str: String){
        syllabusData = Gson().fromJson(str, SyllabusData::class.java)
//        println(syllabusData)
    }

    fun getChapterNamesForClassAt(classIndex: Int): List<String>{
        val chapterNames = arrayListOf<String>()
        syllabusData.syllabus[classIndex].chapters.forEach {
            chapterNames.add(it.chapter)
        }
        return chapterNames
    }
//
    fun getChapterLessonsAt(classIndex: Int, chapterIndex: Int): List<String>{
        val strLessons = syllabusData.syllabus[classIndex].chapters[chapterIndex].lessons
        val lessons = arrayListOf<String>()
        strLessons.split(";").forEach {
            lessons.add(it)
        }
        return lessons

    }

    fun getClassNameAt(classIndex: Int): String {
        return syllabusData.syllabus[classIndex].className
    }

    fun getChapterNameAt(classIndex: Int, chapterIndex: Int): String {
        return syllabusData.syllabus[classIndex].chapters[chapterIndex].chapter
    }
}