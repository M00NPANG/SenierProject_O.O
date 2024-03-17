package com.example.homework

import android.graphics.Bitmap
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

fun uploadBitmap(bitmap: Bitmap, url: String){
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream) // JPEG으로 변경, 품질 80
    val byteArray = stream.toByteArray()
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("file", "image.jpg",
            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull()))
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 에러 처리
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            // 성공 처리, 예: 응답 내용 출력
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                // 응답 내용 기다리고, 그 반영됨을 확인할 수 있어야함
                Log.d("RESPONSE_ITEM","$responseBody")

            }

        }
    })
}

fun uploadCodiSet(bitmap: Bitmap, title: String, hashtag: String, comment: String, username: String, url: String) {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream) // JPEG으로 변경, 품질 80
    val byteArray = stream.toByteArray()

    // 멀티파트 바디 구성
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("image", "codi_image.jpg",
            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull()))
        .addFormDataPart("title", title)
        .addFormDataPart("hashtag", hashtag)
        .addFormDataPart("comment", comment)
        .addFormDataPart("username", username)
        .build()

    // 요청 생성 (post)
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    // 클라이언트 설정 및 요청 실행
    val client = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 에러 처리
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            // 성공 처리, 예: 응답 내용 출력
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("RESPONSE_CODISET","$responseBody")
            }
        }
    })
}

fun uploadImage(bitmap: Bitmap, title: String, category: String, userEmail: String, url: String) {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream) // JPEG으로 변경, 품질 80
    val byteArray = stream.toByteArray()

    // 멀티파트 바디 구성
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("image", "codi_image.jpg",
            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull()))
        .addFormDataPart("title", title)
        .addFormDataPart("category", category)
        .addFormDataPart("user_email", userEmail)
        .build()

    // 요청 생성 (post)
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    // 클라이언트 설정 및 요청 실행
    val client = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            // 에러 처리
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            // 성공 처리, 예: 응답 내용 출력
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("RESPONSE_CODISET","$responseBody")
            }
        }
    })
}
