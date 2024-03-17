package com.example.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class ForgotpwActivity : AppCompatActivity() {
    lateinit var inputEmail : EditText         // 초반 이메일 입력용
    lateinit var inputName : EditText          // 초반 닉네임 입력용
    lateinit var inputPassword1 : EditText     // 바꿀 비밀번호
    lateinit var inputPassword2 : EditText     // 바꿀 비밀번호 확인
    lateinit var confirmButton : Button        // 비밀번호 변경 확정 버튼
    lateinit var emailResult : TextView        // email 유무 결과 확인용
    lateinit var nameResult : TextView         // 닉네임과 email 일치여부 확인용
    lateinit var passwordResult1 : TextView    // 비밀번호 형식여부 확인용
    lateinit var passwordResult2 : TextView    // 비밀번호 일치여부 확인용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpw)

        init()
        confirmButton.setOnClickListener {
            var pw1 = inputPassword1.text.toString()
            var pw2 = inputPassword2.text.toString()
            val email = inputEmail.text.toString()
            val name = inputName.text.toString()
            val endPoint = "/api/editPassword"
            val url = ipAddr + endPoint

            if(checkPassword(pw1,pw2) == 1){
                lifecycleScope.launch {
                    val responseMessage = sendDataForChangePassword(email, name, pw1, url)
                    withContext(Dispatchers.Main) {
                        when {
                            responseMessage.contains("성공적으로 변경되었습니다.") -> {
                                Toast.makeText(applicationContext, responseMessage, Toast.LENGTH_LONG).show()
                                finish()
                            }
                            responseMessage.contains("이메일과 이름이 일치하지 않습니다.") -> {
                                Toast.makeText(applicationContext, "이메일과 이름이 일치하지 않습니다. 다시 확인해 주세요.", Toast.LENGTH_LONG).show()
                            }
                            responseMessage.contains("해당 이메일로 가입된 사용자가 없습니다.") -> {
                                Toast.makeText(applicationContext, "해당 이메일로 가입된 사용자를 찾을 수 없습니다. 이메일을 다시 확인해 주세요.", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                // 예상치 못한 응답 또는 다른 에러 메시지 처리
                                Toast.makeText(applicationContext, "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }

            }
        }

    }

    private fun checkPassword(pw1:String, pw2:String) : Int{
        var result : Int = 0
        var equalCheck : Int = 0
        var typeCheck : Int = 0
        equalCheck = checkPasswordEqual(pw1, pw2)
        typeCheck = checkPasswrodType(pw1)
        if(equalCheck == 1 && typeCheck == 1){
            passwordResult1.setText("올바른 비밀번호")
            passwordResult1.setTextColor(ContextCompat.getColor(this,R.color.right))

            passwordResult2.setText("비밀번호가 일치합니다")
            passwordResult2.setTextColor(ContextCompat.getColor(this,R.color.right))
            result =  1
        }
        else if(equalCheck != 1 && typeCheck == 1){
            passwordResult1.setText("올바른 비밀번호")
            passwordResult1.setTextColor(ContextCompat.getColor(this,R.color.right))

            passwordResult2.setText("비밀번호가 다릅니다")
            passwordResult2.setTextColor(ContextCompat.getColor(this,R.color.wrong))
            result =  0
        }

        else if(typeCheck != 1 && equalCheck == 1){
            passwordResult1.setText("사용할 수 없는 비밀번호")
            passwordResult1.setTextColor(ContextCompat.getColor(this,R.color.wrong))

            passwordResult2.setText("비밀번호가 일치합니다")
            passwordResult2.setTextColor(ContextCompat.getColor(this,R.color.right))
            result = 0
        }
        else if(equalCheck != 1 && typeCheck != 1){
            passwordResult1.setText("사용할 수 없는 비밀번호")
            passwordResult1.setTextColor(ContextCompat.getColor(this,R.color.wrong))

            passwordResult2.setText("비밀번호가 다릅니다")
            passwordResult2.setTextColor(ContextCompat.getColor(this,R.color.right))
            result =  0
        }
        return result
    }

    private fun init(){
        inputEmail = findViewById(R.id.inputEmail)
        inputName = findViewById(R.id.inputName)
        inputPassword1 = findViewById(R.id.inputPassword1)
        inputPassword2 = findViewById(R.id.inputPassword2)
        confirmButton = findViewById(R.id.confirmButton)
        emailResult = findViewById(R.id.emailResult)
        nameResult = findViewById(R.id.nameResult)
        passwordResult1 = findViewById(R.id.passwordResult1)
        passwordResult2 = findViewById(R.id.passwordResult2)
    }
}

fun checkPasswrodType(password : String) : Int{
    val passwordPattern: String = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Za-z0-9]).{8,}$"
    val pattern = Pattern.compile(passwordPattern)
    val matcher = pattern.matcher(password)
    if (matcher.matches()) // 비밀번호 형식 일치
        return 1
    else
        return 0
}

fun checkPasswordEqual(password1 : String, password2 : String) : Int{
    if(password1 == password2) // 비밀번호 일치 시
        return 1
    else                 // 비밀번호 불일치 시
        return 0
}