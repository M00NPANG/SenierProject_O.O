package com.example.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class GridAdapter(private val gridItemList: List<GridItem>) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    class GridViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(gridItem: GridItem) {
            val imageView: ImageView = view.findViewById(R.id.imageView)
            imageView.setImageResource(gridItem.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.grid_item_layout, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = gridItemList[position]
        holder.bind(item)
    }

    override fun getItemCount() = gridItemList.size
}
