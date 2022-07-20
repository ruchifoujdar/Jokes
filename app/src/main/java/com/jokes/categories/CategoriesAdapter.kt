package com.jokes.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jokes.databinding.AdapterCategoriesBinding
import com.jokes.model.Categories

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.MainViewHolder>() {

    private var categoryList = listOf<Categories>()
    fun setItems(categoryList: List<Categories>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    private var clickListener: ClickListener? = null

    inner class MainViewHolder(private val binding: AdapterCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(categories: Categories) {
            binding.categories = categories
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
        val binding = AdapterCategoriesBinding.inflate(inflater, parent, false)

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val categories = categoryList[position]
        holder.bind(categories)
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }
}