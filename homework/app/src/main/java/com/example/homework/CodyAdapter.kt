package com.example.homework

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CodyAdapter(private val context: Context, private val dataSource: List<CodyGridItem>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_post, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val codyGridItem = getItem(position) as CodyGridItem
        viewHolder.textView.text = codyGridItem.title

        //viewHolder.imageView.setImageResource(codyGridItem.imageResId)
        codyGridItem.imagePath?.let { path ->
            // 내부 저장소의 이미지 경로가 있으면 Bitmap을 로드해서 설정
            val bitmap = BitmapFactory.decodeFile(path)
            viewHolder.imageView.setImageBitmap(bitmap)
        } ?: run {
            // imagePath가 없는 경우, 기본 이미지 설정
            viewHolder.imageView.setImageResource(R.drawable.test_cody)
            // viewHolder.imageView.setImageBitmap(null)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.uploadedImageView)
        val textView: TextView = view.findViewById(R.id.codyName)
    }
}
