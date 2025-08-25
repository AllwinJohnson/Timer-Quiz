package com.example.timer_quiz.di


import com.example.timer_quiz.data.local.preferences.PreferencesManager
import com.example.timer_quiz.data.repository.FlagsChallengeRepositoryImpl
import com.example.timer_quiz.data.repository.GameSessionRepositoryImpl
import com.example.timer_quiz.domain.repository.FlagsChallengeRepository
import com.example.timer_quiz.domain.repository.GameSessionRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val dataModule = module {

    // Gson
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }

    // Preferences Manager
    single { PreferencesManager(get()) }

    // Repositories
    single<FlagsChallengeRepository> {
        FlagsChallengeRepositoryImpl(
            preferencesManager = get(),
            gson = get()
        )
    }

    single<GameSessionRepository> {
        GameSessionRepositoryImpl(
            gameSessionDao = get(),
            answerDao = get(),
            gson = get()
        )
    }
}