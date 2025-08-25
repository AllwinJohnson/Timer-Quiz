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

    private val _uiState = MutableStateFlow(FlagsChallengeState())
    val uiState: StateFlow<FlagsChallengeState> = _uiState.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var questionTimerJob: Job? = null
    private var breakTimerJob: Job? = null
    private var questionStartTime: Long = 0

    init {
        loadGameSession()
    }

    private fun loadGameSession() {
        viewModelScope.launch {
            gameUseCases.getGameSession.observeGameSession().collect { gameSession ->
                if (gameSession != null) {
                    _uiState.value = _uiState.value.copy(
                        gameSession = gameSession,
                        currentQuestion = gameSession.getCurrentQuestion(),
                        questionNumber = gameSession.currentQuestionIndex + 1
                    )

                    when (gameSession.gameState) {
                        GameState.IN_PROGRESS -> {
                            if (!_uiState.value.showingResult && !_uiState.value.showingBreak) {
                                startQuestionTimer()
                            }
                        }
                        GameState.COMPLETED -> {
                            _uiState.value = _uiState.value.copy(
                                showGameOver = true,
                                finalScore = gameSession.getCorrectAnswersCount()
                            )
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
            timerUseCases.getTimerState.startQuestionTimer(30).collect { timerState ->
                _timerState.value = timerState

                if (!timerState.isRunning && timerState.remainingTime == 0) {
                    // Time up - submit no answer
                    submitAnswer(null)
                }
            }
        }
    }

    private fun startBreakTimer() {
        _uiState.value = _uiState.value.copy(showingBreak = true)
        breakTimerJob?.cancel()

        breakTimerJob = viewModelScope.launch {
            timerUseCases.getTimerState.startBreakTimer(10).collect { timerState ->
                _timerState.value = timerState

                if (!timerState.isRunning && timerState.remainingTime == 0) {
                    moveToNextQuestion()
                }
            }
        }
    }

    fun selectAnswer(countryId: Int) {
        val currentState = _uiState.value
        if (currentState.selectedAnswer == null && !currentState.showingResult) {
            _uiState.value = currentState.copy(selectedAnswer = countryId)

            viewModelScope.launch {
                delay(300) // Brief delay to show selection
                submitAnswer(countryId)
            }
        }
    }

    private fun submitAnswer(selectedCountryId: Int?) {
        questionTimerJob?.cancel()

        viewModelScope.launch {
            val currentState = _uiState.value
            val gameSession = currentState.gameSession
            val currentQuestion = currentState.currentQuestion

            if (gameSession != null && currentQuestion != null) {
                val timeSpent = ((System.currentTimeMillis() - questionStartTime) / 1000).toInt()
                val isCorrect = selectedCountryId == currentQuestion.answerId

                val answer = Answer(
                    questionIndex = gameSession.currentQuestionIndex,
                    selectedCountryId = selectedCountryId,
                    correctCountryId = currentQuestion.answerId,
                    isCorrect = isCorrect,
                    timeSpent = timeSpent
                )

                // Submit answer
                gameUseCases.submitAnswer(gameSession.currentQuestionIndex, answer)

                // Update UI to show result
                _uiState.value = _uiState.value.copy(
                    selectedAnswer = selectedCountryId,
                    showingResult = true,
                    isAnswerCorrect = isCorrect
                )

                // Show result for 3 seconds
                delay(3000)

                // Check if game is complete
                if (gameSession.currentQuestionIndex + 1 >= gameSession.totalQuestions) {
                    completeGame()
                } else {
                    startBreakTimer()
                }
            }
        }
    }

    private fun moveToNextQuestion() {
        viewModelScope.launch {
            gameUseCases.nextQuestion()

            _uiState.value = _uiState.value.copy(
                selectedAnswer = null,
                showingResult = false,
                showingBreak = false,
                isAnswerCorrect = false
            )

            startQuestionTimer()
        }
    }

    private fun completeGame() {
        viewModelScope.launch {
            gameUseCases.updateGameState(GameState.COMPLETED)

            val finalSession = gameUseCases.getGameSession.getCurrentSession()
            val score = finalSession?.getCorrectAnswersCount() ?: 0

            _uiState.value = _uiState.value.copy(
                showGameOver = true,
                finalScore = score,
                showingResult = false,
                showingBreak = false
            )
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            questionTimerJob?.cancel()
            breakTimerJob?.cancel()

            gameUseCases.resetGame()

            _uiState.value = FlagsChallengeState()
            _timerState.value = TimerState()
        }
    }

    // Handle app lifecycle
    fun onAppPause() {
        // Timer continues in background - no need to cancel
        val currentSession = _uiState.value.gameSession
        if (currentSession != null && currentSession.gameState == GameState.IN_PROGRESS) {
            viewModelScope.launch {
                gameUseCases.updateGameState(GameState.PAUSED)
            }
        }
    }

    fun onAppResume() {
        val currentSession = _uiState.value.gameSession
        if (currentSession != null && currentSession.gameState == GameState.PAUSED) {
            viewModelScope.launch {
                gameUseCases.updateGameState(GameState.IN_PROGRESS)
                // Restart timer if needed
                if (!_uiState.value.showingResult && !_uiState.value.showingBreak) {
                    startQuestionTimer()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        questionTimerJob?.cancel()
        breakTimerJob?.cancel()
    }
}

data class FlagsChallengeState(
    val gameSession: GameSession? = null,
    val currentQuestion: Question? = null,
    val questionNumber: Int = 1,
    val selectedAnswer: Int? = null,
    val showingResult: Boolean = false,
    val showingBreak: Boolean = false,
    val showGameOver: Boolean = false,
    val isAnswerCorrect: Boolean = false,
    val finalScore: Int = 0,
    val error: String? = null
)