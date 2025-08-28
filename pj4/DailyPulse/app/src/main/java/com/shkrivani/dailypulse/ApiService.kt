package com.shkrivani.dailypulse

import retrofit2.Call
import retrofit2.http.GET

data class Quote(val content: String, val author: String)

interface ApiService {
    @GET("/random")
    fun getRandomQuote(): Call<Quote>
}