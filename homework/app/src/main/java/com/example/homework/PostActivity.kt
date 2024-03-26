package com.example.homework

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import kotlin.math.sqrt

// 이미지 데이터와 위치 정보를 저장하기 위한 데이터 클래스


class PostActivity : AppCompatActivity() {
    // 선택된 이미지와 위치 정보를 저장할 리스트
    private val selectedImages = mutableListOf<ImageData>()
    lateinit var hatBtn: Button
    lateinit var shirtBtn : Button
    lateinit var pantsBtn : Button
    lateinit var shoesBtn : Button
    lateinit var gridView: GridView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)/*

        val capImg = arrayOf(R.drawable.cap,R.drawable.cap2)
        val shirtImg = arrayOf(R.drawable.shirt, R.drawable.hanbok,R.drawable.shirt2)
        val pantsImg = arrayOf(R.drawable.hanbok2, R.drawable.pant, R.drawable.skirt, R.drawable.skirt2)
        val shoesImg = arrayOf(R.drawable.shoes)
        gridView = findViewById(R.id.gridView)
        hatBtn = findViewById(R.id.hat)
        shirtBtn = findViewById(R.id.shirts)
        pantsBtn = findViewById(R.id.pants)
        shoesBtn = findViewById(R.id.shoes)

        hatBtn.setOnClickListener {
            hatBtn.isSelected = true
            shirtBtn.isSelected = false
            pantsBtn.isSelected = false
            shoesBtn.isSelected = false
            gridView.setOnItemClickListener { _, _, position, _ ->
                addImageToLayout(position,capImg)
            }
            gridView.adapter = ImageAdapter(this@PostActivity, capImg)
        }
        shirtBtn.setOnClickListener {
            hatBtn.isSelected = false
            shirtBtn.isSelected = true
            pantsBtn.isSelected = false
            shoesBtn.isSelected = false
            gridView.setOnItemClickListener { _, _, position, _ ->
                addImageToLayout(position,shirtImg)
            }
            gridView.adapter = ImageAdapter(this@PostActivity, shirtImg)
        }
        pantsBtn.setOnClickListener {
            hatBtn.isSelected = false
            shirtBtn.isSelected = false
            pantsBtn.isSelected = true
            shoesBtn.isSelected = false
            gridView.setOnItemClickListener { _, _, position, _ ->
                addImageToLayout(position,pantsImg)
            }
            gridView.adapter = ImageAdapter(this@PostActivity, pantsImg)
        }
        shoesBtn.setOnClickListener {
            hatBtn.isSelected = false
            shirtBtn.isSelected = false
            pantsBtn.isSelected = false
            shoesBtn.isSelected = true
        }
    }
    private fun addImageToLayout(position: Int, images: Array<Int>) {
        val imageData = ImageData(images[position])

        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setImageResource(imageData.resourceId)
            x = imageData.x
            y = imageData.y

            // 터치 리스너 설정
            setOnTouchListener(object : View.OnTouchListener {
                var dX: Float = 0f
                var dY: Float = 0f
                var lastEvent: Int = 0
                var downX: Float = 0f
                var downY: Float = 0f
                var startDistance: Float = 0f // 두 손가락 사이의 시작 거리
                var mode: Int = 0

                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when (event?.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            dX = view!!.x - event.rawX
                            dY = view.y - event.rawY
                            downX = event.rawX
                            downY = event.rawY
                            lastEvent = MotionEvent.ACTION_DOWN
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (event.pointerCount == 2 && mode == MotionEvent.ACTION_POINTER_DOWN) {
                                val newDist = distance(event)
                                if (newDist > 10f) {
                                    val scale = newDist / startDistance
                                    view?.scaleX = scale
                                    view?.scaleY = scale
                                }
                            } else if (mode != MotionEvent.ACTION_POINTER_DOWN) {
                                view!!.animate()
                                    .x(event.rawX + dX)
                                    .y(event.rawY + dY)
                                    .setDuration(0)
                                    .start()
                            }
                            lastEvent = MotionEvent.ACTION_MOVE
                        }
                        MotionEvent.ACTION_UP -> {
                            if (lastEvent == MotionEvent.ACTION_DOWN) {
                                // 이미지뷰 클릭 이벤트 처리
                            }
                        }
                        MotionEvent.ACTION_POINTER_DOWN -> {
                            mode = MotionEvent.ACTION_POINTER_DOWN
                            startDistance = distance(event) // 두 손가락 사이의 초기 거리 저장
                        }
                        MotionEvent.ACTION_POINTER_UP -> {
                            mode = 0
                        }
                    }
                    return true
                }

                private fun distance(event: MotionEvent): Float {
                    val dx = event.getX(0) - event.getX(1)
                    val dy = event.getY(0) - event.getY(1)
                    return sqrt(dx * dx + dy * dy)
                }
            })
        }

        (findViewById<ViewGroup>(R.id.view)).addView(imageView)
        selectedImages.add(imageData)
        */
    }




}



/*
private fun addImageToLayout(position: Int, images: Array<Int>) {
        val imageData = ImageData(images[position])
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setImageResource(imageData.resourceId)
            x = imageData.x
            y = imageData.y
            // 터치 리스너 설정
            setOnTouchListener(object : View.OnTouchListener {
                var dX: Float = 0f
                var dY: Float = 0f
                var lastEvent: Int = 0
                var downX: Float = 0f
                var downY: Float = 0f
                var startDistance: Float = 0f // 두 손가락 사이의 시작 거리
                var mode: Int = 0

                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when (event?.actionMasked) {
                        MotionEvent.ACTION_DOWN -> {
                            dX = view!!.x - event.rawX
                            dY = view.y - event.rawY
                            downX = event.rawX
                            downY = event.rawY
                            lastEvent = MotionEvent.ACTION_DOWN
                        }
                        MotionEvent.ACTION_MOVE -> {
                            //양손터치(크기 조절할 때)
                            if (event.pointerCount == 2 && mode == MotionEvent.ACTION_POINTER_DOWN) {
                                val newDist = distance(event)
                                if (newDist > 10f) {
                                    val scale = newDist / startDistance
                                    view?.scaleX = scale
                                    view?.scaleY = scale
                                }
                            } else if (event.pointerCount == 1) { // 한손터치(이미지 움직일 때)
                                val frameLayoutWidth = width // 프레임 레이아웃의 너비
                                val frameLayoutHeight = height // 프레임 레이아웃의 높이

                                val newX = event.rawX + dX
                                val newY = event.rawY + dY

                                // 이미지 뷰가 프레임 레이아웃 내에 있도록 경계 값을 계산
                                val boundedX = max(0f, min(newX, frameLayoutWidth - imageView.width))
                                val boundedY = max(0f, min(newY, frameLayoutHeight - imageView.height))

                                // 이미지 뷰 위치 업데이트
                                view!!.animate()
                                    .x(boundedX)
                                    .y(boundedY)
                                    .setDuration(0)
                                    .start()
                                /*view!!.animate()
                                    .x(event.rawX + dX)
                                    .y(event.rawY + dY)
                                    .setDuration(0)
                                    .start()*/
                            }
                            lastEvent = MotionEvent.ACTION_MOVE
                        }
                        MotionEvent.ACTION_UP -> {
                            if (lastEvent == MotionEvent.ACTION_DOWN) {
                                // 뷰에 올라간 이미지 클릭하면 작동
                            }
                        }
                        MotionEvent.ACTION_POINTER_DOWN -> {
                            mode = MotionEvent.ACTION_POINTER_DOWN
                            startDistance = distance(event) // 두 손가락 사이의 초기 거리 저장
                        }
                        MotionEvent.ACTION_POINTER_UP -> {
                            mode = 0
                        }
                    }
                    return true
                }

                private fun distance(event: MotionEvent): Float {
                    val dx = event.getX(0) - event.getX(1)
                    val dy = event.getY(0) - event.getY(1)
                    return sqrt(dx * dx + dy * dy)
                }
            })
        }

        (findViewById<ViewGroup>(R.id.view)).addView(imageView)
        selectedImages.add(imageData)
    }
 */