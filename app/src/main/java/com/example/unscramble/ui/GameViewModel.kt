package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    // Número máximo de palabras y de intentos permitidos
    private val MAX_NO_OF_WORDS = 5  // Puedes cambiar este valor para ajustar el máximo de palabras
    private val MAX_NO_OF_ATTEMPTS = 3  // Puedes cambiar este valor para ajustar los intentos

    // Estado del juego
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    private var usedWords: MutableSet<String> = mutableSetOf()
    private var attempts = 0  // Contador de intentos
    private lateinit var currentWord: String

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        attempts = 0  // Reiniciar intentos
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            attempts++
            if (attempts >= MAX_NO_OF_ATTEMPTS) {
                _uiState.update { currentState ->
                    currentState.copy(isGameOver = true, isGuessedWordWrong = true)
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(isGuessedWordWrong = true)
                }
            }
        }
        updateUserGuess("")
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS || _uiState.value.isGameOver) {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = false, score = updatedScore, isGameOver = true)
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }
}
