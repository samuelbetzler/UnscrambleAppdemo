package com.example.unscramble.ui

import androidx.compose.runtime.Composable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton


@Composable
fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Game Over") },
        text = { Text(text = "Your final score is $score") },
        confirmButton = {
            Button(onClick = onPlayAgain) {
                Text("Play Again")
            }
        }
    )
}
