package ru.alefilas.documentsandroid.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.alefilas.documentsandroid.data.model.InputDocumentDto
import ru.alefilas.documentsandroid.data.model.OutputDocumentDto
import ru.alefilas.documentsandroid.data.model.PageResponse

interface DocumentsApi {

    @GET("documents/all")
    suspend fun getDocuments(@Query("page") page: Int): PageResponse<OutputDocumentDto>

    @POST("documents")
    suspend fun createDocument(@Body body: InputDocumentDto): OutputDocumentDto

    @GET("documents/types")
    suspend fun getTypes(): List<Map<String, Any?>>
}
