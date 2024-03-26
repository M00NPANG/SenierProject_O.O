package com.example.homework
import java.lang.reflect.Type
import android.util.Log
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@GlideModule
class GlideModule : AppGlideModule() { //글라이드 쓰려고 선언
}


// 서버에서 Post 리스트를 받아오는 함수
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
                        title = post.title,
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


suspend fun checkCategory(clothesList: List<Clothes>): List<ClothCategory> {
    // 각 카테고리별로 GridItem 리스트를 저장할 맵 초기화
    val categoryItems = mutableMapOf<String, MutableList<GridItem>>()

    // 카테고리 ID와 카테고리 이름 매핑
    val categoryNameMap = mapOf(
        10 to "상의",
        20 to "하의",
        30 to "원피스/투피스/점프슈트",
        40 to "아우터",
        50 to "신발",
        60 to "패션 잡화"
    )

    // 받은 clothesList를 순회하며 각 Clothes 객체를 적절한 카테고리에 분류
    clothesList.forEach { cloth ->
        val categoryName = categoryNameMap[cloth.cl_category] ?: "기타"
        val imageUrl = cloth.cl_photo_path ?: ""
        val gridItem = GridItem(imageUrl)

        categoryItems.getOrPut(categoryName) { mutableListOf() }.add(gridItem)
    }

    // 분류된 결과를 바탕으로 ClothCategory 리스트 생성
    val categories = categoryItems.map { (categoryName, items) ->
        ClothCategory(categoryName, items)
    }

    return categories
}
