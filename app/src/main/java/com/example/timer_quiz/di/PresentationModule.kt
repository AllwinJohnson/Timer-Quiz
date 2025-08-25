package com.example.timer_quiz.di


import com.example.timer_quiz.presentation.viewmodel.GameOverViewModel
import com.example.timer_quiz.presentation.viewmodel.ScoreViewModel
import com.example.timer_quiz.presentation.viewmodel.TimerQuizViewModel
import com.example.timer_quiz.presentation.viewmodel.TimerSchedulerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    // ViewModels
    viewModel {
        TimerSchedulerViewModel(
            timerUseCases = get(),
            gameUseCases = get()
        )
    }

    viewModel {
        TimerQuizViewModel(
            gameUseCases = get(),
            timerUseCases = get()
        )
    }

    viewModel {
        GameOverViewModel(
            gameUseCases = get()
        )
    }

    viewModel {
        ScoreViewModel(
            gameUseCases = get()
        )
    }
}
