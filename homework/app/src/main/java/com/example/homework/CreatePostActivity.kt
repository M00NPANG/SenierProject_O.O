package com.example.homework

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CreatePostActivity : AppCompatActivity() {
    private lateinit var codiImage : ImageView // 코디 이미지
    private lateinit var hashtagEdit: EditText // 해시태그
    private lateinit var codiNameEdit : EditText  // 제목
    private lateinit var commentEdit : EditText // 내용
    private lateinit var codiPath : String // 코디 주소

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        codiImage = findViewById(R.id.codiImage)
        codiNameEdit = findViewById(R.id.codiNameEdit)
        commentEdit = findViewById(R.id.codiDescriptionEdit)
        hashtagEdit = findViewById(R.id.hashtagEdit)

        val backButton: ImageView = findViewById(R.id.backButton)
        val completeButton: ImageView = findViewById(R.id.completeButton)


        backButton.setOnClickListener { onBackPressed() }
        completeButton.setOnClickListener { submitPost() }

        val imagePath = intent.getStringExtra("imagePath")
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            // ImageView에 Bitmap 설정
            codiImage.setImageBitmap(bitmap)
            codiPath = imagePath
        }



        hashtagEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 해시태그 입력 전 처리 로직
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 해시태그 처리 로직을 여기에 추가
            }

            override fun afterTextChanged(s: Editable?) {
                // 해시태그 입력 후 처리 로직
            }
        })
    }

    private fun submitPost() {
        val endPoint = "/testCodiUpload"
        val url = ipAddr + endPoint
        val hasttag = hashtagEdit.text.toString()
        val comment = commentEdit.text.toString()
        val title = codiNameEdit.text.toString()
        val bitmap = BitmapFactory.decodeFile(codiPath)
        val username = "ChunSamKim" // 나중에 현재 사용자가 누구인지 알아내는 로직 만들기. 지금은 임시이름

        uploadCodiSet(bitmap,title,hasttag,comment,username,url)
        finish()
    }
}