package com.example.homework

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

class PostAdapter(private val context: Context, private val scope: CoroutineScope, private val tempStatus: Int,private val buttonId : Int) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var posts = mutableListOf<OutfitDTO>()
    private val client = OkHttpClient()

    init {
        fetchOutfits()
    }

    private fun fetchOutfits() {
        val temp: Long = tempStorage.getTemp()
        val email = SharedPreferencesUtils.loadEmail(context)
        val master = "ccc@ccc.com"
        val encodeMaster = URLEncoder.encode(master,"UTF-8")
        val encodedEmail = URLEncoder.encode(email, "UTF-8")
        val url = when (buttonId) {
            1 -> when (tempStatus) {  // 내거
                0 -> "$ipAddr/api/storage/codiSet?email=$encodedEmail"
                1 -> "$ipAddr/api/storage/Temperature?email=$encodedEmail&temperature=$temp"
                else -> "error"
            }
            2 -> when (tempStatus) {  // MIX
                0 -> "$ipAddr/api/storage/codiSetMix?email=$encodedEmail"
                1 -> "$ipAddr/api/storage/TemperatureMix?email=$encodedEmail&temperature=$temp"
                else -> "error"
            }
            3 -> when(tempStatus){   // SHOP
                0 ->"$ipAddr/api/storage/codiSet?email=$encodeMaster"
                1 -> "$ipAddr/api/storage/TemperatureAdmin?email=$encodedEmail&temperature=$temp"
                else -> "error"
            }
            else -> "$ipAddr/api/storage/codiSet?email=$encodedEmail"
        }

        Log.d("PostAdapter", "Fetching outfits from URL: $url")

        scope.launch {
            try {
                val outfits = fetchOutfitsFromServer(url)
                Log.d("서버로부터 받은 아웃핏", outfits.toString())
                withContext(Dispatchers.Main) {
                    posts.clear()
                    posts.addAll(outfits)
                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("PostAdapter", "Error fetching outfits", e)
            }
        }
    }


    private suspend fun fetchOutfitsFromServer(url: String): List<OutfitDTO> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string()?.let { jsonString ->
                return@withContext Gson().fromJson(jsonString, Array<OutfitDTO>::class.java).toList()
            } ?: emptyList()
        } else {
            throw Exception("Failed to fetch data: ${response.message}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val outfit = posts[position]
        holder.bind(outfit)
    }

    override fun getItemCount() = posts.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shirtImageView: ImageView = itemView.findViewById(R.id.shirt_image_view)
        private val pantsImageView: ImageView = itemView.findViewById(R.id.pants_image_view)
        private val onepieceImageView: ImageView = itemView.findViewById(R.id.onepiece_image_view)

        fun bind(outfit: OutfitDTO) {
            outfit.topImageUrl?.let { loadImage(it, shirtImageView) }
            outfit.bottomImageUrl?.let { loadImage(it, pantsImageView) }
            outfit.dressImageUrl?.let { loadImage(it, onepieceImageView) }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, CodyRecomen::class.java).apply {
                    putExtra("topImageUrl", outfit.topImageUrl)
                    putExtra("bottomImageUrl", outfit.bottomImageUrl)
                    putExtra("dressImageUrl", outfit.dressImageUrl)
                }
                context.startActivity(intent)
            }
        }

        private fun loadImage(url: String, imageView: ImageView) {
            Glide.with(itemView.context)
                .load(url)
                .apply(RequestOptions().centerCrop())
                .into(imageView)
        }
    }
}

/*

class PostAdapter(private val context: Context, private val scope: CoroutineScope) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var posts = listOf<OutfitDTO>()
    private val client = OkHttpClient()

    init {
        fetchOutfits()
    }

    private fun fetchOutfits() {
        val email = SharedPreferencesUtils.loadEmail(context)
        val url = "$ipAddr/api/storage/codiSetMix?email=$email"

        scope.launch {
            try {
                val outfits = fetchOutfitsFromServer(url)
                withContext(Dispatchers.Main) {
                    posts = outfits
                    Log.d("현재 받은 outfit들",posts.toString())
                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("PostAdapter", "Error fetching outfits", e)
            }
        }
    }

    private suspend fun fetchOutfitsFromServer(url: String): List<OutfitDTO> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            response.body?.string()?.let { jsonString ->
                return@withContext Gson().fromJson(jsonString, Array<OutfitDTO>::class.java).toList()
            } ?: throw IllegalStateException("No data received from the server")
        } else {
            throw RuntimeException("Failed to fetch data: ${response.message}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val outfit = posts[position]
        holder.bind(outfit)
    }

    override fun getItemCount() = posts.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shirtImageView: ImageView = itemView.findViewById(R.id.shirt_image_view)
        private val pantsImageView: ImageView = itemView.findViewById(R.id.pants_image_view)
        private val onepieceImageView: ImageView = itemView.findViewById(R.id.onepiece_image_view)

        fun bind(outfit: OutfitDTO) {
            outfit.topImageUrl?.let { loadImage(it, shirtImageView) }
            outfit.bottomImageUrl?.let { loadImage(it, pantsImageView) }
            outfit.dressImageUrl?.let { loadImage(it, onepieceImageView) }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, CodyRecomen::class.java).apply {
                    putExtra("topImageUrl", outfit.topImageUrl)
                    putExtra("bottomImageUrl", outfit.bottomImageUrl)
                    putExtra("dressImageUrl", outfit.dressImageUrl)
                }
                context.startActivity(intent)
            }
        }

        private fun loadImage(url: String, imageView: ImageView) {
            Glide.with(itemView.context)
                .load(url)
                .apply(RequestOptions().centerCrop())
                .into(imageView)
        }
    }
}


 */