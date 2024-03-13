package com.example.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
    lateinit var password1 : EditText // 입력한 비밀번호
    lateinit var passwordView1 : TextView // 비밀번호 형식 관련
    lateinit var password2 : EditText // 입력한 두번째 비밀번호
    lateinit var passwordView2 : TextView // 비밀번호 일치 여부 관련
    lateinit var name1 : EditText // 닉네임 입력
    lateinit var nameView : TextView // 닉네임 중복여부
    lateinit var registButton : Button // 가입 버튼
    lateinit var nameCheckBtn : Button  // 이름중복검사 버튼
    var idResult : Int = 0 // id가 email 형식을 충족?
    var passwordResult : Int = 0 // 비밀번호가 형식을 충족?
    var passwordResult2 : Int = 0 // 비밀번호 일치?
    var nameResult : Int = 0 // 닉네임 사용 가능 여부?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameCheckBtn = findViewById(R.id.nameCheckButton)
        id = findViewById(R.id.id)
        idView = findViewById(R.id.idView)
        password1 = findViewById(R.id.password)
        passwordView1 = findViewById(R.id.passwordView1)
        password2 = findViewById(R.id.passwordCheck)
        passwordView2 = findViewById(R.id.passwordView2)
        name1 = findViewById(R.id.name)
        nameView = findViewById(R.id.nameView)
        registButton = findViewById(R.id.registButton)

        //이름중복검사버튼 눌렀을 때
        nameCheckBtn.setOnClickListener {
            checkName()
        }
        // 회원가입 버튼 눌렀을 때
        registButton.setOnClickListener{
            checkAll()
        }
    }

    private fun checkEmail(){
        var email : String
        email = id.text.toString()
        // 이메일 형식 확인을 위한 정규표현식
        val emailPattern: String = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

        // 정규표현식과 일치하는지 확인
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)

        //아이디 관련
        if(matcher.matches()){ // 아이디 사용 가능 시
            idView.setText("올바른 이메일 형식입니다.")
            idResult = 1
            idView.setTextColor(ContextCompat.getColor(this,R.color.right))
        }
        else{// 아이디 사용 불가 시
            idView.setText("올바르지 않은 형식입니다.")
            idView.setTextColor(ContextCompat.getColor(this,R.color.wrong))
        }

    }
    private fun checkPassword(){
        var passwrod1 = password1.text.toString()
        var password2 = password2.text.toString()

        if(passwrod1 != password2){
            passwordView2.setText("비밀번호가 일치하지 않습니다")
            passwordView2.setTextColor(ContextCompat.getColor(this,R.color.wrong))
            passwordResult2 = 0
        }
        else{
            passwordView2.setText("비밀번호가 일치합니다")
            passwordView2.setTextColor(ContextCompat.getColor(this,R.color.right))
            passwordResult2 = 1
        }

        val passwordPattern: String = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Za-z0-9]).{8,}$"
        val pattern2 = Pattern.compile(passwordPattern)
        val matcher2 = pattern2.matcher(password2)
        if (matcher2.matches()) { // 비밀번호 형식 일치
            passwordView1.setText("올바른 비밀번호")
            passwordView1.setTextColor(ContextCompat.getColor(this,R.color.right))
            passwordResult = 1
        }
        else{  //비밀번호 형식 불일치
            passwordView1.setText("사용할 수 없는 비밀번호")
            passwordView1.setTextColor(ContextCompat.getColor(this,R.color.wrong))
            passwordResult = 0
        }


    }
    private fun checkName() {
        val name = name1.text.toString()
        val endpoint = "/api/checkName"
        val url = ipAddr + endpoint
        sendName(name, url) { result ->
            runOnUiThread {
                if (result == 1) {
                    nameResult = 1
                    nameView.setTextColor(ContextCompat.getColor(this, R.color.right))
                    nameView.text = "사용 가능한 닉네임"
                } else if (result == 0) {
                    nameResult = 0
                    nameView.setTextColor(ContextCompat.getColor(this, R.color.wrong))
                    nameView.text = "닉네임 중복"
                }
            }
        }
    }

    private fun checkAll(){
        var result : Int
        checkEmail()
        checkPassword()
        if( !(nameResult == 1 && idResult == 1 && passwordResult == 1 && passwordResult2 == 1)){ // 하나라도 통과하지 않은 경우
            val shakeAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
            findViewById<View>(R.id.registerActivity).startAnimation(shakeAnimation) // loginLayout은 로그인 관련 뷰의 ID
            Toast.makeText(this@RegisterActivity, "조건들을 충족해주세요", Toast.LENGTH_SHORT).show()
        }
        else{ // 모든 조건 충족 시
            var user : User
            user = User().apply {
                name = name1.text.toString()
                email = id.text.toString()
                password = password1.text.toString()
            }
            val endpoint = "/api/testJoin"
            val url = ipAddr + endpoint
            lifecycleScope.launch {
                result = sendUserDataToJoinTest(user, url)
                if(result == 1){ // 정상적으로 등록이 되었다면. 현재는 testJoin이라서 int형으로 받지만 /join엔드포인트는 String이기 때문에 따로 바꾸기
                    val intent = Intent(this@RegisterActivity, PercolActivity::class.java).apply {
                        putExtra("name", name1.text.toString())
                        putExtra("email", id.text.toString())
                        putExtra("password", password1.text.toString())
                    }
                    startActivity(intent)
                }
            }
        }
    }
}