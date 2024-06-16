package com.example.homework

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.splash_logo)
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.roading)
        imageView.startAnimation(rotateAnimation)
    }
}
