package com.example.homework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

val ipAddr = "http://192.168.123.103:8080" // http://192.168.123.103:8080

class MainActivity : AppCompatActivity() {
    lateinit var loginBtn : Button
    lateinit var test2Btn : Button
    lateinit var testBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // 자동 로그인
        val loggedInEmail = SharedPreferencesUtils.loadEmail(this)
        // 로그인한 이메일이 있다면(즉, null이 아니라면) 사용자가 이미 로그인했다고 침
        if (loggedInEmail != null) {
            // 로비 액티비티로 이동
            val intent = Intent(this, LobbyActivity::class.java)
            startActivity(intent)
            finish()
        } else {

        }

        testBtn = findViewById(R.id.btnTest)
        loginBtn = findViewById(R.id.btnLogin)

        loginBtn.setOnClickListener {
            intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
        }
        testBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity,OmniusActivity::class.java))
        }

    }
}

