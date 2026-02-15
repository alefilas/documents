package ru.alefilas.documentsandroid.data.repository

import ru.alefilas.documentsandroid.data.model.InputDocumentDto
import ru.alefilas.documentsandroid.data.model.OutputDocumentDto
import ru.alefilas.documentsandroid.data.network.DocumentsApi

class DocumentsRepository(private val api: DocumentsApi) {

    suspend fun getFirstPage(): List<OutputDocumentDto> {
        return api.getDocuments(page = 0).content
    }

    suspend fun createDocument(body: InputDocumentDto): OutputDocumentDto {
        return api.createDocument(body)
    }

    suspend fun getTypes(): List<String> {
        return api.getTypes().mapNotNull { it["type"] as? String }.ifEmpty { listOf("ARTICLE") }
    }
}
