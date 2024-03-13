package com.example.homework

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    lateinit var btnRegister: TextView          // 회원가입 버튼
    lateinit var btnLogin: Button          // 로그인 버튼
    lateinit var btnPassword : TextView       // 비밀번호 재설정 버튼
    lateinit var btnKakao: ImageView          // 카카오 로그인 버튼
    lateinit var btnGoogle: ImageView          // 구글 로그인
    lateinit var checkID: EditText          // 이메일(id)
    lateinit var checkPW: EditText          // password
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnPassword = findViewById(R.id.findPassword)
        btnLogin = findViewById(R.id.regularLogin)
        btnRegister = findViewById(R.id.regularRegister)
        btnKakao = findViewById(R.id.kakaoLogin)
        btnGoogle = findViewById(R.id.googleLogin)
        checkID = findViewById(R.id.inputID)
        checkPW = findViewById(R.id.inputPW)

        btnPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotpwActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {//회원가입 버튼
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent) //
        }
        btnLogin.setOnClickListener { // 로그인 버튼
            val intent = Intent(this@LoginActivity, LobbyActivity::class.java)
            startActivity(intent)

            //CoroutineScope(Dispatchers.IO).launch {
            //    login()
            //}

        }
        btnKakao.setOnClickListener {// 카카오로그인 버튼
            Log.d("kakaoLogin", "clicked")
        }
        btnGoogle.setOnClickListener {// 구글로그인 버튼
            Log.d("googleLogin", "clicked")
        }
    }


    private suspend fun login() {
        val user = User().apply {
            email = checkID.text.toString()
            password = checkPW.text.toString()
        }
        val endPoint = "/api/login"
        val url = ipAddr + endPoint
        val responseCode = sendUserData(user, url)

        withContext(Dispatchers.Main) {
            when (responseCode) {
                1 -> {
                    // 로그인 성공
                    startActivity(Intent(this@LoginActivity, LobbyActivity::class.java))
                }
                0 -> {
                    // 로그인 실패
                    val shakeAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
                    findViewById<View>(R.id.loginActivityView).startAnimation(shakeAnimation)
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 에러
                    Toast.makeText(this@LoginActivity, "오류 발생", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

