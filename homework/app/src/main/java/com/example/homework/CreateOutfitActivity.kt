package com.example.homework

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.text.TextWatcher
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AbsListView
import android.widget.BaseAdapter
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.homework.ClothesRepository.allClothesUrls
import com.example.homework.ClothesRepository.returnClothes
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.math.sqrt
import kotlin.properties.Delegates

//data class ImageData(val resourceId: Int, var x: Float = 0f, var y: Float = 0f, var width: Int = 0, var height: Int = 0)
data class ImageData(val imageUrl: String, var x: Float = 0f, var y: Float = 0f, var width: Int = 0, var height: Int = 0)

class CreateOutfitActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_create_outfit)

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

        gridView.setOnItemClickListener { _, _, position, _ ->
            addImageToLayout(position)
        }
        //gridView.adapter = ImageAdapter(this@CreateOutfitActivity, Img)
        val imageUrls = ClothesRepository.returnUrls()
        gridView.adapter = ImageAdapter(this@CreateOutfitActivity, imageUrls)

        backButton.setOnClickListener {
            deleteAllItems(frameview) // 모든 아이템 제거
        }

        undoButton.setOnClickListener {
            deleteLastItems(frameview) // 마지막 아이템 제거
        }

        redoButton.setOnClickListener {
        }

        checkButton.setOnClickListener {
            val list = findMatchingClothes(selectedImages) // 선택된 이미지들의 리스트
            ClothesRepository.addMatchedClothes(list)
            Log.d("현재 리스트",selectedImages.toString())
            // FrameLayout을 캡쳐하여 Bitmap으로 변환
            val bitmap = getBitmapFromView(frameview)

            val imagePath = saveImageToInternalStorage(this@CreateOutfitActivity, bitmap, "testimg.png") // 내부 저장소에 이미지 저장

            // Intent를 사용하여 다음 액티비티로 이미지 경로 전달
            val intent = Intent(this@CreateOutfitActivity, CreatePostActivity::class.java).apply {
                putExtra("imagePath", imagePath)

            }

            startActivity(intent)
        }

        // 프레임레이아웃(frameview로 이름 바꿨음)의 가로/세로 측정
        frameview.viewTreeObserver.addOnGlobalLayoutListener(object:ViewTreeObserver.OnGlobalLayoutListener{
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
            Glide.with(this@CreateOutfitActivity)
                .load(imageUrl)
                .into(this)

            tag = imageData // ImageView와 ImageData 인스턴스 연결

            // 이 부분에 onTouchListener 설정
            setOnTouchListener(object : View.OnTouchListener {
                var I_width = 0
                var I_height = 0
                var dX: Float = 0f
                var dY: Float = 0f
                var lastEvent: Int = 0
                var downX: Float = 0f
                var downY: Float = 0f
                var startDistance: Float = 0f // 두 손가락 사이의 시작 거리
                var mode: Int = 0

                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when (event?.actionMasked) {
                        MotionEvent.ACTION_DOWN -> { // 누르기 시작해서 터치한 시점
                            dX = view!!.x - event.rawX
                            dY = view.y - event.rawY
                            downX = event.rawX
                            downY = event.rawY
                            lastEvent = MotionEvent.ACTION_DOWN
                        }
                        MotionEvent.ACTION_MOVE -> { // 누르는 중일때
                            //양손터치(크기 조절할 때)
                            if (event.pointerCount == 2 && mode == MotionEvent.ACTION_POINTER_DOWN) {
                                val newDist = distance(event)
                                if (newDist > 10f) {
                                    val scale = newDist / startDistance
                                    val adjustScale = 1+(scale - 1)* 0.5 * 0.5 * 0.5 * 0.3
                                    I_width  = (width*adjustScale).toInt()
                                    I_height = (height*adjustScale).toInt()

                                    val layoutParams = view!!.layoutParams
                                    layoutParams.width = I_width
                                    layoutParams.height = I_height
                                    view.layoutParams = layoutParams
                                    Log.d("가로,세로","x:$I_width, y:$I_height")

                                    val imageData = view.tag as ImageData
                                    imageData.width = I_width
                                    imageData.height = I_height

                                    /*val scale = newDist / startDistance
                                    view?.scaleX = scale
                                    view?.scaleY = scale*/
                                }

                            } else if (event.pointerCount == 1) { // 한손터치(이미지 움직일 때)

                                view!!.animate()
                                    .x(event.rawX + dX)
                                    .y(event.rawY + dY)
                                    .setDuration(0)
                                    .start()

                            }
                            lastEvent = MotionEvent.ACTION_MOVE
                        }
                        MotionEvent.ACTION_UP -> {
                            var a = view!!.x
                            var b = view.y
                            val imageData = view.tag as ImageData
                            imageData.x = a // 좌표값 넣기
                            imageData.y = b // 좌표값 넣기
                            /*if((a >= F_width-I_width) || (a <= 0) || (b <= 0) || (b > F_height-I_height)){
                                view.x = 0F
                                view.y = 0F
                            }*/ // 이 코드는 현재 아이템이 일정 범위 바깥을 나가면 인식하는 코드인데 문제가 있어서 일시적 비활성화

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
        /* 다음은 초기 생성 시 바로 가로세로길이를 알아내서 view가 layout 바깥으로 나가지 못하게 할 때 필요
        // 레이아웃이 완료된 후 크기 정보를 얻기 위한 OnGlobalLayoutListener 추가
        val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 올린 후 크기 저장.
                xx = imageView.width
                yy = imageView.height
                imageData.width = imageView.width
                imageData.height = imageView.height
                // 이 리스너가 더 이상 필요하지 않기 때문에 반드시 없야함
                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        imageView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
        */
        (findViewById<ViewGroup>(R.id.view)).addView(imageView)
        selectedImages.add(imageData)
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




}

class ImageAdapter(private val context: Context, private val imageUrls: List<String>) : BaseAdapter() {
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


//이미지를 저장
fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, fileName: String): String {
    // 내부 저장소의 앱 전용 디렉토리에 파일을 저장
    val directory = context.filesDir // 앱의 파일 디렉토리를 가져옴
    val file = File(directory, fileName)
    try {
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // 저장된 파일의 경로를 반환
    return file.absolutePath
}


