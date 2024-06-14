package com.example.homework

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class CodyRecomen : AppCompatActivity() {
    lateinit var shirtBox: FrameLayout
    lateinit var pantsBox: FrameLayout
    lateinit var shoesBox: FrameLayout
    lateinit var etcBox: FrameLayout
    lateinit var hatBox: FrameLayout
    lateinit var onepieceBox: FrameLayout
    lateinit var topImageUrl: String
    lateinit var bottomImageUrl: String
    lateinit var dressImageUrl: String
    lateinit var faceBox: FrameLayout
    lateinit var addBtn: ImageView
    var shoesImageUrl: String? = null
    var accessoriesImageUrl: String? = null
    var bagImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_recomen)

        initView()
        displaySelectedClothes()
    }

    private fun initView() {
        addBtn = findViewById(R.id.addCodiset)
        topImageUrl = intent.getStringExtra("topImageUrl").toString()
        bottomImageUrl = intent.getStringExtra("bottomImageUrl").toString()
        dressImageUrl = intent.getStringExtra("dressImageUrl").toString()
        Log.d("탑", topImageUrl)
        Log.d("바텀", bottomImageUrl)
        Log.d("드레스", dressImageUrl)
        shirtBox = findViewById(R.id.shirt_box)
        shirtBox.setOnClickListener {
            lifecycleScope.launch {
                showClothesDialog(topImageUrl)
            }
        }
        pantsBox = findViewById(R.id.pants_box)
        pantsBox.setOnClickListener {
            lifecycleScope.launch {
                showClothesDialog(bottomImageUrl)
            }
        }
        shoesBox = findViewById(R.id.shoes_box)
        shoesBox.setOnClickListener {
            lifecycleScope.launch {
                showClothesDialog(1)
            }
        }
        etcBox = findViewById(R.id.etc_box)
        etcBox.setOnClickListener {
            lifecycleScope.launch {
                showClothesDialog(2)
            }
        }
        hatBox = findViewById(R.id.hat_box)
        hatBox.setOnClickListener {
            lifecycleScope.launch {
                showClothesDialog(3)
            }
        }
        onepieceBox = findViewById(R.id.onepiece_box)
        /*onepieceBox.setOnClickListener{
                        lifecycleScope.launch{
                showClothesDialog(dressImageUrl)
            }

        }*/

        faceBox = findViewById(R.id.user_face)
        lifecycleScope.launch {
            val email = SharedPreferencesUtils.loadEmail(this@CodyRecomen)!!
            val faceUrl = getFaceUrl(email)  // 서버에서 얼굴 이미지 URL을 받아오는 함수

            withContext(Dispatchers.Main) {
                // 새로운 ImageView 생성
                val imageView = ImageView(this@CodyRecomen).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.gravity = Gravity.CENTER  // 이미지를 프레임 레이아웃의 중앙에 위치
                    }
                }
                // Glide를 사용하여 이미지 로드 후 ImageView에 설정
                Glide.with(this@CodyRecomen)
                    .load(faceUrl)
                    .into(imageView)

                // faceBox(FrameLayout)에 ImageView 추가
                faceBox.addView(imageView)
            }
        }
        addBtn.setOnClickListener {
            sendPostUpdateRequest()
        }
    }

    private fun displaySelectedClothes() {
        topImageUrl?.let { loadImage(it, shirtBox) }
        bottomImageUrl?.let { loadImage(it, pantsBox) }
        dressImageUrl?.let { loadImage(it, onepieceBox) }
    }

    private fun loadImage(url: String, container: FrameLayout) {
        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(this@CodyRecomen).load(url).apply(RequestOptions().fitCenter()).into(this)
        }
        container.addView(imageView)
    }

    //서버에서 옷 정보를 받아옴
    private suspend fun getClothInfo(imageUrl: String): Clothes? {
        Log.d("현재 이미지 url", imageUrl)
        var cloth: Clothes? = null
        val maxRetries = 3
        var currentRetry = 0

        while (currentRetry < maxRetries && cloth == null) {
            try {
                val url = "$ipAddr/clickClothes?imageUrl=$imageUrl"
                val request = Request.Builder().url(url).build()

                // OkHttpClient에 타임아웃 설정
                val client = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()

                // 비동기 요청
                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }

                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        cloth = Gson().fromJson(responseBody, Clothes::class.java)
                    }
                } else {
                    Log.e("NetworkError", "서버 응답 실패: ${response.message}, 코드: ${response.code}")
                }
            } catch (e: IOException) {
                Log.e("NetworkException", "네트워크 예외 발생: ${e.message}")
            } catch (e: JsonSyntaxException) {
                Log.e("NetworkException", "JSON 파싱 오류: ${e.message}")
            } catch (e: Exception) {
                Log.e("NetworkException", "기타 예외 발생: ${e.message}")
                e.printStackTrace()
            }

            if (cloth == null) {
                currentRetry++
                delay(2000) // 2초 대기 후 재시도
            }
        }
        Log.d("현재 clothes 객체 값", cloth?.toString() ?: "null")
        return cloth
    }


    // 옷 정보를 다이얼로그로 표시
    private fun showClothesDialog(cl_photo_path: String) {
        lifecycleScope.launch {
            val clothes = withContext(Dispatchers.IO) { getClothInfo(cl_photo_path) }
            if (clothes != null) {
                withContext(Dispatchers.Main) {
                    val dialogView = LayoutInflater.from(this@CodyRecomen)
                        .inflate(R.layout.clothes_dialog, null)
                    val dialogImageView = dialogView.findViewById<ImageView>(R.id.clothes_image)

                    Glide.with(this@CodyRecomen)
                        .load(clothes.cl_photo_path)
                        .into(dialogImageView)

                    val dialog = AlertDialog.Builder(this@CodyRecomen)
                        .setView(dialogView)
                        .show()

                    val addToCloset = dialogView.findViewById<TextView>(R.id.add_to_closet)
                    addToCloset.text = "장바구니에 담기"
                    addToCloset.setOnClickListener {
                        saveClothesToCart(clothes)
                        dialog.dismiss()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CodyRecomen,
                        "Failed to load clothes data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // 장바구니에 옷 추가
    private fun saveClothesToCart(clothes: Clothes) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = DatabaseClient.getDatabase(applicationContext)
                db.clothesDao().insertClothes(clothes)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CodyRecomen, "옷이 장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CodyRecomen, "이미 저장되어 있는 옷입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun fetchClothesData(input: Int): List<Clothes> {
        val email = SharedPreferencesUtils.loadEmail(this)  // 이메일 로드
        val url = when (input) {
            1 -> "$ipAddr/getShoes?email=$email"
            2 -> "$ipAddr/getEtc?email=$email"
            3 -> "$ipAddr/getHat?email=$email"
            else -> return emptyList()  // input이 유효하지 않을 경우 빈 리스트 반환
        }

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        return if (response.isSuccessful) {
            response.body?.string()?.let { responseBody ->
                Gson().fromJson(responseBody, Array<Clothes>::class.java).toList()
            } ?: emptyList()
        } else {
            Log.e("Network Error", "Failed to fetch data: ${response.message}")
            emptyList()
        }
    }


    fun showClothesDialog(input: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.choice_clothes)
        val gridView: GridView = dialog.findViewById(R.id.gridView)

        GlobalScope.launch {
            val clothesList = fetchClothesData(input)
            withContext(Dispatchers.Main) {
                gridView.adapter =
                    SelectClothesAdapter(this@CodyRecomen, clothesList, ::updateEtcBox)
            }
        }

        gridView.setOnItemClickListener { parent, view, position, id ->
            val selectedClothes = parent.adapter.getItem(position) as Clothes
            when (input) {
                1 -> updateShoesBox(selectedClothes)
                2 -> updateEtcBox(selectedClothes)
                3 -> updateHatBox(selectedClothes)
            }
            dialog.dismiss()  // 아이템을 선택하면 다이얼로그를 닫음
        }

        dialog.show()
    }


    fun updateEtcBox(selectedClothes: Clothes) {
        // 새로운 ImageView 생성
        val newImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        // Glide를 사용하여 이미지 로드
        Glide.with(this)
            .load(selectedClothes.cl_photo_path)
            .into(newImageView)


        // 기존 이미지 뷰 제거 후 새로 추가
        accessoriesImageUrl = selectedClothes.cl_photo_path
        etcBox.removeAllViews()
        etcBox.addView(newImageView)
    }

    fun updateHatBox(selectedClothes: Clothes) {
        // 새로운 ImageView 생성
        val newImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        // Glide를 사용하여 이미지 로드
        Glide.with(this)
            .load(selectedClothes.cl_photo_path)
            .into(newImageView)

        // 기존 이미지 뷰 제거 후 새로 추가
        bagImageUrl = selectedClothes.cl_photo_path
        hatBox.removeAllViews()
        hatBox.addView(newImageView)
    }

    fun updateShoesBox(selectedClothes: Clothes) {
        // 새로운 ImageView 생성
        val newImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        // Glide를 사용하여 이미지 로드
        Glide.with(this)
            .load(selectedClothes.cl_photo_path)
            .into(newImageView)

        // 기존 이미지 뷰 제거 후 새로 추가
        shoesImageUrl = selectedClothes.cl_photo_path
        shoesBox.removeAllViews()
        shoesBox.addView(newImageView)
    }

    private fun sendPostUpdateRequest() {
        val email = SharedPreferencesUtils.loadEmail(this)
        val encodedEmail = URLEncoder.encode(email, "UTF-8")  // 이메일 인코딩

        val url = "$ipAddr/api/storage/savePost" +
                "?email=$encodedEmail" +
                "&topUrl=${topImageUrl}" +
                "&bottomUrl=${bottomImageUrl}" +
                "&shoesUrl=${shoesImageUrl}" +
                "&accessoriesUrl=${accessoriesImageUrl}" +
                "&bagUrl=${bagImageUrl}"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .post(RequestBody.create(null, ""))  // POST 요청을 위해 빈 body 사용
                    .build()
                val client = OkHttpClient()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CodyRecomen,
                            "Post updated successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("NetworkError", "Failed to update post: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("NetworkException", "Error updating post: ${e.message}")
            }
        }
    }
}


class SelectClothesAdapter(private val context: Context, private val dataSource: List<Clothes>, private val updateEtcBox: (Clothes) -> Unit) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: inflater.inflate(R.layout.item_clothes, parent, false)
        val clothes = getItem(position) as Clothes

        val imageView = view.findViewById<ImageView>(R.id.clothImage)

        Glide.with(context)
            .load(clothes.cl_photo_path)
            .into(imageView)

        // 다운로드 버튼 클릭 시 etcBox 업데이트

        return view
    }

}