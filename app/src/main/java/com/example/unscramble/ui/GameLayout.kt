package com.example.unscramble.ui
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun GameLayout(
    onUserGuessChanged: (String) -> Unit,
    wordCount: Int,
    userGuess: String,
    onKeyboardDone: () -> Unit,
    currentScrambledWord: String,
    isGuessWrong: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(text = "Scrambled Word: $currentScrambledWord")
        TextField(
            value = userGuess,
            onValueChange = onUserGuessChanged,
            label = { Text("Enter your guess") },
            isError = isGuessWrong,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onKeyboardDone() }),
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Word Count: $wordCount")
    }
}
