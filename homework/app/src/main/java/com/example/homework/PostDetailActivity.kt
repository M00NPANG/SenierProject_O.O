package com.example.homework

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class PostDetailActivity : AppCompatActivity() {
    lateinit var frameLayout : FrameLayout
    lateinit var comment : TextView
    lateinit var post : Post
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        init()

    }

    private fun init() {
        frameLayout = findViewById(R.id.view)
        comment = findViewById(R.id.comment)

        post = intent.getParcelableExtra<Post>("post")!!


        // Set the comment text
        comment.text = post.content

        // Load and display selected images
        post.post_id?.let {
            loadSelectedImages(it)
        }
    }

    private fun loadSelectedImages(post_id: Long) {
        lifecycleScope.launch {
            val selectedImages = receiveSelectedimage(post_id)
            Log.d("받은 선택이미지 리스트", selectedImages.toString())
            selectedImages.forEach { image ->
                addImageToLayout(image)
            }
        }
    }

    private fun addImageToLayout(image: selectedimage) {
        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                image.width ?: 100,  // 기본값 100, 만약 null이면
                image.height ?: 100  // 기본값 100, 만약 null이면
            ).apply {
                leftMargin = image.x?.toInt() ?: 0  // 기본값 0, 만약 null이면
                topMargin = image.y?.toInt() ?: 0   // 기본값 0, 만약 null이면
            }

            scaleType = ImageView.ScaleType.FIT_CENTER

            // 이미지에 OnClickListener 추가
            setOnClickListener {
                Toast.makeText(this@PostDetailActivity, "Selected Image ID: ${image.si_id}", Toast.LENGTH_SHORT).show()
                Log.d("SelectedImage", "Selected Image ID: ${image.si_id}")
            }
        }

        Glide.with(this)
            .load(image.imageUrl ?: "")
            .into(imageView)

        frameLayout.addView(imageView)
    }

}