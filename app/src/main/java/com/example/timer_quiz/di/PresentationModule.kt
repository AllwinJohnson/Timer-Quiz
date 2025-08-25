package com.example.timer_quiz.di


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
}
