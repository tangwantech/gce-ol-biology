package com.example.gceolmcqs

import android.util.Log
import com.example.gceolmcqs.datamodels.ExamScoreEntity
import com.example.gceolmcqs.repository.ExamScoreDataRepository
import com.example.gceolmcqs.repository.RestRepository
import com.google.gson.Gson

class UserScoresStatsTracker() {
//    fun getUserStatsFromServer(id: String, listener: OnUserStatsLoadedListener){})
    fun updateUserScoreStatsInServer(id: String){
        ExamScoreDataRepository.getAllScores(object : ExamScoreDataRepository.OnAllScoresLoadedListener{
            override fun onAllScoresLoaded(scores: List<ExamScoreEntity>) {
                if (scores.isNotEmpty()){
                    val scoresJson = Gson().toJson(scores)
                    Log.i("UserScoresStatsTracker", scoresJson)
                    val params = hashMapOf("userId" to id, "scores" to scoresJson)

                    RestRepository().query(RestRepository.UPDATE_SCORES_STATS, params = params, object : RestRepository.OnQueryListener{
                        override fun onSuccess(result: String) {
//                            Log.i("UserScoresStatsTracker", "Scores stats updated successfully")
//                            println("Scores stats updated successfully")
                        }

                        override fun onError(error: String?) {

                        }
                    })
                }
            }



        })

    }
}