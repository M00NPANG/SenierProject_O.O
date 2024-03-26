package com.example.homework

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.Gson // JSON으로 변환하기 위해 GSON 라이브러리 사용
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import java.io.IOException



suspend fun sendUserData(user: User, url: String): Int = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val gson = Gson()
    val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val jsonString = gson.toJson(user)

    val body = jsonString.toRequestBody(jsonMediaType)
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    client.newCall(request).execute().use { response ->
        val responseBody = response.body?.string() ?: ""
        Log.d("Server Response", responseBody)
        return@withContext when (responseBody) {
            "로그인 성공" -> 1
            "로그인 실패" -> 0
            else -> -1 // 예상치 못한 응답이나 응답 본문이 비어 있는 경우
        }
    }
}


suspend fun sendUserDataToJoinTest(user: User, url: String): Int = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val gson = Gson()
    val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val jsonString = gson.toJson(user)

    val body = jsonString.toRequestBody(jsonMediaType)
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    client.newCall(request).execute().use { response ->
        val responseBody = response.body?.string() ?: ""
        Log.d("서버에 유저 보낸 결과", responseBody)
        try {
            responseBody.toInt()
        } catch (e: NumberFormatException) {
            // 응답 본문이 Int로 변환될 수 없는 경우
            -1
        }
    }
}

suspend fun sendUserDataToJoin(user: User, url: String): Int = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val gson = Gson()
    val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val jsonString = gson.toJson(user)

    val body = jsonString.toRequestBody(jsonMediaType)
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    client.newCall(request).execute().use { response ->
        val responseBody = response.body?.string() ?: ""
        Log.d("서버에 유저 보낸 결과", responseBody)

        // 성공 응답 처리
        if (response.isSuccessful) {
            Log.d("성공", "서버로부터 받은 이메일: $responseBody")
            return@withContext 0 // 성공적으로 사용자 데이터가 서버에 등록되었음을 나타냄
        } else {
            Log.e("실패", "오류 메시지: $responseBody")
            return@withContext -1 // 실패 (예: 잘못된 요청, 서버 내부 오류 등)
        }
    }
}


fun sendName(name: String, url: String, callback: (Int) -> Unit) {
    if (name.isBlank()) {
        Log.d("이름 공백", "공백")
        callback(0) // 공백일 경우 콜백 호출
        return
    }
    val client = OkHttpClient()
    val requestBody = FormBody.Builder()
        .add("name", name)
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            // 에러 처리를 위한 콜백 호출 필요 (예시에서는 생략)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string().toString()
                Log.d("Response", responseBody)
                callback(responseBody.toInt()) // 응답 성공시 콜백 호출
            } else {
                Log.e("Error", "Server responded with error")
                // 에러 처리를 위한 콜백 호출 필요 (예시에서는 생략)
            }
        }
    })
}

suspend fun sendDataForChangePassword(email: String, name: String, newPassword: String, url: String): String = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val requestBody = FormBody.Builder()
        .add("email", email)
        .add("name", name)
        .add("newPassword", newPassword)
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    client.newCall(request).execute().use { response ->
        val responseBody = response.body?.string() ?: ""
        if (response.isSuccessful) {
            // 서버에서 비밀번호 변경 성공 메시지를 반환
            return@withContext responseBody
        } else {
            // 서버에서 예외 발생, 에러 메시지 반환
            return@withContext responseBody
        }
    }
}


