package com.example.homework

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.homework.ClothesRepository.allClothesUrls
import kotlin.properties.Delegates

class CoditestActivity : AppCompatActivity() {
    var count = 0
    //framelayout위의 아이템들
    private val selectedImages = mutableListOf<ImageData>()
    // 뷰들에 대한 참조 변수
    private lateinit var topToolbar: LinearLayout
    private lateinit var frameview: FrameLayout
    private lateinit var searchBar: EditText
    private lateinit var gridView: GridView

    // 버튼들에 대한 참조 변수
    private lateinit var backButton: ImageView
    private lateinit var undoButton: ImageView
    private lateinit var redoButton: ImageView
    private lateinit var checkButton: ImageView

    //프레임레이아웃 가로세로
    private var F_width by Delegates.notNull<Int>()
    private var F_height by Delegates.notNull<Int>()

    lateinit var Img : Array<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coditest)
        // 뷰들을 초기화
        topToolbar = findViewById(R.id.topToolbar)
        frameview = findViewById(R.id.view)
        searchBar = findViewById(R.id.searchBar)
        gridView = findViewById(R.id.clothesGrid)

        // 버튼들을 초기화
        backButton = findViewById(R.id.backButton)
        undoButton = findViewById(R.id.undoButton)
        redoButton = findViewById(R.id.redoButton)
        checkButton = findViewById(R.id.checkButton)

        setItems()
        ClothesRepository.printAllClothes()
        gridView.setOnItemClickListener { _, _, position, _ ->
            addImageToLayout(position)
        }
        //gridView.adapter = ImageAdapter(this@CreateOutfitActivity, Img)
        val imageUrls = ClothesRepository.returnUrls()
        gridView.adapter = ImageAdapter(this@CoditestActivity, imageUrls)

        backButton.setOnClickListener {
            deleteAllItems(frameview) // 모든 아이템 제거
            count = 0
            Log.d("count",count.toString())
        }

        undoButton.setOnClickListener {
            deleteLastItems(frameview) // 마지막 아이템 제거
            Log.d("count",count.toString())
        }

        redoButton.setOnClickListener {
            frameview.removeAllViews() // 기존에 배치된 모든 뷰를 제거
            addRandomClothes()

        }

        checkButton.setOnClickListener {
            /*
            val list = findMatchingClothes(selectedImages) // 선택된 이미지들의 리스트
            ClothesRepository.addMatchedClothes(list)
            Log.d("현재 리스트",selectedImages.toString())
            // FrameLayout을 캡쳐하여 Bitmap으로 변환
            val bitmap = getBitmapFromView(frameview)

            val imagePath = saveImageToInternalStorage(this@CoditestActivity, bitmap, "testimg.png") // 내부 저장소에 이미지 저장

            // Intent를 사용하여 다음 액티비티로 이미지 경로 전달
            val intent = Intent(this@CoditestActivity, CreatePostActivity::class.java).apply {
                putExtra("imagePath", imagePath)

            }

            startActivity(intent)*/
        }

        // 프레임레이아웃(frameview로 이름 바꿨음)의 가로/세로 측정
        frameview.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                F_width = frameview.width
                F_height = frameview.height
            }
        })
    }
    private fun addImageToLayout(position: Int) {
        // 선택된 이미지의 URL을 사용하여 ImageData 객체 생성
        val imageUrl = allClothesUrls[position]
        val imageData = ImageData(imageUrl)
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // Glide를 사용하여 네트워크에서 이미지 로드
            Glide.with(this@CoditestActivity)
                .load(imageUrl)
                .into(this)

            tag = imageData // ImageView와 ImageData 인스턴스 연결

        }
        ClothesRepository.clothesData.firstOrNull { it.cl_photo_path == imageUrl }?.let { matchedClothes ->
            val (xPos, yPos) = determinePosition(matchedClothes.cl_category)
            val (width, height) = determineSize(matchedClothes.cl_category)

            imageView.x = xPos
            imageView.y = yPos
            if(matchedClothes.cl_category!!  >= 6000 && count == 0){
                count++
                Log.d("count",count.toString())
            }
            else if(matchedClothes.cl_category!!  >= 6000 && count >= 1 && count < 4){
                imageView.y = yPos + 200*count
                count++
                Log.d("count",count.toString())
            }
            else if((matchedClothes.cl_category!!  >= 6000 && count == 4)){
                Log.d("count",count.toString())
                return
            }
            imageView.layoutParams = ViewGroup.LayoutParams(width, height)
        }


        (findViewById<ViewGroup>(R.id.view)).addView(imageView)
        selectedImages.add(imageData)
    }

    //각 카테고리값에 따라 위치 및 크기 조정
    private fun determinePosition(category: Int?): Pair<Float, Float> {

        return when (category) {
            in 1000..1999 -> Pair(450f, 50f)   // 상의에 대한 위치
            in 2000..2999 -> Pair(450f, 350f)  // 하의 20~29에 대한 위치
            in 3000..3999 -> Pair(200f, 20f)  // 한벌옷 30~39에 대한 위치
            in 4000..4999 -> Pair(450f, 50f)  // 아우터 40~49에 대한 위치
            in 5000..5999 -> Pair(450f, 720f)  // 신발 50~59에 대한 위치
            in 6000..6999 -> Pair(100f, 50f)  // 패션 잡화 60~69에 대한 위치
            else -> Pair(0f, 0f)          // 기본 위치
        }
    }
    private fun determineSize(category: Int?): Pair<Int, Int> {
        val width = when (category) {
            in 1000..1999 -> 400   // 상의
            in 2000..2999 -> 400   // 하의
            in 3000..3999 -> 850   // 한벌옷
            in 4000..4999 -> 400   // 아우터
            in 5000..5999 -> 400   // 신발
            in 6000..6999 -> 400   // 패션잡화
            else -> 100
        }
        val height = when (category) {
            in 1000..1999 -> 400   // 상의
            in 2000..2999 -> 500   // 하의
            in 3000..3999 -> 850   // 한벌옷
            in 4000..4999 -> 400   // 아우터
            in 5000..5999 -> 400   // 신발
            in 6000..6999 -> 400   // 패션잡화
            else -> 100
        }
        return Pair(width, height)
    }

    // FrameLayout의 현재 상태를 Bitmap으로 변환하는 함수. 이걸 서버로 보내서 저장
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
    private fun deleteAllItems(frameview: FrameLayout) {
        // 되돌아가기 로직
        // 프레임 레이아웃에서 모든 자식 뷰 제거
        frameview.removeAllViews()
        // 선택된 이미지 데이터 리스트 클리어
        selectedImages.clear()
    }
    private fun deleteLastItems(frameview: FrameLayout){
        // 실행 취소 로직
        if (selectedImages.isNotEmpty()) {
            val lastAddedImage = selectedImages.last() // 가장 마지막에 추가된 이미지 데이터
            val compareResult = ClothesRepository.compareWithUrlCategory(lastAddedImage.imageUrl)
            if(compareResult != null){
                if(compareResult >= 6000 && compareResult <= 6999){
                    count--
                }
            }
            else{
                 Log.d("이미지url과 저장소url이 미스매칭","결과가 null")
            }
            selectedImages.remove(lastAddedImage) // 리스트에서 제거

            // 프레임 레이아웃에서 해당 이미지 뷰를 찾아 제거
            frameview.children.forEach { view ->
                if (view.tag == lastAddedImage) {
                    frameview.removeView(view)
                }
            }
        }
    }
    private fun setItems() {
        // ClothesRepository에서 URL 리스트를 가져옴
        allClothesUrls = ClothesRepository.returnUrls()
    }
    //url이 일치하는지 찾고 url과 카테고리 리턴
    fun findMatchingClothes(imageDataList: List<ImageData>): List<Clothes> {
        val matches = mutableListOf<Clothes>()
        Log.d("clothesData정보",ClothesRepository.clothesData.toString())
        imageDataList.forEach { imageData ->
            // ClothesRepository의 clothesData 리스트에서 일치하는 URL을 찾음
            val matchingClothes = ClothesRepository.clothesData.find { clothes ->
                clothes.cl_photo_path == imageData.imageUrl
            }
            // 일치하는 Clothes 객체가 있으면 matches 리스트에 추가
            matchingClothes?.let { matches.add(it) }
        }
        return matches
    }

    private fun addRandomClothes() {
        // 카테고리 리스트를 초기화
        val categories = listOf(2000..2999, 5000..5999, 6000..6999)
        // 상의와 아우터 중 하나를 랜덤으로 선택
        val topOrOuter = if ((0..1).random() == 0) 1000..1999 else 4000..4999

        // 상의와 아우터를 카테고리 리스트에 추가
        val selectedCategories = categories.toMutableList()
        selectedCategories.add(topOrOuter)

        // 각 카테고리에서 랜덤하게 옷을 선택하여 레이아웃에 추가
        selectedCategories.forEach { categoryRange ->
            val randomClothes = ClothesRepository.getRandomClothes(categoryRange)
            randomClothes?.let { clothes ->
                addClothesToLayout(clothes)
            }
        }
    }


    private fun addClothesToLayout(clothes: Clothes) {
        val imageView = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            Glide.with(this@CoditestActivity)
                .load(clothes.cl_photo_path)
                .into(this)
        }
        val (xPos, yPos) = determinePosition(clothes.cl_category)
        val (width, height) = determineSize(clothes.cl_category)
        imageView.layoutParams = ViewGroup.LayoutParams(width, height)
        imageView.x = xPos
        imageView.y = yPos

        frameview.addView(imageView)
    }


}

class ImageAdapter2(private val context: Context, private val imageUrls: List<String>) : BaseAdapter() {
    override fun getCount(): Int = imageUrls.size

    override fun getItem(position: Int): Any = imageUrls[position]

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val clohtesList = ClothesRepository.returnClothes()
        val view: View
        val imageView: ImageView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.grid_item_layout, parent, false)
        } else {
            view = convertView
        }
        imageView = view.findViewById(R.id.imageView)

        Glide.with(context)
            .load(imageUrls[position])
            .into(imageView)

        return view
    }
}
