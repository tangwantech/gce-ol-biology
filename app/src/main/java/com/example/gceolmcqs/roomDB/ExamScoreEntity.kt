package com.example.gceolmcqs.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_scores")
data class ExamScoreEntity(
    @PrimaryKey
    val examItemTitle: String,
    val highScore: Int,
    val lowScore: Int,
    val averageScore: Int,
    val recentScore: Int,
    val recentGrade: String
)
