package com.example.homework

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
    lateinit var spinnerMenu : Spinner
    lateinit var spinnerSubMenu: Spinner
    lateinit var cl_personal_color : String
    private var maxItems : Int = 0
    private var category = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        var cl_category : Int?= 0

        //이미지 처리를 쉽게하려고 bitmap을 줄임
        val originalBitmap = BitmapStorage.Bitmap
        val originalWidth = originalBitmap!!.width
        val originalHeight = originalBitmap.height
        val newWidth = (originalWidth * 0.65).toInt()
        val newHeight = (originalHeight * 0.65).toInt()
        Log.d("현재 가로 /세로","$originalWidth , $originalHeight")
        val bitmap2 = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

        val processedBitmap = removeSemiTransparentPixels(bitmap2)
        val bitmap = scaleBitmapToOriginalSize(processedBitmap, originalBitmap.width, originalBitmap.height) // 원래 크기로 확대

        okButton = findViewById(R.id.okButton)
        colorTextView = findViewById(R.id.colorTextView)
        imageView = findViewById(R.id.imageCheck)
        imageView.setImageBitmap(bitmap)
        spinnerSubMenu = findViewById(R.id.spinnerSubMenu)

        okButton.setOnClickListener { // 저장 버튼
            cl_category = cl_category!!*100 + category
            Log.d("카테고리값",cl_category.toString())
            submitClothes(bitmap!!, cl_category!!)
        }


        GlobalScope.launch(Dispatchers.IO) { // 백그라운드 스레드에서 실행
            val (color, personalColorType) = decidePersonalColorFromImage(bitmap!!)
            //val closestColor = findClosestColor(Integer.toHexString(color))
            GlobalScope.launch(Dispatchers.Main) { // 메인 스레드에서 코루틴 시작


                // UI 업데이트
                colorTextView.setTextColor(color)
                Log.d("현재 color",Integer.toHexString(color))
                //Log.d("color의 가까운 색","${closestColor.name}")
                colorTextView.text = personalColorType
                cl_personal_color = personalColorType
            }
        }


        // 첫 번째 스피너 설정하는 과정
        spinnerMenu = findViewById(R.id.spinnerMenu)
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
                    0 -> 60
                    1 -> 10
                    2 -> 20
                    3 -> 30
                    4 -> 40
                    5 -> 50
                    6 -> 70
                    else -> 0
                }
                maxItems = when(position){ // 선택한 메뉴에 따른 최대 서브메뉴 수
                    0->3  // 가방
                    1->3  // 상의
                    2->3  // 하의
                    3->1  // 원피스
                    4->5  // 신발
                    5->1  // 악세서리
                    6->5  // 모자
                    else -> 0
                }

                // 서브 메뉴 리소스 배열 ID 설정
                val subMenuArrayId = when (position) {
                    0 -> R.array.sub_menu6 // 가방
                    1 -> R.array.sub_menu1 // 상의
                    2 -> R.array.sub_menu2 // 하의
                    3 -> R.array.sub_menu3 // 한벌옷
                    4 -> R.array.sub_menu4 // 신발
                    5 -> R.array.sub_menu5 // 악세서리
                    6 -> R.array.sub_menu7 // 모자
                    else -> 0 // 오류
                }

                // 선택된 메뉴에 따라 서브 메뉴 어댑터 설정
                if (subMenuArrayId != 0) {
                    val subMenuAdapter = ArrayAdapter.createFromResource(
                        this@CategoryActivity,
                        subMenuArrayId,
                        android.R.layout.simple_spinner_item
                    )
                    subMenuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerSubMenu.adapter = subMenuAdapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택되지 않았을 때
            }
        }


        spinnerSubMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                category = 0
                if (position < maxItems) {
                    // 유효한 항목이 선택된 경우
                    category = when(position){
                        0 -> 1
                        1 -> 2
                        2 -> 3
                        3 -> 4
                        4 -> 5
                        5 -> 6
                        7 -> 8
                        else -> 0
                    }

                } else {
                    // 유효하지 않은 항목이 선택된 경우
                    Log.e("서브메뉴 스피너 에러", "line 144")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 서브 메뉴에서 아무것도 선택되지 않았을 때의 처리
            }
        }


    }


    private fun submitClothes(bitmap : Bitmap, cl_category : Int) {
        val endPoint = "/uploadClothes"
        val url = ipAddr + endPoint
        val useremail = SharedPreferencesUtils.loadEmail(this).toString()
        uploadClothes(bitmap,cl_category,useremail, cl_personal_color ,url)
        finish()
    }

    //불투명도 날려버리기
    private fun removeSemiTransparentPixels(originalBitmap: Bitmap): Bitmap {
        // 결과를 저장할 동일한 크기의 Bitmap 생성
        val resultBitmap = Bitmap.createBitmap(originalBitmap.width, originalBitmap.height, Bitmap.Config.ARGB_8888)

        // 모든 픽셀을 순회하면서 처리
        for (x in 0 until originalBitmap.width) {
            for (y in 0 until originalBitmap.height) {
                val pixel = originalBitmap.getPixel(x, y)
                val alpha = Color.alpha(pixel)

                // 불투명도가 50% 미만인 경우, 픽셀을 완전 투명하게 설정
                if (alpha < 128) {
                    resultBitmap.setPixel(x, y, Color.TRANSPARENT)
                } else {
                    // 불투명도가 50% 이상인 경우, 픽셀을 완전 불투명하게 설정
                    resultBitmap.setPixel(x, y, Color.argb(255, Color.red(pixel), Color.green(pixel), Color.blue(pixel)))
                }
            }
        }

        return resultBitmap
    }


    // 이 함수를 사용하여 이미지의 불투명도를 조정한 후, 원래 크기로 확대합니다.
    private fun scaleBitmapToOriginalSize(processedBitmap: Bitmap, originalWidth: Int, originalHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(processedBitmap, originalWidth, originalHeight, true)
    }

}