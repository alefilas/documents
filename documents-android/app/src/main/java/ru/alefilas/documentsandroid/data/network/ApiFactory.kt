package ru.alefilas.documentsandroid.data.network

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {

    fun create(baseUrl: String, username: String, password: String): DocumentsApi {
        val authHeader = Credentials.basic(username, password)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", authHeader)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        val formattedBaseUrl = if (baseUrl.endsWith('/')) baseUrl else "$baseUrl/"

        return Retrofit.Builder()
            .baseUrl(formattedBaseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DocumentsApi::class.java)
    }
}
