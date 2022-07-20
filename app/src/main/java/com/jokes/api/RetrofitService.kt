package com.jokes.api

import android.text.Editable
import com.jokes.model.Jokes
import com.jokes.model.Results
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("random")
    suspend fun getRandomJokes() : Response<Jokes>

    @GET("categories")
    suspend fun getCategories() : Response<List<String>>

    @GET("random?")
    suspend fun getJokeByCategory(@Query("category") category: String) : Response<Jokes>

    @GET("search?")
    suspend fun searchJokes(@Query("query") query: Editable) : Response<Results>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.chucknorris.io/jokes/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}