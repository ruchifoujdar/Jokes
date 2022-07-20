package com.jokes

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jokes.model.Jokes
import com.jokes.model.Results
import com.jokes.repository.MainRepository
import kotlinx.coroutines.*

class MainViewModel constructor(private val mainRepository: MainRepository): ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val randomJokes = MutableLiveData<Jokes>()
    val jokeCategories = MutableLiveData<List<String>>()
    val searchJokes = MutableLiveData<Results>()

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getRandomJokes(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.getRandomJokes()
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    randomJokes.postValue(response.body())
                    loading.value = false
                } else {
                    if (response != null) {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    fun getCategories(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.getCategories()
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    jokeCategories.postValue(response.body())
                    loading.value = false
                } else {
                    if (response != null) {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    fun getJokeByCategory(category:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.getJokeByCategory(category)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    randomJokes.postValue(response.body())
                    loading.value = false
                } else {
                    if (response != null) {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    fun searchJokes(query: Editable){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.searchJoke(query)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    searchJokes.postValue(response.body())
                    loading.value = false
                } else {
                    if (response != null) {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}