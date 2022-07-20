package com.jokes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.jokes.api.RetrofitService
import com.jokes.categories.CategoriesActivity
import com.jokes.repository.MainRepository
import com.jokes.databinding.ActivityMainBinding
import com.jokes.databinding.CustomDialogBinding
import com.jokes.model.Jokes
import com.jokes.searchjoke.SearchJokeActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       val retrofitService = RetrofitService.getInstance()
       val mainRepository= MainRepository(retrofitService)

       val mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(mainRepository)
        )[MainViewModel::class.java]


        binding.retryBtn.visibility = View.INVISIBLE

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

        binding.retryBtn.setOnClickListener {
            mainViewModel.getRandomJokes()
            mainViewModel.randomJokes.observe(this) {it1 ->
                showCustomAlert(it1)
            }
        }

        binding.randomJokeBtn.setOnClickListener {
            mainViewModel.getRandomJokes()
            mainViewModel.randomJokes.observe(this) {it1 ->
                showCustomAlert(it1)
            }
        }

        binding.categoriesBtn.setOnClickListener {
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
        }

        binding.searchJokeBtn.setOnClickListener {
            val intent = Intent(this, SearchJokeActivity::class.java)
            startActivity(intent)
        }

    }
    private fun showCustomAlert(joke: Jokes?) {
        val binding: CustomDialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.custom_dialog,
                null,
                false
            )
        val customDialog = this.let {
            AlertDialog.Builder(it)
                .setView(binding.root)
                .show()
        }
        binding.randomJokes = joke
        customDialog?.setCanceledOnTouchOutside(true)
        binding.okButton.setOnClickListener{
            customDialog?.dismiss()
        }
    }
}