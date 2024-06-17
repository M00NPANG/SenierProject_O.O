package com.example.homework

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodyRecomen2 : AppCompatActivity() {
    lateinit var shirtBox: FrameLayout
    lateinit var pantsBox: FrameLayout
    lateinit var shoesBox: FrameLayout
    lateinit var etcBox: FrameLayout
    lateinit var hatBox: FrameLayout
    lateinit var onepieceBox: FrameLayout
    lateinit var faceBox: FrameLayout

    var topImageUrl: String? = null
    var bottomImageUrl: String? = null
    var dressImageUrl: String? = null
    var shoesImageUrl: String? = null
    var accessoriesImageUrl: String? = null
    var bagImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_recomen2)

        initView()
    }

    // 초기화
    private fun initView() {
        topImageUrl = intent.getStringExtra("topImageUrl")
        bottomImageUrl = intent.getStringExtra("bottomImageUrl")
        dressImageUrl = intent.getStringExtra("dressImageUrl")
        shoesImageUrl = intent.getStringExtra("shoesImageUrl")
        accessoriesImageUrl = intent.getStringExtra("accessoriesImageUrl")
        bagImageUrl = intent.getStringExtra("bagImageUrl")

        shirtBox = findViewById(R.id.shirt_box)
        pantsBox = findViewById(R.id.pants_box)
        onepieceBox = findViewById(R.id.onepiece_box)
        shoesBox = findViewById(R.id.shoes_box)
        etcBox = findViewById(R.id.etc_box)
        hatBox = findViewById(R.id.hat_box)
        faceBox = findViewById(R.id.user_face)

        // 각 이미지 박스에 이미지를 설정하는 함수 호출
        loadImageIntoBox(topImageUrl, shirtBox)
        loadImageIntoBox(bottomImageUrl, pantsBox)
        loadImageIntoBox(dressImageUrl, onepieceBox)
        loadImageIntoBox(shoesImageUrl, shoesBox)
        loadImageIntoBox(accessoriesImageUrl, etcBox)
        loadImageIntoBox(bagImageUrl, hatBox)

        lifecycleScope.launch {
            val email = SharedPreferencesUtils.loadEmail(this@CodyRecomen2)!!
            val faceUrl = getFaceUrl(email)  // 서버에서 얼굴 이미지 URL을 받아오는 함수

            withContext(Dispatchers.Main) {
                // 새로운 ImageView 생성
                val imageView = ImageView(this@CodyRecomen2).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.gravity = Gravity.CENTER  // 이미지를 프레임 레이아웃의 중앙에 위치
                    }
                }
                // Glide를 사용하여 이미지 로드 후 ImageView에 설정
                Glide.with(this@CodyRecomen2)
                    .load(faceUrl)
                    .into(imageView)

                // faceBox(FrameLayout)에 ImageView 추가
                faceBox.addView(imageView)
            }
        }
    }

    // 이미지 URL을 받아 해당 FrameLayout에 이미지를 설정하는 함수
    private fun loadImageIntoBox(imageUrl: String?, box: FrameLayout) {
        imageUrl?.let {
            val imageView = ImageView(this).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ).also {
                    it.gravity = Gravity.CENTER
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            Glide.with(this)
                .load(it)
                .into(imageView)
            box.addView(imageView)
        }
    }
}
