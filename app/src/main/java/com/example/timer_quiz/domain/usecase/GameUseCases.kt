package com.example.timer_quiz.domain.usecase

data class GameUseCases(
    val getQuestions: GetQuestionsUseCase,
    val submitAnswer: SubmitAnswerUseCase,
    val getGameSession: GetGameSessionUseCase,
    val saveGameSession: SaveGameSessionUseCase,
    val resetGame: ResetGameUseCase,
    val calculateScore: CalculateScoreUseCase,
    val updateGameState: UpdateGameStateUseCase,
    val nextQuestion: NextQuestionUseCase
)