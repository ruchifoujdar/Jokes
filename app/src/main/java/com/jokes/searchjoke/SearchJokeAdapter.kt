package com.jokes.searchjoke

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jokes.databinding.AdapterSearchJokeBinding
import com.jokes.model.Jokes
import com.jokes.model.Results

class SearchJokeAdapter : RecyclerView.Adapter<SearchJokeAdapter.MainViewHolder>() {

    private var jokesList = Results()
    fun setItems(jokesList: Results) {
        this.jokesList = jokesList
        notifyDataSetChanged()
    }

    private var clickListener: ClickListener? = null

    inner class MainViewHolder(private val binding: AdapterSearchJokeBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(jokes: Jokes) {
            binding.jokes = jokes
        }

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }

        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterSearchJokeBinding.inflate(inflater, parent, false)

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val jokes = jokesList.result[position]
        holder.bind(jokes)
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemCount(): Int {
        return jokesList.result.size
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }
}