package com.example.homework

import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

fun postRequest() {
    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaType()
    val jsonBody = """{
        ## JSON BODY
    }""".trimIndent()
    val body = jsonBody.toRequestBody(mediaType)
    val request = Request.Builder()
        .url("https://api.kr.omnicommerce.ai/2022-08/")
        .post(body)
        .addHeader("x-api-key:", "test-ccXEyM9kXMQ7Km9j2qop8sCBZPRERFg5YKQYJQVf")
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body?.string())
    }
}

fun fetchTaggingInfo(productId: String, apiKey: String) {
    val client = OkHttpClient()

    // URL 구성 시, 스킴과 호스트를 분리하여 지정
    val urlBuilder = HttpUrl.Builder()
        .scheme("https")
        .host("api.kr.omnicommerce.ai") // 호스트명만 지정
        .addPathSegment("2022-08") // 경로의 첫 부분을 여기에 추가
        .addPathSegment("tagging")
        .addPathSegment("tags")
        .addPathSegment(productId)
        // 쿼리 파라미터는 이어서 추가
        .build()

    val request = Request.Builder()
        .url(urlBuilder)
        .get()
        .addHeader("X-Api-Key", apiKey)
        .addHeader("Content-Type", "application/json")
        .addHeader("Accept-Language", "en-US") // 필요에 따라 변경
        .build()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

        // 응답 본문 처리
        println(response.body?.string())
    }
}