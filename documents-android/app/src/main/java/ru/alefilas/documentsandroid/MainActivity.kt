package ru.alefilas.documentsandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.alefilas.documentsandroid.ui.MainViewModel
import ru.alefilas.documentsandroid.ui.screens.DocumentsScreen
import ru.alefilas.documentsandroid.ui.screens.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()
            val loginState by viewModel.loginState.collectAsState()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (uiState.isAuthorized) {
                    DocumentsScreen(
                        state = uiState,
                        onRefresh = viewModel::loadDocuments,
                        onCreateDocument = viewModel::createDocument
                    )
                } else {
                    LoginScreen(
                        state = loginState,
                        isLoading = uiState.isLoading,
                        error = uiState.error,
                        onBaseUrlChanged = viewModel::updateBaseUrl,
                        onUsernameChanged = viewModel::updateUsername,
                        onPasswordChanged = viewModel::updatePassword,
                        onLogin = viewModel::connect
                    )
                }
            }
        }
    }
}
