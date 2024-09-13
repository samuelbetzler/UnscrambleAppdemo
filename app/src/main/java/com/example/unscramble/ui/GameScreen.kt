package com.example.unscramble.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.R

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(mediumPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
            )
            // Score movido a la parte superior derecha
            GameStatus(score = gameUiState.score)
        }

        // Solución para el scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())  // Añadimos verticalScroll y rememberScrollState
                .padding(mediumPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Layout del juego
            GameLayout(
                onUserGuessChanged = { guess -> gameViewModel.updateUserGuess(guess) },  // Cambiado "it" por una variable explícita "guess"
                wordCount = gameUiState.currentWordCount,
                userGuess = gameViewModel.userGuess,
                onKeyboardDone = { gameViewModel.checkUserGuess() },
                currentScrambledWord = gameUiState.currentScrambledWord,
                isGuessWrong = gameUiState.isGuessedWordWrong,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(mediumPadding)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(mediumPadding),
                verticalArrangement = Arrangement.spacedBy(mediumPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { gameViewModel.checkUserGuess() }
                ) {
                    Text(
                        text = stringResource(R.string.submit),
                        fontSize = 16.sp
                    )
                }

                OutlinedButton(
                    onClick = { gameViewModel.skipWord() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.skip),
                        fontSize = 16.sp
                    )
                }
            }

            // Si el juego ha terminado, mostramos el diálogo final
            if (gameUiState.isGameOver) {
                FinalScoreDialog(
                    score = gameUiState.score,
                    onPlayAgain = { gameViewModel.resetGame() }
                )
            }
        }
    }
}

@Composable
fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    // Score en la parte superior derecha
    Text(
        text = stringResource(R.string.score, score),
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier.padding(8.dp)
    )
}
