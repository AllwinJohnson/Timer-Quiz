package com.example.timer_quiz.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer_quiz.presentation.ui.screens.TimerQuizScreen
import com.example.timer_quiz.presentation.ui.screens.TimerSchedulerScreen

sealed class Screen(val route: String) {
    object TimerScheduler : Screen("timer_scheduler")
    object FlagsChallenge : Screen("flags_challenge")
}

@Composable
fun FlagsNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TimerScheduler.route
    ) {
        composable(Screen.TimerScheduler.route) {
            TimerSchedulerScreen(
                onGameStart = {
                    navController.navigate(Screen.FlagsChallenge.route) {
                        popUpTo(Screen.TimerScheduler.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.FlagsChallenge.route) {
            TimerQuizScreen(
                onGameComplete = {
                    navController.navigate(Screen.TimerScheduler.route) {
                        popUpTo(Screen.FlagsChallenge.route) { inclusive = true }
                    }
                }
            )
        }
    }
}