package com.example.homework

import android.annotation.SuppressLint
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ServerResponse1(val message: String)

interface ApiService {
    @GET("엔드포인트")
    fun getHello(): Call<ServerResponse1>
}

fun test() {
    // Retrofit 빌더 생성
    val retrofit = Retrofit.Builder()
        .baseUrl("url")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    // GET 요청 실행
    val call = apiService.getHello()
    call.enqueue(object : Callback<ServerResponse1> {
        override fun onResponse(call: Call<ServerResponse1>, response: Response<ServerResponse1>) {
            // 응답 출력
            Log.d("123123","responsed")
            if (response.isSuccessful) {
                val serverResponse = response.body()
            } else {
                // 실패 처리
                Log.e("connectTest", "에러: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<ServerResponse1>, t: Throwable) {
            // 실패 처리
            Log.e("connectTest", "네트워크 오류", t)
        }
    })

}


class ApitestActivity : AppCompatActivity() {
    lateinit var pushBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apitest)

        pushBtn = findViewById(R.id.pushBtn)
        pushBtn.setOnClickListener {
            test()
            Log.d("testbutton","pressed")
        }
    }
}