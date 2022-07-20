package com.jokes.repository

import android.text.Editable
import com.jokes.api.RetrofitService

class MainRepository(private val retrofitService: RetrofitService?) {

 suspend fun getRandomJokes() = retrofitService?.getRandomJokes()

 suspend fun getCategories() = retrofitService?.getCategories()

 suspend fun getJokeByCategory(category: String) = retrofitService?.getJokeByCategory(category)

 suspend fun searchJoke(query: Editable) = retrofitService?.searchJokes(query)
}

