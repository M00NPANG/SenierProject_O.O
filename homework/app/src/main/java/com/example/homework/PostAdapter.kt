package com.example.homework

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PostAdapter(private val posts: List<Post>, private val scope: CoroutineScope) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        // 여기서 post 객체의 각 필드를 holder의 뷰와 연결
        holder.bind(post, scope)
    }

    override fun getItemCount(): Int = posts.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.userName)
        private val uploadedImageView: ImageView = itemView.findViewById(R.id.uploadedImageView)
        private val codyName: TextView = itemView.findViewById(R.id.codyName)
        private val tags: TextView = itemView.findViewById(R.id.tags)
        private val heartImageView: ImageView = itemView.findViewById(R.id.heart)

        fun bind(post: Post, scope: CoroutineScope) { // 여기에서 작업하면 됨
            val percol = post.post_percol
            val color = post.post_color
            userName.text = post.userName
            codyName.text = post.title

            tags.text = post.hashtag
            Glide.with(itemView.context)
                .load(post.imagePath)
                .into(uploadedImageView)


            heartImageView.setOnClickListener {
                post.isLiked = !post.isLiked!!
                updateHeartIcon(post.isLiked!!, post, scope)
            }

        }

        private fun updateHeartIcon(isLiked: Boolean, post: Post, scope: CoroutineScope) {
            if (!isLiked) {
                heartImageView.setImageResource(R.drawable.heart)
            } else {
                heartImageView.setImageResource(R.drawable.selected_heart)
                val styleList = parseHashtagsToStyles(post.hashtag.toString()) // 리스트를 배열로 변환
                val styleNumList = Array(styleList.size) { 1L }

                Log.d("해시태그들", styleList.contentToString())
                Log.d("리스트수","리스트수:${styleNumList.size}, 리스트값:${styleNumList.contentToString()}")

                val email = SharedPreferencesUtils.loadEmail(itemView.context)
                Log.d("email",email!!)
                val updateRequest  = UserPreferenceUpdateRequest(email, null, styleList, 1, styleNumList)
                scope.launch {
                    try {
                        val result = sendUserPreferenceUpdate(updateRequest)
                        Log.d("업데이트 결과", result.toString())
                    } catch (e: Exception) {
                        Log.e("업데이트 실패", "오류: ${e.message}")
                    }
                }
            }
        }

        private fun parseHashtagsToStyles(hashtags: String): Array<String> {
            return hashtags.split(" ")
                .map { it.removePrefix("#") }
                //.map { "\"$it\"" } // 각 항목을 따옴표로 감싸기
                .toTypedArray()
        }
    }
}

