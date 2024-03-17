package com.example.design

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageViewCloset = findViewById<ImageView>(R.id.closet)
        imageViewCloset.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                ClosetActivity::class.java
            )
            startActivity(intent)
        }
    }
}

