package com.example.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GridAdapter(private val items: List<GridItem>) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    // ViewHolder 클래스 정의
    class GridViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        // ViewHolder 내에서 ImageView를 찾습니다.
        private val imageView: ImageView = view.findViewById(R.id.imageView)

        // bind 메소드에서는 ViewHolder가 가진 view의 context를 사용하여 Glide 호출
        fun bind(item: GridItem) {
            Glide.with(view.context)
                .load(item.imageURL) // item에서 URL을 받아옵니다.
                .into(imageView) // imageView에 이미지를 로드합니다.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        // 아이템 뷰를 생성하여 ViewHolder에 전달합니다.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item_layout, parent, false)
        return GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        // ViewHolder의 bind 메소드를 호출하여 데이터를 뷰와 바인딩합니다.
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}


/*
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
 */
