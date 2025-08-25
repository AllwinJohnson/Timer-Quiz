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

class GameOverViewModel(
    private val gameUseCases: GameUseCases
) : ViewModel() {

    private val _remainingTime = MutableStateFlow(10)
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadScoreAndStartTimer()
    }

    private fun loadScoreAndStartTimer() {
        viewModelScope.launch {
            // Get score from current game session
            val currentSession = gameUseCases.getGameSession.getCurrentSession()
            val gameScore = currentSession?.getCorrectAnswersCount() ?: 0
            _score.value = gameScore

            startTimer()
        }
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
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}