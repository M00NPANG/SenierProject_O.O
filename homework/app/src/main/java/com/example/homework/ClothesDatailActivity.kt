package com.example.homework

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ClothesDatailActivity : AppCompatActivity() {
    lateinit var cl_idView : TextView
    lateinit var gridView : GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes_datail) // 창 크기 조절
        cl_idView = findViewById(R.id.cl_idViewer)
        gridView = findViewById(R.id.grid_view)
        // 화면 높이의 70%를 차지하도록 창 크기 조절
        val metrics = resources.displayMetrics
        val height = (metrics.heightPixels * 0.9).toInt() // 높이만 조절

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height)
        window.setGravity(Gravity.TOP)

        // Intent에서 cl_category 값 가져오기
        val cl_category = intent.getIntExtra("CL_ID", 0) // 기본값으로 0을 사용
        cl_idView.setText(cl_category.toString())
        val email = SharedPreferencesUtils.loadEmail(this)
        lifecycleScope.launch {
            val clothesList = receiveRecommendedClothes(email!!,cl_category)
            val adapter = CustomAdapter(this@ClothesDatailActivity, clothesList)
            gridView.adapter = adapter
        }

    }
}

class CustomAdapter(private val context: Context, private val items: List<Clothes>) : BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_clothes, parent, false)

        val item = items[position]
        val imageView = itemView.findViewById<ImageView>(R.id.clothImage)

        //옷 이미지 보여주는 이미지뷰 설정 부분
        val clothImageView = itemView.findViewById<ImageView>(R.id.clothImage)
        // Glide를 사용하여 이미지 로드
        Glide.with(context)
            .load(item.cl_photo_path) // URL이나 파일 경로
            .placeholder(R.drawable.roading) // 로딩 중에 표시될 이미지
            .error(R.drawable.error) // 로드 실패 시 표시될 이미지
            .into(imageView) // 이미지를 로드할 대상 ImageView
        clothImageView.setOnClickListener {
            Toast.makeText(context, "Cloth Image Clicked!", Toast.LENGTH_SHORT).show()
        }

        /*// 다운로드 아이콘 ImageView
        val downloadImageView = itemView.findViewById<ImageView>(R.id.download_img) // xml 파일에 정의된 다운로드 아이콘 ImageView의 id를 사용하세요
        // 다운로드 아이콘 클릭 리스너 설정
        downloadImageView.setOnClickListener {
            // 여기에 클릭 시 수행할 다운로드 관련 작업 작성
            Toast.makeText(context, "Download Icon Clicked!", Toast.LENGTH_SHORT).show()
        }*/
        return itemView
    }
}


