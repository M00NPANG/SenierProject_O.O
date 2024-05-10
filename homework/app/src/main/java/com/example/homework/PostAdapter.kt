package com.example.homework

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope

class PostAdapter(private val scope: CoroutineScope) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val posts: List<List<Clothes>> = List(10) { ClothesRepository.getRandomClothesByCategory() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post, scope)

        // 안전한 접근을 위해 null 검사 및 예외 처리 추가
        val recyclerView = (holder.itemView.parent as? RecyclerView)
        recyclerView?.let {
            // 각 아이템의 크기를 동적으로 설정
            val parentWidth = it.width / 2 // 2열 그리드이므로 폭을 2로 나눔
            val layoutParams = holder.itemView.layoutParams
            layoutParams.width = parentWidth
            layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT // 높이는 내용에 맞춤
            holder.itemView.layoutParams = layoutParams
        }
    }

    override fun getItemCount(): Int = posts.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shirtImageView: ImageView = itemView.findViewById(R.id.shirt_image_view)
        private val pantsImageView: ImageView = itemView.findViewById(R.id.pants_image_view)
        private val onepieceImageView: ImageView = itemView.findViewById(R.id.onepiece_image_view)
        private val shoesImageView: ImageView = itemView.findViewById(R.id.shoes_image_view)
        private val etcImageView: ImageView = itemView.findViewById(R.id.etc_image_view)
        private val hatImageView: ImageView = itemView.findViewById(R.id.hat_image_view)

        fun bind(post: List<Clothes>, scope: CoroutineScope) {
            val idsList = mutableListOf<Int?>()

            post.forEach { clothes ->
                when (clothes.cl_category) {
                    in 1000..1999 , in 4000 .. 4999-> {loadImage(clothes.cl_photo_path, shirtImageView)}
                    in 2000..2999 -> loadImage(clothes.cl_photo_path, pantsImageView)
                    in 5000..5999 -> loadImage(clothes.cl_photo_path, shoesImageView)
                    in 6002..6005 -> loadImage(clothes.cl_photo_path, etcImageView)
                    6001 -> loadImage(clothes.cl_photo_path, hatImageView)
                }
                idsList.add(clothes.cl_id)
                }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, CodyRecomen::class.java).apply {
                    // null이 아닌 ID만 추출하여 IntArray로 변환
                    val idsArray = idsList.filterNotNull().toIntArray()
                    putExtra("clothesIds", idsArray)
                }
                context.startActivity(intent)
            }
        }

        private fun loadImage(url: String?, imageView: ImageView) {
            url?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .apply(RequestOptions().centerCrop()) // centerCrop 옵션으로 변경
                    .into(imageView) // ImageView 직접 로드
            }
        }
    }
}






