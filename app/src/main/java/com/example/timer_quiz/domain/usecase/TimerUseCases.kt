package com.example.timer_quiz.domain.usecase

data class TimerUseCases(
    val scheduleChallenge: ScheduleChallengeUseCase,
    val startCountdown: StartCountdownUseCase,
    val getTimerState: GetTimerStateUseCase
)