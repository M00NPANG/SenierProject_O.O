package com.example.homework

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

data class GridItem(val imageResId: Int)
data class ClothCategory(val title: String, val items: List<GridItem>)
//data class CodyGridItem(val imageResId: Int, val title: String)
data class CodyGridItem(val imageResId: Int? = null, val imagePath: String? = null, val title: String)

class ClosetActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2
    private lateinit var currentPhotoPath: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothesAdapter: ClothesAdapter
    private lateinit var gridLayoutCody: GridLayout
    private lateinit var clothButton : Button
    private lateinit var codyButton : Button

    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)
        clothButton = findViewById(R.id.cloth)
        codyButton = findViewById(R.id.cody)

        clothButton.isSelected = true
        //아이템용 리사이클러 뷰
        recyclerView = findViewById(R.id.recyclerViewClothes1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val categories = listOf(
            ClothCategory("모자", listOf(GridItem(R.drawable.cap2))),
            ClothCategory("상의", listOf(GridItem(R.drawable.shirt2), GridItem(R.drawable.hanbok))),
            ClothCategory("하의", listOf(GridItem(R.drawable.skirt2), GridItem(R.drawable.hanbok2), GridItem(R.drawable.hanbok2), GridItem(R.drawable.hanbok2), GridItem(R.drawable.hanbok2))),
            ClothCategory("신발", listOf(GridItem(R.drawable.shoes))))
        clothesAdapter = ClothesAdapter(categories)
        recyclerView.adapter = clothesAdapter

        // 옷 버튼 눌렀을 때
        clothButton.setOnClickListener {
            codyButtonSelect(1)

        }
        //코디 버튼 눌렀을 떄
        codyButton.setOnClickListener {
            codyButtonSelect(2)
        }

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        val fabGallery = findViewById<FloatingActionButton>(R.id.fab_gallery)
        val fabCamera = findViewById<FloatingActionButton>(R.id.fab_camera)

        fabAdd.setOnClickListener {
            toggleFabVisibility(fabGallery, fabCamera)
        }

        fabGallery.setOnClickListener {
            openGallery()
        }

        fabCamera.setOnClickListener {
            intent = Intent(this@ClosetActivity, TestActivity::class.java)
            startActivity(intent)
        }
    }
    private fun addCodyItemsToGridLayout(codyItems: List<CodyGridItem>) {
        codyItems.forEach { item ->
            val codyView = LayoutInflater.from(this).inflate(R.layout.item_post, gridLayoutCody, false)
            val imageView: ImageView = codyView.findViewById(R.id.uploadedImageView)
            val textView: TextView = codyView.findViewById(R.id.codyName)

            //imageView.setImageResource(item.imageResId)
            item.imagePath?.let { path ->
                val bitmap = BitmapFactory.decodeFile(path)
                imageView.setImageBitmap(bitmap)
            } ?: item.imageResId?.let { resId ->
                imageView.setImageResource(resId)
            }

            textView.text = item.title


            val layoutParams = GridLayout.LayoutParams()

            val pixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                resources.displayMetrics
            ).toInt()

            //layoutParams.width = pixels
            //layoutParams.height = pixels
            layoutParams.rightMargin = pixels
            layoutParams.bottomMargin = pixels

            gridLayoutCody.addView(codyView,layoutParams)
        }
    }

    private fun toggleFabVisibility(vararg fabs: FloatingActionButton) {
        fabs.forEach { fab ->
            fab.visibility = if (fab.visibility == FloatingActionButton.VISIBLE) {
                FloatingActionButton.INVISIBLE
            } else {
                FloatingActionButton.VISIBLE
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val bitmap: Bitmap? = when (requestCode) {
                TAKE_PHOTO_REQUEST -> {
                    // 카메라 사진 촬영 결과 처리
                    val photoFile = File(currentPhotoPath)
                    BitmapFactory.decodeFile(photoFile.absolutePath)
                }
                PICK_IMAGE_REQUEST -> {
                    // 갤러리에서 선택한 이미지 결과 처리
                    data?.data?.let { uri ->
                        contentResolver.openInputStream(uri).use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }
                    }
                }
                else -> null
            }

            bitmap?.let {
                // 서버 URL 설정
                val endPoint = "/testUpload"
                val url = ipAddr + endPoint
                // Bitmap을 서버로 업로드
                uploadBitmap(it, url)
            }
        }
    }


    private fun codyButtonSelect(num : Int){
        if(num == 1){
            clothButton.isSelected = true
            codyButton.isSelected = false
            val categories = listOf(
                ClothCategory("모자", listOf(GridItem(R.drawable.cap2))),
                ClothCategory("상의", listOf(GridItem(R.drawable.shirt2), GridItem(R.drawable.hanbok))),
                ClothCategory("하의", listOf(GridItem(R.drawable.skirt2), GridItem(R.drawable.hanbok2), GridItem(R.drawable.hanbok2), GridItem(R.drawable.hanbok2), GridItem(R.drawable.hanbok2))),
                ClothCategory("신발", listOf(GridItem(R.drawable.shoes))))
            clothesAdapter = ClothesAdapter(categories)
            recyclerView.adapter = clothesAdapter
            recyclerView.visibility = View.VISIBLE
            gridLayoutCody.visibility = View.GONE
        }
        else if(num == 2){
            val context: Context = this // 현재 Context
            val filename = "test_bitmap.png" // 내부 저장소에 저장된 파일 이름
            val imagePath = getInternalStorageImagePath(context, filename) // 파일 경로 얻기
            clothButton.isSelected = false
            codyButton.isSelected = true
            gridLayoutCody = findViewById(R.id.gridLayoutCody)
            val codyItems = listOf(
                CodyGridItem(R.drawable.test_cody, title ="아이템 제목 1"),
                CodyGridItem(R.drawable.test_cody, title = "아이템 제목 2"),
                CodyGridItem(R.drawable.test_cody, title = "아이템 제목 3"),
                CodyGridItem(title = filename, imagePath = imagePath)
                //CodyGridItem(imagePath = imagePath, title = "아이템 제목 4") // 객체 생성
                // 추가 아이템...
            )

            addCodyItemsToGridLayout(codyItems)
            recyclerView.visibility = View.GONE
            gridLayoutCody.visibility = View.VISIBLE
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // 이미지 파일 이름 생성
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // 파일: 경로를 저장합니다.
            currentPhotoPath = absolutePath
        }
    }

}

// 저장한 코디셋 접근용
fun getInternalStorageImagePath(context: Context, filename: String): String {
    return File(context.filesDir, filename).absolutePath
}

