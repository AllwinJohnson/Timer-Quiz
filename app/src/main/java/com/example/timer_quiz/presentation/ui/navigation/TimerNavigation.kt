package com.example.timer_quiz.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timer_quiz.presentation.ui.screens.GameOverScreen
import com.example.timer_quiz.presentation.ui.screens.ScoreScreen
import com.example.timer_quiz.presentation.ui.screens.TimerQuizScreen
import com.example.timer_quiz.presentation.ui.screens.TimerSchedulerScreen

sealed class Screen(val route: String) {
    object TimerScheduler : Screen("timer_scheduler")
    object FlagsChallenge : Screen("flags_challenge")
    object GameOver : Screen("game_over")
    object Score : Screen("score/{score}") {
        fun createRoute(score: Int) = "score/$score"
    }
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
                onGameComplete = { score ->
                    navController.navigate(Screen.GameOver.route) {
                        popUpTo(Screen.FlagsChallenge.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.GameOver.route) {
            GameOverScreen(
                onTimerComplete = { score ->
                    navController.navigate(Screen.Score.createRoute(score)) {
                        popUpTo(Screen.GameOver.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Score.route) { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
            ScoreScreen(
                score = score,
                onTimerComplete = {
                    navController.navigate(Screen.TimerScheduler.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}