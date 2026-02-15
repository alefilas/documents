package ru.alefilas.documentsandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alefilas.documentsandroid.data.model.InputDocumentDto
import ru.alefilas.documentsandroid.data.model.InputDocumentVersionDto
import ru.alefilas.documentsandroid.data.model.OutputDocumentDto
import ru.alefilas.documentsandroid.data.network.ApiFactory
import ru.alefilas.documentsandroid.data.repository.DocumentsRepository

data class LoginState(
    val baseUrl: String = "http://10.0.2.2:8080/",
    val username: String = "",
    val password: String = ""
)

data class MainUiState(
    val isAuthorized: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val documents: List<OutputDocumentDto> = emptyList(),
    val availableTypes: List<String> = emptyList()
)

class MainViewModel : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var repository: DocumentsRepository? = null

    fun updateBaseUrl(value: String) {
        _loginState.value = _loginState.value.copy(baseUrl = value)
    }

    fun updateUsername(value: String) {
        _loginState.value = _loginState.value.copy(username = value)
    }

    fun updatePassword(value: String) {
        _loginState.value = _loginState.value.copy(password = value)
    }

    fun connect() {
        val login = _loginState.value
        repository = DocumentsRepository(
            ApiFactory.create(login.baseUrl, login.username, login.password)
        )
        loadDocuments()
    }

    fun loadDocuments() {
        val repo = repository ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching {
                val docs = repo.getFirstPage()
                val types = repo.getTypes()
                docs to types
            }.onSuccess { (documents, types) ->
                _uiState.value = _uiState.value.copy(
                    isAuthorized = true,
                    isLoading = false,
                    documents = documents,
                    availableTypes = types,
                    error = null
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message ?: "Не удалось подключиться"
                )
            }
        }
    }

    fun createDocument(
        directoryId: Long,
        title: String,
        description: String,
        type: String,
        priority: String
    ) {
        val repo = repository ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching {
                repo.createDocument(
                    InputDocumentDto(
                        directoryId = directoryId,
                        currentVersion = InputDocumentVersionDto(
                            title = title,
                            description = description,
                            files = emptyList()
                        ),
                        documentPriority = priority,
                        type = type
                    )
                )
            }.onSuccess {
                loadDocuments()
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message ?: "Не удалось создать документ"
                )
            }
        }
    }
}
