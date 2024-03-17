package com.example.design

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CreatePostActivity : AppCompatActivity() {
    private lateinit var hashtagEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val backButton: ImageView = findViewById(R.id.backButton)
        val completeButton: ImageView = findViewById(R.id.completeButton)
        hashtagEdit = findViewById(R.id.hashtagEdit)

        backButton.setOnClickListener { onBackPressed() }
        completeButton.setOnClickListener { submitPost() }

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
        // 게시물을 제출하는 로직을 여기에 구현
    }
}
