package com.example.homework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

val ipAddr = "http://192.168.123.100:8080"

class MainActivity : AppCompatActivity() {
    lateinit var loginBtn : Button
    lateinit var test2Btn : Button
    lateinit var testBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn = findViewById(R.id.btnLogin)
        loginBtn.setOnClickListener {
            intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        testBtn = findViewById(R.id.btnTest)
        testBtn.setOnClickListener {
            intent = Intent(this@MainActivity,TestActivity::class.java)
            startActivity(intent)
        }

        test2Btn = findViewById(R.id.btnTest2)
        test2Btn.setOnClickListener {
            intent = Intent(this@MainActivity,CreateOutfitActivity::class.java)
            startActivity(intent)
        }
    }
}

