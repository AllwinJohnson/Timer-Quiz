package com.example.timer_quiz.data.local.dao


import androidx.room.*
import com.example.timer_quiz.data.local.entities.AnswerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {

    @Query("SELECT * FROM answers WHERE gameSessionId = :gameSessionId ORDER BY questionIndex")
    suspend fun getAnswersForSession(gameSessionId: String): List<AnswerEntity>

    @Query("SELECT * FROM answers WHERE gameSessionId = :gameSessionId ORDER BY questionIndex")
    fun observeAnswersForSession(gameSessionId: String): Flow<List<AnswerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswer(answer: AnswerEntity)

    @Query("DELETE FROM answers WHERE gameSessionId = :gameSessionId")
    suspend fun deleteAnswersForSession(gameSessionId: String)

    @Query("DELETE FROM answers")
    suspend fun deleteAllAnswers()
}