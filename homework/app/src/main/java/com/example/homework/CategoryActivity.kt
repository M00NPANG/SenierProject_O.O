package com.example.homework

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryActivity : AppCompatActivity() {
    lateinit var imageView : ImageView
    lateinit var okButton : Button
    lateinit var colorTextView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        var cl_category : Int?= 0

        //이미지 처리를 쉽게하려고 bitmap을 줄임
        val originalBitmap = BitmapStorage.Bitmap
        val originalWidth = originalBitmap!!.width
        val originalHeight = originalBitmap.height
        val newWidth = (originalWidth * 0.6).toInt()
        val newHeight = (originalHeight * 0.6).toInt()
        val bitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

        okButton = findViewById(R.id.okButton)
        colorTextView = findViewById(R.id.colorTextView)
        imageView = findViewById(R.id.imageCheck)
        imageView.setImageBitmap(originalBitmap)


        GlobalScope.launch(Dispatchers.IO) { // 백그라운드 스레드에서 실행
            GlobalScope.launch(Dispatchers.Main) { // 메인 스레드에서 코루틴 시작
                val (color, personalColorType) = decidePersonalColorFromImage(bitmap!!)

                //logUniqueClosestCSSColorsForImage(bitmap, cssColors)
                // UI 업데이트
                colorTextView.setTextColor(color)
                colorTextView.text = personalColorType
            }
        }



        val spinnerMenu = findViewById<Spinner>(R.id.spinnerMenu)
        ArrayAdapter.createFromResource(
            this,
            R.array.menu_items,
            android.R.layout.simple_spinner_item // 스피너 아이템 레이아웃 지정
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // 드롭다운 뷰 레이아웃 지정
            spinnerMenu.adapter = adapter
        }

        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                cl_category = when(position){
                    0 -> 60     // 기타 잡화
                    1 -> 10     // 상의
                    2 -> 20     // 하의
                    3 -> 30     // 하의(기타)
                    4 -> 40     // 아우터
                    5 -> 50     // 신발
                    else -> 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택되지 않았을 때
            }
        }

        okButton.setOnClickListener {
            submitClothes(bitmap!!, cl_category!!)
        }

    }
    private fun submitClothes(bitmap : Bitmap, cl_category : Int) {
        val endPoint = "/uploadClothes"
        val url = ipAddr + endPoint
        val useremail = SharedPreferencesUtils.loadEmail(this).toString()
        uploadClothes(bitmap,cl_category,useremail,url)
        finish()
    }
}