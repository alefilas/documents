package ru.alefilas.documentsandroid.data.model

data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val totalPages: Int = 0,
    val totalElements: Int = 0
)

data class OutputDocumentDto(
    val id: Long,
    val creationDate: String? = null,
    val directoryId: Long? = null,
    val currentVersion: OutputDocumentVersionDto? = null,
    val documentPriority: String? = null,
    val username: String? = null,
    val type: String? = null,
    val status: String? = null
)

data class OutputDocumentVersionDto(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val files: List<String> = emptyList()
)

data class InputDocumentDto(
    val directoryId: Long,
    val currentVersion: InputDocumentVersionDto,
    val documentPriority: String,
    val type: String
)

data class InputDocumentVersionDto(
    val title: String,
    val description: String,
    val files: List<String>
)
