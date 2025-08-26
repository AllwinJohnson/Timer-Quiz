# Timer Quiz - Flags Challenge App

A timer-based flags quiz application built with Android Jetpack Compose, following Clean Architecture principles with MVVM pattern and Koin dependency injection.

### App Preview

[Watch Full Demo Video](https://github.com/AllwinJohnson/Timer-Quiz/blob/master/screen/timer-quiz.mp4)

### Screenshots
<p align="center">
  <img src="https://github.com/AllwinJohnson/Timer-Quiz/blob/master/screen/img.png" width="250" alt="Timer Setup Screen"/>
  <img src="https://github.com/AllwinJohnson/Timer-Quiz/blob/master/screen/img_1.png" width="250" alt="Quiz Question"/>
  <img src="https://github.com/AllwinJohnson/Timer-Quiz/blob/master/screen/img_2.png" width="250" alt="Answer Results"/>
</p>
<p align="center">
  <img src="https://github.com/AllwinJohnson/Timer-Quiz/blob/master/screen/img_3.png" width="250" alt="Game Over Screen"/>
  <img src="https://github.com/AllwinJohnson/Timer-Quiz/blob/master/screen/img_4.png" width="250" alt="Score Display"/>
</p>

**Download Links:**
- [APK Download](https://github.com/AllwinJohnson/Timer-Quiz/blob/master/app/build/outputs/apk/debug/app-debug.apk) (Prefering to build an updated version from project)


### Core Functionality
- **Customizable Timer Setup**: Set challenge start time using hour, minute, and second inputs
- **20-Second Countdown**: Pre-game countdown before the challenge begins  
- **15 Flag Questions**: Multiple choice questions with country flags
- **30-Second Timer**: Each question has a 30-second time limit
- **Real-time Flag Loading**: Flags loaded from external API with SVG support
- **Answer Feedback**: Immediate visual feedback showing correct/wrong answers
- **Score Tracking**: Track correct answers out of 15 questions
- **Game Over Sequence**: 10-second game over screen followed by 10-second score display
- **Background State Management**: Timer continues accurately when app is backgrounded
- **Last Test Retrieval**: Resume incomplete tests when returning to the app after backgrounding

- Game sessions stored locally with Room database
- Answer history and scoring persistence
- Timer state preservation in DataStore
- **Last Test Retrieval**: Automatic resume of incomplete tests when app is reopened after killing or background
- Background-safe data operations
- State synchronization between timer and question progress

### Technical Features
- **Clean Architecture**: Domain, Data, and Presentation layers
- **MVVM Pattern**: ViewModels with StateFlow for reactive UI
- **Koin Dependency Injection**: Modular DI setup
- **Room Database**: Local storage for game sessions and answers
- **DataStore Preferences**: Timer and game state persistence
- **Jetpack Compose UI**: Modern declarative UI toolkit
- **Navigation Component**: Type-safe navigation between screens
- **Coroutines & Flow**: Asynchronous programming with reactive streams




