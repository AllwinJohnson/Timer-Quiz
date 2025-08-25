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
    private var gameCreationInProgress = false

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val existingSession = gameUseCases.getGameSession.getCurrentSession()
            if (existingSession != null && existingSession.gameState == GameState.IN_PROGRESS) {
                // Delay navigation to prevent flickering
                delay(100)
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
        if (currentState.isTimeValid && !gameCreationInProgress) {
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
            while (_uiState.value.isScheduled && !_uiState.value.gameStarted && !gameCreationInProgress) {
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
            var remainingTime = 20
            while (remainingTime > 0) {
                _timerState.value = _timerState.value.copy(
                    remainingTime = remainingTime,
                    isRunning = true
                )
                delay(1000L)
                remainingTime--
            }

            _timerState.value = _timerState.value.copy(
                remainingTime = 0,
                isRunning = false
            )

            startGame()
        }
    }

    private fun startGame() {
        if (gameCreationInProgress) return

        gameCreationInProgress = true
        viewModelScope.launch {
            try {
                // First clear any existing game
                gameUseCases.resetGame()
                delay(100) // Small delay to ensure cleanup

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

                    // Small delay before navigation to prevent flickering
                    delay(200)

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
            } finally {
                gameCreationInProgress = false
            }
        }
    }

    fun resetChallenge() {
        viewModelScope.launch {
            countdownJob?.cancel()
            monitoringJob?.cancel()
            gameCreationInProgress = false

            gameUseCases.resetGame()
            delay(100) // Ensure cleanup completes

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