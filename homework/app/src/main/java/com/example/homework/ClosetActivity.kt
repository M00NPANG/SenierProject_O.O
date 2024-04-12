package com.example.homework

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.content.Context
import android.content.Intent
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
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.eren.removebg.RemoveBg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.max

//data class GridItem(val imageResId: Int)
data class GridItem(val imageURL: String)
data class ClothCategory(val title: String, val items: MutableList<GridItem>)
//data class CodyGridItem(val imageResId: Int, val title: String)

class ClosetActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2
    private lateinit var currentPhotoPath: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothesAdapter: ClothesAdapter
    private lateinit var gridLayoutCody: GridLayout
    private lateinit var clothButton : Button
    private lateinit var codyButton : Button
    private lateinit var homeButton : ImageView
    private lateinit var storageButton : ImageView
    private lateinit var closetButton : ImageView
    private lateinit var bellButton : ImageView
    private lateinit var userButton : ImageView
    private lateinit var currentPhotoUri: Uri
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_CODE = 101
    lateinit var bitmap : Bitmap

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)
        initButtons()
        clothButton.isSelected = true
        //아이템용 리사이클러 뷰
        recyclerView = findViewById(R.id.recyclerViewClothes1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        codyButtonSelect(1)



        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        val fabGallery = findViewById<FloatingActionButton>(R.id.fab_gallery)
        val fabCamera = findViewById<FloatingActionButton>(R.id.fab_camera)
        val fabCodi = findViewById<FloatingActionButton>(R.id.fab_codi)

        fabAdd.setOnClickListener {
            adjustFabVisibilityBasedOnSelection(fabCamera, fabGallery, fabCodi)
        }

        fabGallery.setOnClickListener {
            openGallery()
        }

        fabCamera.setOnClickListener {
            intent = Intent(this@ClosetActivity, TestActivity::class.java)
            startActivity(intent)
        }
        fabCodi.setOnClickListener {
            intent = Intent(this@ClosetActivity, CreateOutfitActivity::class.java)
            startActivity(intent)
        }
    }
    private fun initButtons() {
        clothButton = findViewById(R.id.cloth)
        codyButton = findViewById(R.id.cody)
        homeButton = findViewById(R.id.home)
        closetButton = findViewById(R.id.closet)
        storageButton = findViewById(R.id.storage)
        bellButton = findViewById(R.id.bell)
        userButton = findViewById(R.id.user)
        gridLayoutCody = findViewById(R.id.gridLayoutCody)

        clothButton.setOnClickListener {
            codyButtonSelect(1)
        }

        codyButton.setOnClickListener {
            codyButtonSelect(2)
        }

        homeButton.setOnClickListener {
            // 홈 버튼 클릭 시의 동작
            finish()
        }

        closetButton.setOnClickListener {
            // 옷장 버튼 클릭 시의 동작
            val intent = Intent(this, ClosetActivity::class.java)
            finish()
            startActivity(intent)
        }

        storageButton.setOnClickListener {
            // 저장소 버튼 클릭 시의 동작
        }

        bellButton.setOnClickListener {
            // 알림 버튼 클릭 시의 동작
        }

        userButton.setOnClickListener {
            // 사용자 버튼 클릭 시의 동작
        }
    }


    private fun addCodyItemsToGridLayout(codyItems: List<CodyGridItem>) { // 본인이 만든 코디세트를 띄움
        codyItems.forEach { item ->
            val codyView = LayoutInflater.from(this).inflate(R.layout.item_post, gridLayoutCody, false)
            val imageView: ImageView = codyView.findViewById(R.id.uploadedImageView)
            val codiname: TextView = codyView.findViewById(R.id.codyName)
            val username: TextView = codyView.findViewById(R.id.userName)
            val hashtag: TextView = codyView.findViewById(R.id.tags)
            // 이미지 경로가 있으면 해당 경로로 이미지 로드
            item.imagePath?.let { path ->
                val fullPath = if (path.startsWith("http")) {
                    path // 이미 전체 URL인 경우 그대로 사용
                } else {
                    "$ipAddr$path" // 상대 경로인 경우 기본 URL과 결합
                }
                Glide.with(this).load(fullPath).into(imageView)
            } ?: run {
                // imagePath가 없는 경우, 기본 이미지 설정 또는 이미지 뷰를 숨김
                // 예: imageView.setImageResource(R.drawable.default_image)
            }

            codiname.text = item.title
            username.text= item.username
            hashtag.text = item.hashtag
            val layoutParams = GridLayout.LayoutParams()
            val pixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                resources.displayMetrics
            ).toInt()

            layoutParams.rightMargin = pixels
            layoutParams.bottomMargin = pixels

            gridLayoutCody.addView(codyView, layoutParams)
        }
    }

    suspend fun checkCategory(clothesList: List<Clothes>): List<ClothCategory> {
        ClothesRepository.clearClothesUrls()
        // 카테고리별 기본 아이템들 넣음
        val categories = mutableListOf(
            ClothCategory("패션 잡화", mutableListOf(GridItem("https://storage.googleapis.com/codiset/cap2.png"))),
            ClothCategory("상의", mutableListOf(GridItem("https://storage.googleapis.com/codiset/shirt2.png"))),
            ClothCategory("하의", mutableListOf(GridItem("https://storage.googleapis.com/codiset/skirt2.png"))),
            ClothCategory("한벌옷", mutableListOf(GridItem("https://storage.googleapis.com/codiset/59ae9d27-d63e-4334-bfa8-202c7da66c2d"))),
            ClothCategory("아우터", mutableListOf(GridItem("https://storage.googleapis.com/codiset/64fff404-69e0-4b6a-b0e8-d93a4e5a0a8b"))),
            ClothCategory("신발", mutableListOf(GridItem("https://storage.googleapis.com/codiset/ef0d2fbd-b232-4a19-ae59-917dba66ced7")))
        )

        // 서버에서 받은 데이터를 각 카테고리에 추가
        clothesList.forEach { cloth ->
            cloth.cl_category?.let { categoryId ->
                val categoryIndex = when {
                    categoryId.toString().startsWith("6") -> 0 // 패션 잡화
                    categoryId.toString().startsWith("1") -> 1 // 상의
                    categoryId.toString().startsWith("2") -> 2 // 하의
                    categoryId.toString().startsWith("3") -> 3 // 한벌옷
                    categoryId.toString().startsWith("4") -> 4 // 아우터
                    categoryId.toString().startsWith("5") -> 5 // 신발
                    else -> null
                }
                categoryIndex?.let { index ->
                    categories[index].items.add(GridItem(cloth.cl_photo_path ?: "Default URL for missing image"))
                    cloth.cl_photo_path?.let { ClothesRepository.addClothesUrl(it)
                    ClothesRepository.addClothesData(clothesList) }
                }
            }
        }

        // 카테고리별 기본 아이템을 추가함
        categories.forEach { category ->
            category.items.firstOrNull()?.let { gridItem ->
                ClothesRepository.addClothesUrl(gridItem.imageURL)
            }
        }

        return categories
    }





    private fun adjustFabVisibilityBasedOnSelection(fabCamera: FloatingActionButton, fabGallery: FloatingActionButton, fabCodi: FloatingActionButton) {
        if (clothButton.isSelected) {
            // clothButton이 선택된 상태일 때
            fabCamera.visibility = toggleVisibility(fabCamera.visibility)
            fabGallery.visibility = toggleVisibility(fabGallery.visibility)
            fabCodi.visibility = FloatingActionButton.INVISIBLE
        } else if (codyButton.isSelected) {
            // codyButton이 선택된 상태일 때
            fabCodi.visibility = toggleVisibility(fabCodi.visibility)
            fabCamera.visibility = FloatingActionButton.INVISIBLE
            fabGallery.visibility = FloatingActionButton.INVISIBLE
        }
    }

    private fun toggleVisibility(currentVisibility: Int): Int {
        return if (currentVisibility == FloatingActionButton.VISIBLE) FloatingActionButton.INVISIBLE else FloatingActionButton.VISIBLE
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
                    val photoFile = File(currentPhotoPath)
                    BitmapFactory.decodeFile(photoFile.absolutePath)
                }

                PICK_IMAGE_REQUEST -> {
                    // 갤러리에서 선택했을 때 처리
                    data?.data?.let { uri ->
                        getCorrectlyOrientedImage(this, uri)
                    }
                    /*
                    data?.data?.let { uri ->
                        contentResolver.openInputStream(uri).use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }
                    }*/
                }
                else -> null
            }
            bitmap?.let {
                lifecycleScope.launch {
                    val remover = RemoveBg(this@ClosetActivity)

                    var outputImage: Bitmap?

                    remover.clearBackground(it).collect { output ->
                        if (output != null) {
                            outputImage = output
                            BitmapStorage.Bitmap = outputImage
                            startActivity(Intent(this@ClosetActivity, CategoryActivity::class.java))
                        }
                    }

                }
            }
        }
    }


    private fun codyButtonSelect(num : Int){ // 옷/코디버튼 눌렀을 때
        val email : String = SharedPreferencesUtils.loadEmail(this).toString()
        if(num == 1){ // 옷 버튼이 눌렸을 때
            clothButton.isSelected = true
            codyButton.isSelected = false

            lifecycleScope.launch {
                val clothesList = receiveClothes(email) // 서버로부터 데이터 받아오기
                val categories = checkCategory(clothesList) // 데이터 분류

                withContext(Dispatchers.Main) {
                    // 분류된 데이터로 RecyclerView 업데이트
                    clothesAdapter = ClothesAdapter(categories)
                    recyclerView.adapter = clothesAdapter
                }
            }
            recyclerView.visibility = View.VISIBLE
            gridLayoutCody.visibility = View.GONE
        }
        else if(num == 2){ // 코디 버튼이 눌렸을 떄
            clothButton.isSelected = false
            codyButton.isSelected = true

            gridLayoutCody.removeAllViews() // 기존에 있는 뷰를 제거
            CoroutineScope(Dispatchers.Main).launch {
                val codyItems = receivePosts(email) // 서버로부터 데이터를 받아옴
                addCodyItemsToGridLayout(codyItems) // UI 업데이트
                recyclerView.visibility = View.GONE
                gridLayoutCody.visibility = View.VISIBLE
                Log.d("받은결과", codyItems.toString())
            }

        }
        else{
            Log.e("ClosetActivity Error","버튼이 정상적으로 눌리지 않음")
        }

    }

    // 갤러리에서 선택한 이미지의 URI로부터 Bitmap을 로딩하고, 필요한 회전 처리를 수행하는 함수
    private fun getCorrectlyOrientedImage(context: Context, photoUri: Uri): Bitmap? {
        // 이미지의 회전 정보를 얻기 위해 ExifInterface 사용
        val orientation = context.contentResolver.openInputStream(photoUri)?.use { inputStream ->
            val exifInterface = ExifInterface(inputStream)
            exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        } ?: ExifInterface.ORIENTATION_NORMAL

        // 회전 각도를 결정
        val rotationAngle = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        // Bitmap을 디코드하는 데 사용될 두 번째 InputStream
        val bitmap = context.contentResolver.openInputStream(photoUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }

        // 필요한 회전 처리 수행
        return if (rotationAngle != 0 && bitmap != null) {
            val matrix = Matrix().apply { postRotate(rotationAngle.toFloat()) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }



}
