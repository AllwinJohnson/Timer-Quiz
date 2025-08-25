package com.example.timer_quiz.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer_quiz.domain.usecase.GameUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScoreViewModel(
    private val gameUseCases: GameUseCases
) : ViewModel() {

    private val _remainingTime = MutableStateFlow(10)
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    private var timerJob: Job? = null

    fun setScore(score: Int) {
        // Score is passed as parameter, just start timer
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var time = 10
            while (time > 0) {
                _remainingTime.value = time
                delay(1000L)
                time--
            }
            _remainingTime.value = 0

            // Clean up game data when score screen timer completes
            gameUseCases.resetGame()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

