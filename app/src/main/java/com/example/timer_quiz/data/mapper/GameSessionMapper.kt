package com.example.timer_quiz.data.mapper


import com.example.timer_quiz.data.local.entities.AnswerEntity
import com.example.timer_quiz.data.local.entities.GameSessionEntity
import com.example.timer_quiz.domain.model.Answer
import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.model.GameState
import com.example.timer_quiz.domain.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun GameSession.toEntity(gson: Gson): GameSessionEntity {
    return GameSessionEntity(
        id = id,
        questionsJson = gson.toJson(questions),
        currentQuestionIndex = currentQuestionIndex,
        totalQuestions = totalQuestions,
        score = score,
        gameState = gameState.name,
        scheduledStartTime = scheduledStartTime,
        actualStartTime = actualStartTime,
        endTime = endTime,
        timePerQuestion = timePerQuestion,
        breakBetweenQuestions = breakBetweenQuestions,
        updatedAt = System.currentTimeMillis()
    )
}

fun GameSessionEntity.toDomain(gson: Gson, answers: List<AnswerEntity>): GameSession {
    val questionsType = object : TypeToken<List<Question>>() {}.type
    val questions: List<Question> = try {
        gson.fromJson(questionsJson, questionsType)
    } catch (e: Exception) {
        emptyList()
    }

    val answersMap = answers.associate {
        it.questionIndex to it.toDomain()
    }

    return GameSession(
        id = id,
        questions = questions,
        currentQuestionIndex = currentQuestionIndex,
        totalQuestions = totalQuestions,
        score = score,
        gameState = GameState.valueOf(gameState),
        scheduledStartTime = scheduledStartTime,
        actualStartTime = actualStartTime,
        endTime = endTime,
        answers = answersMap,
        timePerQuestion = timePerQuestion,
        breakBetweenQuestions = breakBetweenQuestions
    )
}

fun Answer.toEntity(gameSessionId: String): AnswerEntity {
    return AnswerEntity(
        gameSessionId = gameSessionId,
        questionIndex = questionIndex,
        selectedCountryId = selectedCountryId,
        correctCountryId = correctCountryId,
        isCorrect = isCorrect,
        timeSpent = timeSpent,
        timestamp = timestamp
    )
}

fun AnswerEntity.toDomain(): Answer {
    return Answer(
        questionIndex = questionIndex,
        selectedCountryId = selectedCountryId,
        correctCountryId = correctCountryId,
        isCorrect = isCorrect,
        timeSpent = timeSpent,
        timestamp = timestamp
    )
}