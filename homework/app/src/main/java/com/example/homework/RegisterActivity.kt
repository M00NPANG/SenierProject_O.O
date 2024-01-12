package com.example.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.homework.databinding.ActivityProfilesetupBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var id : EditText
    lateinit var idView : TextView
    lateinit var password : EditText
    lateinit var passwordCheck : EditText
    lateinit var passwordView : TextView
    lateinit var name : EditText
    lateinit var nameCheckButton: Button
    lateinit var registButton : Button
    var idResult : Int = 0
    var passwordResult : Int = 0
    var nameResult : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        id = findViewById(R.id.id)
        idView = findViewById(R.id.idView)
        password = findViewById(R.id.password)
        passwordCheck = findViewById(R.id.passwordCheck)
        passwordView = findViewById(R.id.passwordView)
        name = findViewById(R.id.name)
        nameCheckButton = findViewById(R.id.nameCheckButton)
        registButton = findViewById(R.id.registButton)

        //중복검사 버튼 눌렀을 때
        nameCheckButton.setOnClickListener {
            //닉네임 중복확인을 위해 서버와 연결 필요함
            
            nameResult = 1
        }

        // 회원가입 버튼 눌렀을 때
        registButton.setOnClickListener{
            var  getId: String   // 아이디란에 쓴 아이디
            var getPassword: String // 비밀번호란에 쓴 비밀번호
            var getPassword2: String // 비밀번호 재입력란에 쓴 비밀번호
            var getName : String // 닉네임란에 쓴 닉네임

            getId = id.text.toString() // 입력한 이메일
            getPassword = password.text.toString() // 입력한 비밀번호
            getPassword2 = passwordCheck.text.toString() // 재입력한 비밀번호
            getName = name.text.toString() // 입력한 닉네임 ** 추후 닉네임 중복 방지 기능 필요


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
                passwordView.setText("비밀번호 불일치")
            else {// 비밀번호 일치 시
                val passwordPattern: String = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Za-z0-9]).{8,}$"
                val pattern2 = Pattern.compile(passwordPattern)
                val matcher2 = pattern2.matcher(getPassword)

                // 비밀번호 형식 확인
                if (matcher2.matches()) { // 비밀번호 형식 일치
                    passwordView.setText("사용가능 비밀번호")
                    passwordResult = 1
                }
                else{  //비밀번호 형식 불일치
                    passwordView.setText("비밀번호 형식오류")

                }
            }
            if(idResult == 1 && nameResult == 1 && passwordResult == 1){
                // 모든 조건을 충족하면 intent에 값들 넣고 다음액티비티로 전달하여 퍼스널컬러 등 측정
                intent = Intent(this@RegisterActivity,ProfilesetupActivity::class.java)
                intent.putExtra("id",getId)
                intent.putExtra("password",getPassword)
                intent.putExtra("name",getName)
                startActivity(intent)
            }
        }

    }
}