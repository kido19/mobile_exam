package com.example.dailynotes.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    val id: Int = 0,
    @SerializedName("quote") val content: String,
    val author: String
)

interface QuoteApi {
    @GET("quotes/random")
    suspend fun getRandomQuote(): QuoteResponse

    companion object {
        private const val BASE_URL = "https://dummyjson.com/"

        fun create(): QuoteApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuoteApi::class.java)
        }
    }
}
