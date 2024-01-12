package com.example.homework

import android.content.Intent
import android.net.http.HttpResponseCache.install
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LoginActivity : AppCompatActivity() {
    lateinit var btnRegister : Button          // 회원가입 버튼
    lateinit var btnLogin : Button          // 로그인 버튼
    lateinit var btnKakao : Button          // 카카오 로그인 버튼
    lateinit var btnGoogle : Button          // 구글 로그인
    lateinit var checkID : EditText          // id
    lateinit var checkPW : EditText          // password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.regularLogin)
        btnRegister = findViewById(R.id.regularRegister)
        btnKakao = findViewById(R.id.kakaoLogin)
        btnGoogle = findViewById(R.id.googleLogin)
        checkID = findViewById(R.id.inputID)
        checkPW = findViewById(R.id.inputPW)

        btnRegister.setOnClickListener {//회원가입 버튼
            val intent2 = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent2) //
        }
        btnLogin.setOnClickListener { // 로그인 버튼
            // 로그인이 되었다면 로비화면으로 이동하며 현 날씨를 맨 위에 작게 보여줌. o
            // 저장된 정보와 같은지 확인을 위해 서버와 통신 필요.


            // 로그인 후 로비로 이동..
            val intent3 = Intent(this@LoginActivity,LobbyActivity::class.java)
            startActivity(intent3)

        }
        btnKakao.setOnClickListener {// 카카오로그인 버튼
            Log.d("kakaoLogin","clicked")
        }
        btnGoogle.setOnClickListener {// 구글로그인 버튼
            Log.d("googleLogin","clicked")
        }
    }


}