package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.DictionaryData

class DictionaryRepository {
    companion object{
        private lateinit var dictionaryData: List<DictionaryData>
        private lateinit var keyWords: List<String>

        fun updateDictionaryData(temp: List<DictionaryData>){
            dictionaryData = temp
            updateKeyWords()
        }

        private fun updateKeyWords(){

            keyWords = dictionaryData.map { it.keyword}

        }
        fun getKeys(): List<String>{
            return keyWords
        }

        fun getDefinition(keyWord: String): String{

            if (keyWord.lowercase() in keyWords.map { it.lowercase() } ){
                val match = dictionaryData.find { it.keyword.lowercase() == keyWord.lowercase()}
                return match?.definition!!

            }else{
                return "$keyWord not in database"
            }
        }

        fun getAllMatches(keyWord: String): List<String>{
            return keyWords.filter { it.startsWith(keyWord, ignoreCase = true) }

        }
    }
}