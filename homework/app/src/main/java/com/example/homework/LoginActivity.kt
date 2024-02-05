package com.example.homework

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.BufferedReader
import java.io.InputStreamReader

import java.net.HttpURLConnection
import java.net.URL

//request로 올려
data class User( // 유저 DB 대충 구성
    var userSeq : Int?,
    var userId : String,
    var userPwd : String,
    var userName : String,
    var userPercol : String?,
    var userAuth : String,
    var userType : String
)

class LoginActivity : AppCompatActivity() {
    lateinit var btnRegister : TextView          // 회원가입 버튼
    lateinit var btnLogin : Button          // 로그인 버튼
    lateinit var btnKakao : ImageView          // 카카오 로그인 버튼
    lateinit var btnGoogle : ImageView          // 구글 로그인
    lateinit var checkID : EditText          // id
    lateinit var checkPW : EditText          // password
    var resultLogin : Int = -1 // 로그인 결과값
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
            sendDataToServer()
        }
        btnKakao.setOnClickListener {// 카카오로그인 버튼
            Log.d("kakaoLogin","clicked")
        }
        btnGoogle.setOnClickListener {// 구글로그인 버튼
            Log.d("googleLogin","clicked")
        }
    }

    private fun sendDataToServer() = lifecycleScope.launch(Dispatchers.IO) { // 이미지를
        try {
            val user = User(null,checkID.text.toString(), checkPW.text.toString(),"ChunSamKim", "cool_winter","NORMAL","REGULAR")

            // Gson을 사용하여 객체를 JSON 문자열로 변환
            val gson = Gson()
            val jsonData = gson.toJson(user)

            val url = URL("http://10.0.2.2:8080/login")

            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "POST"
                doOutput = true

                // Content-Type 설정
                setRequestProperty("Content-Type", "application/json;charset=UTF-8")

                // 전송할 데이터 생성
                val postDataBytes = jsonData.toByteArray(Charsets.UTF_8)

                // 데이터 전송
                outputStream.use { it.write(postDataBytes) }

                // 서버 응답 읽기
                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuilder()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    Log.d("Server Response", response.toString())
                    resultLogin = response.toString().toInt() // 접속 결과
                    if(resultLogin == 1){ // 성공적
                        intent = Intent(this@LoginActivity, LobbyActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to connect to the server", e)
        }
    }
}