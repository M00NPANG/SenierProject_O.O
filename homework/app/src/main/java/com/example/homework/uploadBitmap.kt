package com.example.homework

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.File
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

fun uploadCodiSet(bitmap: Bitmap, title: String, hashtag: String, comment: String, useremail: String, url: String) {
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
        .addFormDataPart("useremail", useremail)
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



fun uploadClothes(bitmap: Bitmap, cl_category : Int, useremail: String, cl_personal_color:String,url: String) {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream) // JPEG으로 변경, 품질 80
    val byteArray = stream.toByteArray()

    // 멀티파트 바디 구성
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("image", "codi_image.png",
            byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull()))
        .addFormDataPart("cl_category", cl_category.toString())
        .addFormDataPart("useremail", useremail)
        .addFormDataPart("cl_personal_color",cl_personal_color)
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

suspend fun uploadFace(bitmap: Bitmap, url: String): String = withContext(Dispatchers.IO) {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream) // JPEG으로 변경, 품질 80
    val byteArray = stream.toByteArray()
    val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("file", "image.jpg", byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull()))
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

    try {
        val response = client.newCall(request).execute() // 동기 호출로 변경
        if (response.isSuccessful) {
            response.body?.string() ?: "receive fail" // 성공 시 응답 내용 리턴, null이면 "fail"
        } else {
            "fail" // 실패 시 "fail" 리턴
        }
    } catch (e: IOException) {
        e.printStackTrace()
        "exception" // 예외 발생 시 "exception" 리턴
    }
}
