package com.example.timer_quiz.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timer_quiz.domain.model.Answer
import com.example.timer_quiz.domain.model.GameSession
import com.example.timer_quiz.domain.model.GameState
import com.example.timer_quiz.domain.model.Question
import com.example.timer_quiz.domain.model.TimerState
import com.example.timer_quiz.domain.usecase.GameUseCases
import com.example.timer_quiz.domain.usecase.TimerUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerQuizViewModel(
    private val gameUseCases: GameUseCases,
    private val timerUseCases: TimerUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerQuizState())
    val uiState: StateFlow<TimerQuizState> = _uiState.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var questionTimerJob: Job? = null
    private var gameOverTimerJob: Job? = null
    private var questionStartTime: Long = 0
    private var isGameLoaded = false
    private var navigationInProgress = false

    init {
        loadGameSession()
    }

    private fun loadGameSession() {
        viewModelScope.launch {
            delay(150)

            gameUseCases.getGameSession.observeGameSession().collect { gameSession ->
                if (gameSession != null && !navigationInProgress) {
                    val newCurrentQuestion = gameSession.getCurrentQuestion()

                    _uiState.value = _uiState.value.copy(
                        gameSession = gameSession,
                        currentQuestion = newCurrentQuestion
                    )

                    when (gameSession.gameState) {
                        GameState.IN_PROGRESS -> {
                            if (!isGameLoaded && newCurrentQuestion != null) {
                                isGameLoaded = true
                                delay(100)
                                if (!_uiState.value.showingResult) {
                                    startQuestionTimer()
                                }
                            }
                        }
                        GameState.COMPLETED -> {
                            if (!_uiState.value.showGameOver) {
                                handleGameCompletion(gameSession)
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun startQuestionTimer() {
        questionStartTime = System.currentTimeMillis()
        questionTimerJob?.cancel()

        questionTimerJob = viewModelScope.launch {
            var remainingTime = 30
            while (remainingTime > 0 && !navigationInProgress) {
                _timerState.value = _timerState.value.copy(
                    remainingTime = remainingTime,
                    isRunning = true
                )
                delay(1000L)
                remainingTime--
            }

            if (!navigationInProgress) {
                _timerState.value = _timerState.value.copy(
                    remainingTime = 0,
                    isRunning = false
                )

                submitAnswer(null)
            }
        }
    }

    fun selectAnswer(countryId: Int) {
        val currentState = _uiState.value
        if (currentState.selectedAnswer == null && !currentState.showingResult && !navigationInProgress) {
            _uiState.value = currentState.copy(selectedAnswer = countryId)

            viewModelScope.launch {
                delay(300)
                if (!navigationInProgress) {
                    submitAnswer(countryId)
                }
            }
        }
    }

    private fun submitAnswer(selectedCountryId: Int?) {
        questionTimerJob?.cancel()

        viewModelScope.launch {
            val currentState = _uiState.value
            val gameSession = currentState.gameSession
            val currentQuestion = currentState.currentQuestion

            if (gameSession != null && currentQuestion != null && !navigationInProgress) {
                val timeSpent = ((System.currentTimeMillis() - questionStartTime) / 1000).toInt()
                val isCorrect = selectedCountryId == currentQuestion.answerId

                val answer = Answer(
                    questionIndex = gameSession.currentQuestionIndex,
                    selectedCountryId = selectedCountryId,
                    correctCountryId = currentQuestion.answerId,
                    isCorrect = isCorrect,
                    timeSpent = timeSpent
                )

                gameUseCases.submitAnswer(gameSession.currentQuestionIndex, answer)

                _uiState.value = _uiState.value.copy(
                    selectedAnswer = selectedCountryId,
                    showingResult = true,
                    isAnswerCorrect = isCorrect
                )

                delay(3000)

                if (gameSession.currentQuestionIndex + 1 >= gameSession.totalQuestions) {
                    completeGame()
                } else {
                    moveToNextQuestion()
                }
            }
        }
    }

    private fun moveToNextQuestion() {
        viewModelScope.launch {
            if (!navigationInProgress) {
                gameUseCases.nextQuestion()

                _uiState.value = _uiState.value.copy(
                    selectedAnswer = null,
                    showingResult = false,
                    isAnswerCorrect = false
                )

                delay(100)
                startQuestionTimer()
            }
        }
    }

    private fun completeGame() {
        viewModelScope.launch {
            gameUseCases.updateGameState(GameState.COMPLETED)

            val finalSession = gameUseCases.getGameSession.getCurrentSession()
            val score = finalSession?.getCorrectAnswersCount() ?: 0

            // Just trigger navigation to game over screen with score
            _uiState.value = _uiState.value.copy(
                showGameOver = true,
                finalScore = score
            )
        }
    }

    private fun handleGameCompletion(gameSession: GameSession) {
        val score = gameSession.getCorrectAnswersCount()

        _uiState.value = _uiState.value.copy(
            showGameOver = true,
            finalScore = score,
            showingResult = false,
            showingScore = false
        )

        startGameOverSequence()
    }

    private fun startGameOverSequence() {
        gameOverTimerJob?.cancel()
        gameOverTimerJob = viewModelScope.launch {
            // Phase 1: Show "GAME OVER" with 10-second countdown
            var remainingTime = 10
            while (remainingTime > 0 && !navigationInProgress) {
                _timerState.value = _timerState.value.copy(
                    remainingTime = remainingTime,
                    isRunning = true
                )
                delay(1000L)
                remainingTime--
            }

            if (!navigationInProgress) {
                _timerState.value = _timerState.value.copy(
                    remainingTime = 0,
                    isRunning = false
                )

                // Phase 2: Show score page
                _uiState.value = _uiState.value.copy(showingScore = true)

                remainingTime = 10
                while (remainingTime > 0 && !navigationInProgress) {
                    _timerState.value = _timerState.value.copy(
                        remainingTime = remainingTime,
                        isRunning = true
                    )
                    delay(1000L)
                    remainingTime--
                }

                if (!navigationInProgress) {
                    _timerState.value = _timerState.value.copy(
                        remainingTime = 0,
                        isRunning = false
                    )

                    // Phase 3: Navigate back to scheduler
                    navigationInProgress = true
                    delay(200) // Small delay before navigation
                    resetGame()
                }
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            navigationInProgress = true
            questionTimerJob?.cancel()
            gameOverTimerJob?.cancel()

            gameUseCases.resetGame()
            delay(100)

            _uiState.value = TimerQuizState()
            _timerState.value = TimerState()
            isGameLoaded = false
            navigationInProgress = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        questionTimerJob?.cancel()
        gameOverTimerJob?.cancel()
    }
}

data class TimerQuizState(
    val gameSession: GameSession? = null,
    val currentQuestion: Question? = null,
    val selectedAnswer: Int? = null,
    val showingResult: Boolean = false,
    val showGameOver: Boolean = false,
    val showingScore: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val finalScore: Int = 0,
    val error: String? = null
)