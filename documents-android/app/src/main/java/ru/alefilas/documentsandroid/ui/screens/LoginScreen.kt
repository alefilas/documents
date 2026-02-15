package ru.alefilas.documentsandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.alefilas.documentsandroid.ui.LoginState

@Composable
fun LoginScreen(
    state: LoginState,
    isLoading: Boolean,
    error: String?,
    onBaseUrlChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Документы: подключение к backend")

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.baseUrl,
            onValueChange = onBaseUrlChanged,
            label = { Text("Base URL") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.username,
            onValueChange = onUsernameChanged,
            label = { Text("Username") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            onValueChange = onPasswordChanged,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            onClick = onLogin
        ) {
            Text("Войти")
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (error != null) {
            Text("Ошибка: $error")
        }
    }
}
