package com.jokes.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jokes.MainViewModel
import com.jokes.MainViewModelFactory
import com.jokes.R
import com.jokes.api.RetrofitService
import com.jokes.databinding.ActivityCategoriesBinding
import com.jokes.databinding.CustomDialogBinding
import com.jokes.model.Categories
import com.jokes.model.Jokes
import com.jokes.repository.MainRepository

class CategoriesActivity : AppCompatActivity() {

    private val categoriesAdapter = CategoriesAdapter()
    private var categories = ArrayList<Categories>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitService = RetrofitService.getInstance()
        var mainRepository= MainRepository(retrofitService)

        var mainViewModel = ViewModelProvider(this,
            MainViewModelFactory(mainRepository)
        )[MainViewModel::class.java]

        binding.recyclerViewCategories.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        binding.recyclerViewCategories.adapter = categoriesAdapter

        binding.retryBtn.visibility = View.INVISIBLE
        mainViewModel.jokeCategories.observe(this) { it1 ->
            it1.forEach{  println(it)
                categories.add(Categories(it)) }
            categoriesAdapter.setItems(categories)
        }

        mainViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            binding.retryBtn.visibility = View.VISIBLE
        }

        mainViewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }

        mainViewModel.getCategories()

        categoriesAdapter.setOnItemClickListener(object : CategoriesAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                val category =  mainViewModel.jokeCategories.value?.get(position)
                mainViewModel.getJokeByCategory(category+"")
                mainViewModel.randomJokes.observe(this@CategoriesActivity) {
                    showCustomAlert(it)
                }
            }
        })

        binding.retryBtn.setOnClickListener {
            mainViewModel.getCategories()
        }
    }
    private fun showCustomAlert(joke: Jokes) {
        val binding: CustomDialogBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.custom_dialog,
                null,
                false
            )
        val customDialog = this?.let {
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