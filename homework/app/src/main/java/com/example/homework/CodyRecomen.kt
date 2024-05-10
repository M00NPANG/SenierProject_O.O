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
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CodyRecomen : AppCompatActivity() {
    lateinit var shirtBox: FrameLayout
    lateinit var pantsBox: FrameLayout
    lateinit var shoesBox: FrameLayout
    lateinit var etcBox: FrameLayout
    lateinit var hatBox: FrameLayout
    lateinit var onepieceBox : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_recomen)

        initView()
        val clothesIds = intent.getIntArrayExtra("clothesIds") ?: intArrayOf()
        displaySelectedClothes(clothesIds)
    }

    private fun initView() {
        shirtBox = findViewById(R.id.shirt_box)
        pantsBox = findViewById(R.id.pants_box)
        shoesBox = findViewById(R.id.shoes_box)
        etcBox = findViewById(R.id.etc_box)
        hatBox = findViewById(R.id.hat_box)
        onepieceBox = findViewById(R.id.onepiece_box)
    }

    private fun displaySelectedClothes(clothesIds: IntArray) {
        clothesIds.forEach { id ->
            ClothesRepository.getClothesById(id)?.let { clothes ->
                val imageView = ImageView(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    Glide.with(this@CodyRecomen).load(clothes.cl_photo_path).into(this)

                    // Set a click listener for logging and showing details
                    setOnClickListener {
                        logClothesCategory(clothes)
                        showClothesDialog(clothes)
                    }
                }
                addImageViewToLayout(clothes, imageView)
            }
        }
    }

    private fun showClothesDialog(clothes: Clothes) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.clothes_dialog, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()

        val dialogImageView = dialogView.findViewById<ImageView>(R.id.clothes_image)
        Glide.with(this).load(clothes.cl_photo_path).into(dialogImageView)

        val addToCloset = dialogView.findViewById<TextView>(R.id.add_to_closet)
        addToCloset.text = "장바구니에 담기"
        addToCloset.setOnClickListener {
            saveClothesToCart(clothes)
            dialog.dismiss()
        }
    }

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

    private fun logClothesCategory(clothes: Clothes) {
        val category = when (clothes.cl_category) {
            in 1000..1999, in 4000..4999 -> "Shirt"
            in 2000..2999 -> "Pants"
            in 3000..3999 -> "Onepiece"
            in 5000..5999 -> "Shoes"
            6001 -> "Hat"
            in 6002..6005 -> "Etc"
            else -> "Unknown"
        }
        Log.d("CategoryLog", "$category clicked: ${clothes.cl_name}")
    }

    private fun addImageViewToLayout(clothes: Clothes, imageView: ImageView) {
        when (clothes.cl_category) {
            in 1000..1999, in 4000..4999 -> shirtBox.addView(imageView)
            in 2000..2999 -> pantsBox.addView(imageView)
            in 3000..3999 -> onepieceBox.addView(imageView)
            in 5000..5999 -> shoesBox.addView(imageView)
            6001 -> hatBox.addView(imageView)
            in 6002..6005 -> etcBox.addView(imageView)
        }
    }
}

