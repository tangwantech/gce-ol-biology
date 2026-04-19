package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.NotesData

class NotesDataRepository {
    companion object{
        private var notes = ArrayList<NotesData>()
        fun initNotesData(notes: List<NotesData>){
            this.notes.clear()
            this.notes.addAll(notes)
        }
        fun getChapterNames(): List<String>{
            if (notes.isNotEmpty()){
                return notes.map { it.chapterName }
            }
            return emptyList()
        }

        fun getFilePath(chapterIndex: Int): String{
            val fileName = notes[chapterIndex].fileName
            val filePath = "file:///android_asset/notes/html/$fileName"
            return filePath
        }

        fun getChapterExerciseNumbers(chapterIndex: Int): List<String>{
            return notes[chapterIndex].exercises
        }

        fun isNotesInitialised(): Boolean{
            return notes.isNotEmpty()
        }
    }


}