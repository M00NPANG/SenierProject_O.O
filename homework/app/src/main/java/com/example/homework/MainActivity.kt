package com.example.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {
    lateinit var loginBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginBtn = findViewById(R.id.btnLogin)
        loginBtn.setOnClickListener {
            intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}