package com.example.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ClothesAdapter(private val categories: List<ClothCategory>) : RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder>() {

    class ClothesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(category: ClothCategory) {
            val titleTextView: TextView = view.findViewById(R.id.title)
            titleTextView.text = category.title

            val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewClothes2)
            recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = GridAdapter(category.items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recycler_layout, parent, false)
        return ClothesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothesViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount() = categories.size
}
