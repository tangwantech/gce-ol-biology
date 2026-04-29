package com.example.gceolmcqs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam_scores")
data class ExamScoreEntity(
    @PrimaryKey
    val examItemTitle: String,
    val attempts: Int = 0,
    val highScore: Int = 0,
    val highGrade: String = "U",
    val lowScore: Int = 0,
    val lowGrade: String = "U",
    val averageScore: Int = 0,
    val recentScore: Int = 0,
    val recentGrade: String = "U"
)
