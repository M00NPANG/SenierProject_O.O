package com.example.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var id : EditText //입력한 아이디
    lateinit var idView : TextView // 이메일 형식 관련
    lateinit var password : EditText // 입력한 비밀번호
    lateinit var passwordView1 : TextView // 비밀번호 형식 관련
    lateinit var passwordCheck : EditText // 입력한 두번째 비밀번호
    lateinit var passwordView2 : TextView // 비밀번호 일치 여부 관련
    lateinit var name : EditText // 닉네임 입력
    lateinit var nameView : TextView // 닉네임 중복여부
    lateinit var registButton : Button // 가입 버튼
    var idResult : Int = 0 // id가 email 형식을 충족?
    var passwordResult : Int = 0 // 비밀번호가 형식을 충족시키며 일치?
    var nameResult : Int = 0 // 닉네임 사용 가능 여부?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        id = findViewById(R.id.id)
        idView = findViewById(R.id.idView)
        password = findViewById(R.id.password)
        passwordView1 = findViewById(R.id.passwordView1)
        passwordCheck = findViewById(R.id.passwordCheck)
        passwordView2 = findViewById(R.id.passwordView2)
        name = findViewById(R.id.name)
        nameView = findViewById(R.id.nameView)
        registButton = findViewById(R.id.registButton)

        // 회원가입 버튼 눌렀을 때
        registButton.setOnClickListener{
            var  getId: String   // 아이디란에 쓴 아이디
            var getPassword: String // 비밀번호란에 쓴 비밀번호
            var getPassword2: String // 비밀번호 재입력란에 쓴 비밀번호
            var getName : String // 닉네임란에 쓴 닉네임

            getId = id.text.toString() // 입력한 이메일
            getPassword = password.text.toString() // 입력한 비밀번호
            getPassword2 = passwordCheck.text.toString() // 재입력한 비밀번호
            getName = name.text.toString() // 입력한 닉네임


            // 이메일 형식 확인을 위한 정규표현식
            val emailPattern: String = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

            // 정규표현식과 일치하는지 확인
            val pattern = Pattern.compile(emailPattern)
            val matcher = pattern.matcher(getId)

            //아이디 관련
            if(matcher.matches()){ // 아이디 사용 가능 시
                idView.setText("사용가능 아이디")
                idResult = 1
            }
            else // 아이디 사용 불가 시
                idView.setText("email 형식 오류")

            // 비밀번호 관련
            if(getPassword != getPassword2 ) // 비밀번호 불일치 시
                passwordView2.setText("비밀번호 불일치")

            else {// 비밀번호 일치 시
                passwordView2.setText("비밀번호 일치")
                val passwordPattern: String = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Za-z0-9]).{8,}$"
                val pattern2 = Pattern.compile(passwordPattern)
                val matcher2 = pattern2.matcher(getPassword)

                // 비밀번호 형식 확인
                if (matcher2.matches()) { // 비밀번호 형식 일치
                    passwordView1.setText("사용가능 비밀번호")
                    passwordResult = 1
                }
                else{  //비밀번호 형식 불일치
                    passwordView1.setText("비밀번호 형식오류")

                }
            }
            checkname()

            if(idResult == 1 && passwordResult == 1 && nameResult == 1){
                // 모든 조건을 충족하면 intent에 값들 넣고 다음액티비티로 전달
                intent = Intent(this@RegisterActivity,ProfilesetupActivity::class.java)
                intent.putExtra("id",getId)
                intent.putExtra("password",getPassword)
                intent.putExtra("name",getName)
                startActivity(intent)
                regist()
            }
        }

    }
    private fun checkname() = lifecycleScope.launch(Dispatchers.IO) {
        try {
            val data = name.text.toString()

            // Gson을 사용하여 객체를 JSON 문자열로 변환
            val gson = Gson()
            val jsonData = gson.toJson(data)
            val url = URL("http://10.0.2.2:8080/namecheck")

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
                    nameResult = response.toString().toInt() // 접속 결과
                    if(nameResult != 1){
                        nameView.setText("중복된 닉네임.")
                    }
                    else{
                        nameView.setText("사용 가능한 닉네임.")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to connect to the server", e)
        }
    }

    private fun regist() = lifecycleScope.launch(Dispatchers.IO) {
        try {
            val user = User(null,id.text.toString(), password.text.toString(), name.text.toString(), "cool_winter","NORMAL","REGULAR")

            // Gson을 사용하 여 객체를 JSON 문자열로 변환
            val gson = Gson()
            val jsonData = gson.toJson(user)

            val url = URL("http://10.0.2.2:8080/regist")

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


                }
            }
        } catch (e: Exception) {
            Log.e("Error", "Failed to connect to the server", e)
        }
    }
}