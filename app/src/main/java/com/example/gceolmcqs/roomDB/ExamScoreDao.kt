package com.example.gceolmcqs.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gceolmcqs.datamodels.ExamScoreEntity

@Dao
interface ExamScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertScore(examScore: ExamScoreEntity)

    @Query("SELECT * FROM exam_scores")
    fun getAllScores(): List<ExamScoreEntity>

    @Query("SELECT * FROM exam_scores WHERE examItemTitle = :title")
    fun getScoreByTitle(title: String): ExamScoreEntity?

    @Update
    fun updateScore(examScore: ExamScoreEntity)

    @Query("DELETE FROM exam_scores")
    fun deleteAll()
}
