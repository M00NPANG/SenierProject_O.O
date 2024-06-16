package com.example.homework

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS)
data class ITEMS(val item : List<ITEM>)
// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)



//var gson= GsonBuilder().setLenient().create()

private val retrofit = Retrofit.Builder() // http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst <- 초단기예보
    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")//http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0 <- 단기예보
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}

class LobbyActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var storage : ImageView
    var wheather: String = ""
    var temperature : Int = 0
    var TO_GRID = 0
    var TO_GPS = 1
    lateinit var tvRainRatio : TextView     // 강수 확률
    lateinit var tvRainType : TextView      // 강수 형태
    lateinit var tvHumidity : TextView      // 습도
    lateinit var tvSky: TextView            // 하늘 상태
    lateinit var tvTemp: TextView           // 온도
    lateinit var skyImg : ImageView // 하늘 이미지
    var base_date = "11111111"  // 발표 일자
    var base_time = "aaaa"      // 발표 시각
    var time : String = ""
    private lateinit var postAdapter: PostAdapter
    private val posts = mutableListOf<Post>()
    private lateinit var mixBtn : Button
    private lateinit var closetBtn : Button
    private lateinit var interestBtn : Button
    private lateinit var shopBtn : Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tempCheckBox : CheckBox
    private var tempStatus : Int = 0
    private var temper : Int = 10
    private lateinit var badgeTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        //초기화 부분
        mixBtn = findViewById(R.id.mixBtn)
        mixBtn.isSelected = true
        closetBtn = findViewById(R.id.closetBtn)
        shopBtn = findViewById(R.id.shopBtn)
        storage = findViewById(R.id.storage)
        recyclerView = findViewById(R.id.recommendedView)
        tvSky = findViewById(R.id.tvSky)
        tvTemp = findViewById(R.id.tvTemp)
        skyImg = findViewById(R.id.skyImg)
        recyclerView.layoutManager = GridLayoutManager(this@LobbyActivity, 2)
        postAdapter = PostAdapter(this@LobbyActivity,lifecycleScope,0,2)
        recyclerView.adapter = postAdapter
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        badgeTextView = findViewById(R.id.badgeTextView)
        updateStorageBadge()
        getCurrentLocation() // 현재 위치를 구하는 것 뿐 아니라 그 위치로 기상청 api에 날씨정보를 요청함

        // 하단 메뉴바이고, 나중에 간략화하기
        val closet : ImageView = findViewById(R.id.closet)
        closet.setOnClickListener{
            intent = Intent(this@LobbyActivity,ClosetActivity::class.java)
            startActivity(intent)
        }
        val home : ImageView = findViewById(R.id.home)
        home.setOnClickListener {
            val intent = Intent(this, LobbyActivity::class.java)
            finish()
            startActivity(intent)
        }
        storage.setOnClickListener {
            startActivity(Intent(this,StorageActivity::class.java))
        }
        val user: ImageView = findViewById(R.id.user)
        user.setOnClickListener {
            startActivity(Intent(this,UserActivity::class.java))
        }
        shopBtn.setOnClickListener{
            tempStatus = if(tempCheckBox.isChecked) 1 else 0
            postAdapter = PostAdapter(this@LobbyActivity,lifecycleScope,tempStatus,3)
            recyclerView.adapter = postAdapter
            shopBtn.isSelected = true
            mixBtn.isSelected = false
            closetBtn.isSelected = false
        }
        mixBtn.setOnClickListener {
            tempStatus = if (tempCheckBox.isChecked) 1 else 0
            postAdapter = PostAdapter(this@LobbyActivity,lifecycleScope,tempStatus,2)
            recyclerView.adapter = postAdapter
            shopBtn.isSelected = false
            mixBtn.isSelected = true
            closetBtn.isSelected = false
        }
        closetBtn.setOnClickListener {
            tempStatus = if (tempCheckBox.isChecked) 1 else 0
            postAdapter = PostAdapter(this@LobbyActivity,lifecycleScope,tempStatus,1)
            recyclerView.adapter = postAdapter
            shopBtn.isSelected = false
            mixBtn.isSelected = false
            closetBtn.isSelected = true
        }
        tempCheckBox =findViewById(R.id.tempCheckBox)
        tempCheckBox.setOnCheckedChangeListener { _, isChecked ->
            tempStatus = if (isChecked) 1 else 0
            postAdapter = PostAdapter(this@LobbyActivity, lifecycleScope, tempStatus, 2)
            recyclerView.adapter = postAdapter


        }

        // 서버로부터 코디를 받고 띄움
        //loadRecommendedPosts(SharedPreferencesUtils.loadEmail(this).toString())
    }
    // storage에 있는 옷 숫자 띄움
    private fun updateStorageBadge() {
        lifecycleScope.launch {
            val count = withContext(Dispatchers.IO) {
                DatabaseClient.getDatabase(this@LobbyActivity).clothesDao().getClothesCount()
            }

            if (count > 0) {
                badgeTextView.text = count.toString()
                badgeTextView.visibility = View.VISIBLE
            } else {
                badgeTextView.visibility = View.GONE
            }
        }
    }

    private fun getCurrentLocation() { // 현재 위도경도를 구하고 그걸 격자로 바꾸고 그걸로 api 통신
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude
                val tmp = convertGRID_GPS(TO_GRID, latitude, longitude)
                val a = tmp!!.x.toInt()
                val b = tmp!!.y.toInt()
                setWeather(a.toString(),b.toString())
                Log.d("LocationInfo", "Current location is: Lat: $latitude, Lon: $longitude")
                Log.d("LocationInfo2", "Current location is: x: $a, y: $b")

            } ?: Log.d("LocationInfo", "Location is null")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) { //권한확인용
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Log.d("LocationInfo", "Permission denied")
        }
    }

    fun setWeather(nx : String, ny : String) { //날짜 설정
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        time = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시간
        // API 가져오기 적당하게 변환
        base_time = getTime(time)


        // 날씨 정보 가져오기
        // (응답 자료 형식-"JSON", 한 페이지 결과 수 = 10, 페이지 번호 = 1, 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = ApiObject.retrofitService.GetWeather("JSON", 10, 1, base_date, base_time, nx, ny)
        // 비동기 실행
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.response?.body
                    if (responseBody != null) {
                        val items = responseBody.items?.item

                        if (items != null && items.size >= 10) {
                            // 날씨 정보 가져오기
                            var rainRatio = "x"      // 강수 확률
                            var rainType = "x"       // 강수 형태
                            var humidity = "x"       // 습도
                            var sky = ""            // 하늘 상태
                            var temp = ""           // 기온

                            for (i in 0..9) {
                                when(items[i].category) {
                                    "POP" -> rainRatio = items[i].fcstValue    // 강수 확률
                                    "PTY" -> rainType = items[i].fcstValue     // 강수 형태
                                    "REH" -> humidity = items[i].fcstValue     // 습도
                                    "SKY" -> sky = items[i].fcstValue          // 하늘 상태
                                    "TMP" -> temp = items[i].fcstValue         // 기온

                                    else -> continue
                                }
                            }

                            // 날씨 정보 보이기
                            setWeather(rainRatio, rainType, humidity, sky, temp)
                            Log.d("location",base_date)

                            // 토스트 띄우기
                            Toast.makeText(applicationContext, items[0].fcstDate + ", " + items[0].fcstTime + "의 날씨 정보입니다.", Toast.LENGTH_SHORT).show()

                        } else {
                            // items가 null이거나 크기가 10보다 작을 때의 처리
                        }
                    } else {
                        // response body가 null일 때의 처리
                    }
                } else {
                    // 오류 응답을 처리
                }
            }


            // 응답 실패
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
        updateClothesIcons("clean",11)  // 나중에 지워버리기.
    }
    // 뷰에 날씨 정보 보여주기
    fun setWeather(rainRatio : String, rainType : String, humidity : String, sky : String, temp : String) {

        // 강수 형태
        var result = ""
        when(rainType) {
            "0" -> setSky(sky) // 강수 없음
            "1" -> skyImg.setImageResource(R.drawable.rain) // 비
            "2" -> skyImg.setImageResource(R.drawable.rain) // 비/눈 인데 그냥 비로 함
            "3" -> skyImg.setImageResource(R.drawable.snow) // 눈
            "4" -> skyImg.setImageResource(R.drawable.rain) // 비(소나기)
            "5" -> skyImg.setImageResource(R.drawable.rain) // 비
            "6" -> skyImg.setImageResource(R.drawable.rain) // 비
            "7" -> skyImg.setImageResource(R.drawable.snow) // 눈
            else -> "오류"
        }

        when(rainType){
            "1" -> wheather = "rain"
            "2" -> wheather = "rain"
            "3" -> wheather = "snow"
            "4" -> wheather = "rain"
            "5" -> wheather = "rain"
            "6" -> wheather = "rain"
            "7" -> wheather = "snow"
            else -> "오류"
        }

        tvSky.text = result
        // 온도
        tvTemp.text = temp + "°"
        temperature = temp.toInt()
        tempStorage.setTemp(temperature.toLong())
        updateClothesIcons(wheather, temperature)
    }


    // 동네 예보 API는 3시간마다 현재시각+4시간 뒤의 날씨 예보를 보여줌
    fun getTime(time : String) : String {

        var result = ""
        when(time) {
            in "00".."02" -> result = "0200"    // 00~02  "2000"
            in "03".."05" -> result = "0500"    // 03~05  "2300"
            in "06".."08" -> result = "0800"    // 06~08  "0200"
            in "09".."11" -> result = "1100"    // 09~11  "0500"
            in "12".."14" -> result = "1400"    // 12~14  "0800"
            in "15".."17" -> result = "1700"    // 15~17  "1100"
            in "18".."20" -> result = "2000"    // 18~20  "1400"
            else -> result = "2300"             // 21~23   "1700"
        }
        return result

    }

    fun setSky(sky : String){ // 하늘상태임
        Log.d("time",time)
        when(sky){
            "1" -> wheather = "clean"
            "3" -> wheather = "little_cloudy"
            "4" -> wheather = "cloudy"
        }
        if(time.toInt() < 5  || time.toInt() > 17){
            when(sky) {
                "1" -> skyImg.setImageResource(R.drawable.moon)  // 맑음(달)
                "3" -> skyImg.setImageResource(R.drawable.little_cloud_moon) // 약간 흐림(달)
                "4" -> skyImg.setImageResource(R.drawable.cloud) // 흐림
                else -> "오류"
            }
        }
        else{
            when(sky) {  //
                "1" -> skyImg.setImageResource(R.drawable.sun) // 맑음(해)
                "3" -> skyImg.setImageResource(R.drawable.little_cloud) // 약간 흐림(해)
                "4" -> skyImg.setImageResource(R.drawable.cloud) // 흐림
                else -> "오류"
            }
        }
    }

    private fun convertGRID_GPS(mode: Int, lat_X: Double, lng_Y: Double): LatXLngY? {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기1준점 Y좌표(GRID)


        val DEGRAD = Math.PI / 180.0
        val RADDEG = 180.0 / Math.PI
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD
        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)
        val rs = LatXLngY()
        if (mode == TO_GRID) {
            rs.lat = lat_X
            rs.lng = lng_Y
            var ra = Math.tan(Math.PI * 0.25 + lat_X * DEGRAD * 0.5)
            ra = re * sf / Math.pow(ra, sn)
            var theta = lng_Y * DEGRAD - olon
            if (theta > Math.PI) theta -= 2.0 * Math.PI
            if (theta < -Math.PI) theta += 2.0 * Math.PI
            theta *= sn
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5)
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)
        } else {
            rs.x = lat_X
            rs.y = lng_Y
            val xn = lat_X - XO
            val yn = ro - lng_Y + YO
            var ra = Math.sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = Math.pow(re * sf / ra, 1.0 / sn)
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5
            var theta = 0.0
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else theta = Math.atan2(xn, yn)
            }
            val alon = theta / sn + olon
            rs.lat = alat * RADDEG
            rs.lng = alon * RADDEG
        }
        return rs
    }


    internal class LatXLngY {
        var lat = 0.0
        var lng = 0.0
        var x = 0.0
        var y = 0.0
    }

    private fun loadRecommendedPosts(email: String) { // 로비에 띄울 포스트들을 구해옴
        CoroutineScope(Dispatchers.Main).launch {
            // receiveRecommendPosts 함수 호출을 통해 직접 Post 객체의 리스트를 받음
            val recommendedPosts = receiveRecommendPosts(email)
            // posts 리스트를 받아온 데이터로 업데이트
            posts.clear()
            posts.addAll(recommendedPosts)
            postAdapter.notifyDataSetChanged()
        }
    }

    private fun updateClothesIcons(weather: String, temperature: Int) {
        val container = findViewById<LinearLayout>(R.id.clothesIconContainer)
        container.removeAllViews() // 기존 아이콘 제거
        Log.d("날씨와 기온", "날씨 : $weather , 기온 : $temperature")
        val clothesIcons = getClothesIcons(weather, temperature)

        clothesIcons.forEach { icon ->
            val imageView = ImageView(this).apply {
                // LinearLayout 내에 ImageView를 추가할 때는 LinearLayout.LayoutParams를 사용
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    // 마진 설정
                    val dpSize = 10
                    val scale = resources.displayMetrics.density // 화면의 density
                    val marginInPixels = (dpSize * scale + 0.5f).toInt() // dp를 픽셀 단위로 변환
                    // 수평 LinearLayout이므로, 양쪽 마진 설정
                    setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)
                }
                setImageResource(icon)

                // 매핑된 식별자 ID를 태그로 설정
                tag = iconMap[icon]

                setOnClickListener {
                    // 태그를 사용하여 식별자 ID를 가져옴
                    val cl_id = it.tag as? Int ?: 0 // 태그가 없는 경우 0을 기본값으로 사용
                    Toast.makeText(this@LobbyActivity, "Clicked Icon ID: $cl_id", Toast.LENGTH_SHORT).show()

                    // Intent를 생성하여 ClothesDetailActivity를 시작하고, cl_id를 전달
                    val intent = Intent(this@LobbyActivity, ClothesDatailActivity::class.java).apply {
                        putExtra("CL_ID", cl_id) // "CL_ID"는 key로, cl_id는 전달될 값
                    }
                    startActivity(intent)
                }
            }
            container.addView(imageView)
        }
    }

    private fun getClothesIcons(weather: String, temperature: Int): List<Int> {
        // 날씨와 온도에 따라 적절한 옷 아이콘 리소스 ID 리스트 반환
        // 예시로 간단히 처리; 실제 사용시 더 상세한 로직 필요
        return when {
            temperature > 10 -> listOf(R.drawable.shirt_icon, R.drawable.hat_icon, R.drawable.pants_icon)
            else -> listOf(R.drawable.roading)
        }
    }

    override fun onResume() {
        updateStorageBadge()
        super.onResume()
    }

}


val iconMap = mapOf(
    R.drawable.shirt_icon to 1001,
    R.drawable.pants_icon to 2002,
    R.drawable.hat_icon to 7001,
    // 여기에 더 많은 아이콘과 식별자 매핑 추가
)

