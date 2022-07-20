package com.jokes.searchjoke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jokes.MainViewModel
import com.jokes.MainViewModelFactory
import com.jokes.api.RetrofitService
import com.jokes.databinding.ActivitySearchJokeBinding
import com.jokes.repository.MainRepository
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager


class SearchJokeActivity : AppCompatActivity() {

    private val searchJokeAdapter = SearchJokeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivitySearchJokeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        var mainRepository= MainRepository(retrofitService)

        var mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(mainRepository)
        )[MainViewModel::class.java]

        binding.recyclerViewSearchJoke.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.recyclerViewSearchJoke.adapter = searchJokeAdapter

        binding.retryBtn.visibility = View.INVISIBLE
        mainViewModel.searchJokes.observe(this) {
            searchJokeAdapter.setItems(it)
        }

        mainViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            binding.retryBtn.visibility = View.VISIBLE
        }
        binding.progressDialog.visibility = View.GONE
        mainViewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
        binding.searchBtn.setOnClickListener {
            var query = binding.editTextTextPersonName.text
            mainViewModel.searchJokes(query)
        }

        binding.retryBtn.setOnClickListener {
            var query = binding.editTextTextPersonName.text
            mainViewModel.searchJokes(query)
        }
    }
}