package com.example.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OmniusActivity : AppCompatActivity() {
    lateinit var btn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omnius)

        btn = findViewById(R.id.testbtn)
        btn.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                // 네트워크 요청 실행
                fetchTaggingInfo("", "test-ccXEyM9kXMQ7Km9j2qop8sCBZPRERFg5YKQYJQVf")
            }
        }
    }
}