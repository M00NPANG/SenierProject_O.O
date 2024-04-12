package com.example.homework
import java.lang.reflect.Type
import android.util.Log
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

@GlideModule
class GlideModule : AppGlideModule() { //글라이드 쓰려고 선언

}


// 서버에서 내가만든 Post 리스트를 받아오는 함수
suspend fun receivePosts(email : String): List<CodyGridItem> = withContext(Dispatchers.IO) {
    Log.d("현재 email", email)
    val posts = mutableListOf<CodyGridItem>()
    try {
        val url = "$ipAddr/receivePost?userEmail=$email"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Post>>() {}.type
                val postList: List<Post> = gson.fromJson(responseBody, listType)
                postList.forEach { post ->
                    // imagePath가 있는 경우와 없는 경우를 모두 처리
                    posts.add(CodyGridItem(
                        post_id = post.post_id,
                        imagePath = post.imagePath,
                        title = post.title!!,
                        hashtag = post.hashtag,
                        content = post.content,
                        username = post.userName,
                        useremail = post.userEmail
                    ))
                }
            }
        }
        Log.d("전달받은결과",posts.toString())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return@withContext posts
}
// 서버에서 추천받은 게시물을 보이는 함수
suspend fun receiveRecommendPosts(email : String): List<Post> = withContext(Dispatchers.IO) {
    Log.d("현재 email", email)
    val posts = mutableListOf<Post>()
    try {
        //val url = "$ipAddr/receiveRecommendedPosts?userEmail=$email"
        val url = "$ipAddr/api/preferences/receiveRecommendedPosts?userEmail=$email"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Post>>() {}.type
                val postList: List<Post> = gson.fromJson(responseBody, listType)
                postList.forEach { post ->
                    // imagePath가 있는 경우와 없는 경우를 모두 처리
                    posts.add(Post(
                        post_id = post.post_id,
                        imagePath = post.imagePath,
                        title = post.title,
                        hashtag = post.hashtag,
                        content = post.content,
                        userName = post.userName,
                        userEmail = post.userEmail,
                        post_color = post.post_color,
                        post_percol = post.post_percol
                    ))
                }
            }
        }
        Log.d("전달받은결과",posts.toString())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return@withContext posts
}

suspend fun receiveClothes(email: String): List<Clothes> = withContext(Dispatchers.IO) {
    val clothesList = mutableListOf<Clothes>()
    try {
        val url = "$ipAddr/receiveClothes?email=$email"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Clothes>>() {}.type
                val fetchedClothesList: List<Clothes> = gson.fromJson(responseBody, listType)
                clothesList.addAll(fetchedClothesList)
            }
        }
        Log.d("전달받은결과", clothesList.toString())

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return@withContext clothesList
}

// 선호도 업데이트
suspend fun sendUserPreferenceUpdate(requestData: UserPreferenceUpdateRequest): Response? = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val gson = Gson()
    val url = "$ipAddr/api/preferences/update"
    val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    val json = gson.toJson(requestData)
    val requestBody = json.toRequestBody(jsonMediaType)
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    try {
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            // 서버 응답 처리
            Log.d("서버응답",response.toString())
            return@withContext response
        } else {
            // 실패 처리
            return@withContext null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return@withContext null
    }
}



suspend fun receiveRecommendedClothes(email: String, cl_category : Int): List<Clothes> = withContext(Dispatchers.IO) {
    val clothesList = mutableListOf<Clothes>()
    try {
        // URL에 email과 cl_id를 포함하여 서버로 전송
        val email2  = "ClosetHelper"
        val url = "$ipAddr/receiveRecommendedClothes?email=$email2&cl_category=$cl_category"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Clothes>>() {}.type
                val fetchedClothesList: List<Clothes> = gson.fromJson(responseBody, listType)
                clothesList.addAll(fetchedClothesList)
            }
        }
        Log.d("전달받은결과", clothesList.toString())

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return@withContext clothesList
}