package com.example.homework

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class CreatePostActivity : AppCompatActivity() {
    private lateinit var codiImage : ImageView // 코디 이미지
    private lateinit var hashtagEdit: EditText // 해시태그
    private lateinit var codiNameEdit : EditText  // 제목
    private lateinit var commentEdit : EditText // 내용
    private lateinit var codiPath : String // 코디 주소
    private val hashtagsSet = HashSet<String>() // 해시태그를 관리하기 위한 HashSet

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
        completeButton.setOnClickListener {
            //submitPost()
            val styleInstance = style(
                sty_id = null, // 스타일 ID는 여기서 null로 설정하거나 적절히 생성/할당
                sty_street = if (hashtagsSet.contains("#스트릿")) 1 else 0,
                sty_modern = if (hashtagsSet.contains("#모던")) 1 else 0,
                sty_minimal = if (hashtagsSet.contains("#미니멀")) 1 else 0,
                sty_feminine = if (hashtagsSet.contains("#페미닌")) 1 else 0,
                sty_simpleBasic = if (hashtagsSet.contains("#심플 베이직")) 1 else 0,
                sty_americanCasual = if (hashtagsSet.contains("#아메카지")) 1 else 0,
                sty_businessCasual = if (hashtagsSet.contains("#비즈니스 캐주얼")) 1 else 0,
                sty_casual = if (hashtagsSet.contains("#캐주얼")) 1 else 0,
                sty_retro = if (hashtagsSet.contains("#레트로")) 1 else 0,
                sty_classic = if (hashtagsSet.contains("#클래식")) 1 else 0,
                sty_elegant = if (hashtagsSet.contains("#엘레강스")) 1 else 0,
                sty_girlish = if (hashtagsSet.contains("#걸리쉬")) 1 else 0,
                sty_tomboy = if (hashtagsSet.contains("#톰보이")) 1 else 0,
                sty_vintage = if (hashtagsSet.contains("#빈티지")) 1 else 0,
                user_id = null
            )
            Log.d("StyleInstance", styleInstance.toString())
        }

        val imagePath = intent.getStringExtra("imagePath")
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            // ImageView에 Bitmap 설정
            codiImage.setImageBitmap(bitmap)
            codiPath = imagePath
        }

        val hashtags = listOf("#스트릿","#모던","#미니멀", "#페미닌", "#심플 베이직", "#아메카지", "#비즈니스 캐주얼","#캐주얼","#레트로","#스포츠","#클래식","#엘레강스","#걸리쉬","#톰보이","#빈티지")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, hashtags)
        val autoText: AutoCompleteTextView = findViewById(R.id.hashtagEdit)
        autoText.setAdapter(adapter)

        // 사용자가 항목을 선택할 때 실행할 동작을 설정합니다.
        autoText.setOnItemClickListener { parent, view, position, id ->
            val selectedTag = parent.getItemAtPosition(position).toString()

            // 선택된 태그를 Chip으로 변환하여 ChipGroup에 추가
            addChipToGroup(selectedTag)
            autoText.text.clear() // 태그 선택 후 AutoCompleteTextView를 없앰
        }

    }

    private fun submitPost() {
        val endPoint = "/uploadCodi"
        val url = ipAddr + endPoint
        val hasttag = hashtagEdit.text.toString()
        val comment = commentEdit.text.toString()
        val title = codiNameEdit.text.toString()
        val bitmap = BitmapFactory.decodeFile(codiPath)
        val useremail = SharedPreferencesUtils.loadEmail(this).toString()

        uploadCodiSet(bitmap,title,hasttag,comment,useremail,url)
        //finish()
        val intent = Intent(this, LobbyActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
    }

    private fun addChipToGroup(tag: String) {
        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        if (!hashtagsSet.contains(tag)) {
            hashtagsSet.add(tag) // 해시태그를 HashSet에 추가합니다.
            val chip = Chip(this).apply {
                text = tag
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    // Chip의 닫기 아이콘을 클릭하면 ChipGroup에서 해당 Chip을 제거하고 HashSet에서도 제거합니다.
                    chipGroup.removeView(this)
                    hashtagsSet.remove(tag)
                }
            }
            chipGroup.addView(chip)

        }
    }
    private fun removeChipFromGroup(tag: String) {
        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        if (hashtagsSet.contains(tag)) {
            val chipToRemove = chipGroup.children.find { (it as Chip).text == tag }
            chipGroup.removeView(chipToRemove)
            hashtagsSet.remove(tag)
            Log.d("CurrentHashtags", hashtagsSet.toString())
        }
    }

}


