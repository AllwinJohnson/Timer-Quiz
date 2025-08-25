package com.example.timer_quiz.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.model.GameState
import com.example.timer_quiz.domain.model.TimerState
import com.example.timer_quiz.domain.usecase.GameUseCases
import com.example.timer_quiz.domain.usecase.TimerUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class TimerSchedulerViewModel(
    private val timerUseCases: TimerUseCases,
    private val gameUseCases: GameUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerSchedulerState())
    val uiState: StateFlow<TimerSchedulerState> = _uiState.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var countdownJob: Job? = null
    private var monitoringJob: Job? = null

    init {
        // Check if there's an existing game session
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val existingSession = gameUseCases.getGameSession.getCurrentSession()
            if (existingSession != null && existingSession.gameState != GameState.COMPLETED) {
                _uiState.value = _uiState.value.copy(gameStarted = true)
            }
        }
    }

    fun setTime(hours: Int, minutes: Int, seconds: Int) {
        val totalSeconds = (hours * 3600) + (minutes * 60) + seconds
        _uiState.value = _uiState.value.copy(
            selectedHours = hours,
            selectedMinutes = minutes,
            selectedSeconds = seconds,
            totalSelectedSeconds = totalSeconds,
            isTimeValid = totalSeconds > 0
        )
    }

    fun saveScheduledTime() {
        val currentState = _uiState.value
        if (currentState.isTimeValid) {
            viewModelScope.launch {
                try {
                    val scheduledTime = System.currentTimeMillis() + (currentState.totalSelectedSeconds * 1000L)
                    timerUseCases.scheduleChallenge(scheduledTime).getOrThrow()

                    _uiState.value = currentState.copy(
                        isScheduled = true,
                        scheduledTime = scheduledTime,
                        error = null
                    )

                    startCountdownMonitoring()
                } catch (e: Exception) {
                    _uiState.value = currentState.copy(
                        error = e.message ?: "Failed to schedule challenge"
                    )
                }
            }
        }
    }

    private fun startCountdownMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = viewModelScope.launch {
            while (_uiState.value.isScheduled && !_uiState.value.gameStarted) {
                val currentTime = System.currentTimeMillis()
                val scheduledTime = _uiState.value.scheduledTime

                if (scheduledTime != null) {
                    val remainingMs = scheduledTime - currentTime

                    when {
                        remainingMs <= 20000 && remainingMs > 0 && !_uiState.value.showCountdown -> {
                            startFinalCountdown()
                            break
                        }
                        remainingMs <= 0 -> {
                            startGame()
                            break
                        }
                        else -> {
                            val remainingSeconds = (remainingMs / 1000).toInt()
                            _uiState.value = _uiState.value.copy(
                                remainingTimeToStart = remainingSeconds
                            )
                        }
                    }
                }
                delay(1000)
            }
        }
    }

    private fun startFinalCountdown() {
        _uiState.value = _uiState.value.copy(showCountdown = true)

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            timerUseCases.startCountdown(10).collect { timerState ->
                _timerState.value = timerState

                if (!timerState.isRunning && timerState.remainingTime == 0) {
                    startGame()
                }
            }
        }
    }

    private fun startGame() {
        viewModelScope.launch {
            try {
                val questionsResult = gameUseCases.getQuestions()

                if (questionsResult.isSuccess) {
                    val questions = questionsResult.getOrNull() ?: emptyList()

                    val gameSession = GameSession(
                        id = UUID.randomUUID().toString(),
                        questions = questions,
                        gameState = GameState.IN_PROGRESS,
                        actualStartTime = System.currentTimeMillis()
                    )

                    gameUseCases.saveGameSession(gameSession)

                    _uiState.value = _uiState.value.copy(
                        isScheduled = false,
                        showCountdown = false,
                        gameStarted = true,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to load questions: ${questionsResult.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error starting game: ${e.message}"
                )
            }
        }
    }

    fun resetChallenge() {
        viewModelScope.launch {
            countdownJob?.cancel()
            monitoringJob?.cancel()

            gameUseCases.resetGame()

            _uiState.value = TimerSchedulerState()
            _timerState.value = TimerState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
        monitoringJob?.cancel()
    }
}

data class TimerSchedulerState(
    val selectedHours: Int = 0,
    val selectedMinutes: Int = 0,
    val selectedSeconds: Int = 0,
    val totalSelectedSeconds: Int = 0,
    val isTimeValid: Boolean = false,
    val isScheduled: Boolean = false,
    val scheduledTime: Long? = null,
    val remainingTimeToStart: Int = 0,
    val showCountdown: Boolean = false,
    val gameStarted: Boolean = false,
    val error: String? = null
)