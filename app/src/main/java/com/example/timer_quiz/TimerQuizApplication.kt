package com.example.timer_quiz


import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.example.timer_quiz.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TimerQuizApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TimerQuizApplication)
            modules(allModules)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}

/*
project structure

app/
├── src/main/java/com/example/timer_quiz/
│   ├── di/
│   │   ├── AppModule.kt
│   │   ├── DataModule.kt
│   │   ├── DomainModule.kt
│   │   └── PresentationModule.kt
│   ├── data/
│   │   ├── local/
│   │   │   ├── entities/
│   │   │   ├── dao/
│   │   │   └── database/
│   │   ├── remote/
│   │   │   ├── api/
│   │   │   └── dto/
│   │   ├── repository/
│   │   └── mapper/
│   ├── domain/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── usecase/
│   │   └── util/
│   ├── presentation/
│   │   ├── ui/
│   │   │   ├── screens/
│   │   │   ├── components/
│   │   │   ├── navigation/
│   │   │   └── theme/
│   │   └── viewmodel/
│   ├── service/
│   └── util/

*/
