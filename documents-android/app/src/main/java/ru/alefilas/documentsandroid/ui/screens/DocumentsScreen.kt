package ru.alefilas.documentsandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.alefilas.documentsandroid.ui.MainUiState

@Composable
fun DocumentsScreen(
    state: MainUiState,
    onRefresh: () -> Unit,
    onCreateDocument: (Long, String, String, String, String) -> Unit
) {
    var directoryId by remember { mutableLongStateOf(1L) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember(state.availableTypes) { mutableStateOf(state.availableTypes.firstOrNull() ?: "ARTICLE") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onRefresh) {
                Text("Обновить")
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }

        state.error?.let { Text("Ошибка: $it") }

        Text("Создать документ")
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = directoryId.toString(),
            onValueChange = { directoryId = it.toLongOrNull() ?: directoryId },
            label = { Text("ID директории") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = type,
            onValueChange = { type = it },
            label = { Text("Тип документа") }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onCreateDocument(directoryId, title, description, type, "LOW")
                title = ""
                description = ""
            }
        ) {
            Text("Создать")
        }

        HorizontalDivider()
        Text("Список документов")

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.documents) { document ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(document.currentVersion?.title ?: "Без названия")
                        Text(document.currentVersion?.description ?: "")
                        Text("Тип: ${document.type ?: "unknown"}")
                        Text("Статус: ${document.status ?: "unknown"}")
                    }
                }
            }
        }
    }
}
