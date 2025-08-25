package com.example.timer_quiz.di


import com.example.timer_quiz.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {

    // Individual Use Cases
    single { GetQuestionsUseCase(get()) }
    single { SubmitAnswerUseCase(get()) }
    single { GetGameSessionUseCase(get()) }
    single { SaveGameSessionUseCase(get()) }
    single { ResetGameUseCase(get()) }
    single { CalculateScoreUseCase() }
    single { UpdateGameStateUseCase(get()) }
    single { NextQuestionUseCase(get()) }
    single { ScheduleChallengeUseCase(get()) }
    single { StartCountdownUseCase() }
    single { GetTimerStateUseCase() }

    // Composite Use Cases
    single {
        GameUseCases(
            getQuestions = get(),
            submitAnswer = get(),
            getGameSession = get(),
            saveGameSession = get(),
            resetGame = get(),
            calculateScore = get(),
            updateGameState = get(),
            nextQuestion = get()
        )
    }

    single {
        TimerUseCases(
            scheduleChallenge = get(),
            startCountdown = get(),
            getTimerState = get()
        )
    }
}