package com.example.gceolmcqs.repository

import com.example.gceolmcqs.datamodels.DictionaryData

class DictionaryRepository {
    companion object{
        private lateinit var dictionaryData: List<DictionaryData>
        private lateinit var keyWords: List<String>

        fun isInitialized(): Boolean {
            return ::keyWords.isInitialized
        }

        fun updateDictionaryData(temp: List<DictionaryData>){
            dictionaryData = temp
            updateKeyWords()
        }

        private fun updateKeyWords(){
            keyWords = dictionaryData.map { it.keyword}
        }

        fun getKeys(): List<String>{
            return if (isInitialized()) keyWords else emptyList()
        }

        fun getDefinition(keyWord: String): String{
            if (!isInitialized()) {
                return "Dictionary not initialized"
            }

            if (keyWord.lowercase() in keyWords.map { it.lowercase() } ){
                val match = dictionaryData.find { it.keyword.lowercase() == keyWord.lowercase()}
                return match?.definition!!

            }else{
                return "$keyWord not in database"
            }
        }

        fun getAllMatches(keyWord: String): List<String>{
            if (!isInitialized()) return emptyList()
            return keyWords.filter { it.startsWith(keyWord, ignoreCase = true) }
        }
    }
}