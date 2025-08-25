package com.example.timer_quiz.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.timer_quiz.data.local.database.TimerQuizDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flags_challenge_prefs")

val databaseModule = module {

    // DataStore
    single<DataStore<Preferences>> { androidContext().dataStore }

    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            TimerQuizDatabase::class.java,
            "timer_quiz_database"
        )
//            .fallbackToDestructiveMigration()
            .build()
    }

    // DAOs
    single { get<TimerQuizDatabase>().gameSessionDao() }
    single { get<TimerQuizDatabase>().answerDao() }
}