package com.example.design

import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.text.TextWatcher
import android.text.Editable

class CreateOutfitActivity : AppCompatActivity() {

    // 뷰들에 대한 참조 변수
    private lateinit var topToolbar: LinearLayout
    private lateinit var outfitLayout: FrameLayout
    private lateinit var searchBar: EditText
    private lateinit var clothesGrid: GridView

    // 버튼들에 대한 참조 변수
    private lateinit var backButton: ImageView
    private lateinit var undoButton: ImageView
    private lateinit var redoButton: ImageView
    private lateinit var checkButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_outfit)

        // 뷰들을 초기화
        topToolbar = findViewById(R.id.topToolbar)
        outfitLayout = findViewById(R.id.outfitLayout)
        searchBar = findViewById(R.id.searchBar)
        clothesGrid = findViewById(R.id.clothesGrid)

        // 버튼들을 초기화
        backButton = findViewById(R.id.backButton)
        undoButton = findViewById(R.id.undoButton)
        redoButton = findViewById(R.id.redoButton)
        checkButton = findViewById(R.id.checkButton)

        // 버튼에 클릭 리스너 설정
        backButton.setOnClickListener {
            // 되돌아가기 로직
        }

        undoButton.setOnClickListener {
            // 실행 취소 로직
        }

        redoButton.setOnClickListener {
            // 다시 실행 로직
        }

        checkButton.setOnClickListener {
            // 확인 로직, 다음 화면으로 이동
        }

        // GridView에 어댑터 설정
       // clothesGrid.adapter = /* 어댑터 인스턴스 */

                // GridView의 아이템 클릭 리스너 설정
           // clothesGrid.setOnItemClickListener { parent, view, position, id ->
                // 아이템 클릭 시 수행될 로직
           // }

        // 검색창 이벤트 리스너 설정
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 시
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후
                // 검색 필터 로직, GridView 업데이트
            }
        })
    }
}
