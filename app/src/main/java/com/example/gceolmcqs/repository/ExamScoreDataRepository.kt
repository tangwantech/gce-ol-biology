package com.example.gceolmcqs.repository

import android.content.Context
import com.example.gceolmcqs.datamodels.ExamScoreEntity
import com.example.gceolmcqs.roomDB.ExamScoreDao
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExamScoreDataRepository {
    companion object {
        private var examScoreDao: ExamScoreDao? = null

        fun init(context: Context) {
            examScoreDao = GceOLMcqDatabase.getDatabase(context).examScoreDao()
        }

        fun insertScore(examScore: ExamScoreEntity, listener: OnScoreActionCompleteListener? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                examScoreDao?.insertScore(examScore)
                withContext(Dispatchers.Main) {
                    listener?.onActionComplete()
                }
            }
        }

        fun updateScore(examScore: ExamScoreEntity, listener: OnScoreActionCompleteListener? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                examScoreDao?.updateScore(examScore)
                withContext(Dispatchers.Main) {
                    listener?.onActionComplete()
                }
            }
        }

        fun getScoreByTitle(title: String, listener: OnScoreLoadedListener) {
            CoroutineScope(Dispatchers.IO).launch {
                val score = examScoreDao?.getScoreByTitle(title)
                withContext(Dispatchers.Main) {
                    listener.onScoreLoaded(score)
                }
            }
        }

        fun getAllScores(listener: OnAllScoresLoadedListener) {
            CoroutineScope(Dispatchers.IO).launch {
                val scores = examScoreDao?.getAllScores() ?: emptyList()
                withContext(Dispatchers.Main) {
                    listener.onAllScoresLoaded(scores)
                }
            }
        }

        fun deleteAllScores(listener: OnScoreActionCompleteListener? = null) {
            CoroutineScope(Dispatchers.IO).launch {
                examScoreDao?.deleteAll()
                withContext(Dispatchers.Main) {
                    listener?.onActionComplete()
                }
            }
        }
    }

    interface OnScoreActionCompleteListener {
        fun onActionComplete()
    }

    interface OnScoreLoadedListener {
        fun onScoreLoaded(examScore: ExamScoreEntity?)
    }

    interface OnAllScoresLoadedListener {
        fun onAllScoresLoaded(scores: List<ExamScoreEntity>)
    }
}
