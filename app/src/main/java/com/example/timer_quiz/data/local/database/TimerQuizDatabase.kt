package com.example.timer_quiz.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.timer_quiz.data.local.dao.AnswerDao
import com.example.timer_quiz.data.local.dao.GameSessionDao
import com.example.timer_quiz.data.local.entities.AnswerEntity
import com.example.timer_quiz.data.local.entities.GameSessionEntity

@Database(
    entities = [GameSessionEntity::class, AnswerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TimerQuizDatabase : RoomDatabase() {
    abstract fun gameSessionDao(): GameSessionDao
    abstract fun answerDao(): AnswerDao
}