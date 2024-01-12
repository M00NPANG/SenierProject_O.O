package com.example.homework

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

import java.util.*

data class WEATHER (val response : RESPONSE)
data class RESPONSE(val header : HEADER, val body : BODY)
data class HEADER(val resultCode : Int, val resultMsg : String)
data class BODY(val dataType : String, val items : ITEMS)
data class ITEMS(val item : List<ITEM>)
// category : 자료 구분 코드, fcstDate : 예측 날짜, fcstTime : 예측 시간, fcstValue : 예보 값
data class ITEM(val category : String, val fcstDate : String, val fcstTime : String, val fcstValue : String)

//var gson= GsonBuilder().setLenient().create()

private val retrofit = Retrofit.Builder()
    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")//http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}


class LobbyActivity : AppCompatActivity() {
    lateinit var tvRainRatio : TextView     // 강수 확률
    lateinit var tvRainType : TextView      // 강수 형태
    lateinit var tvHumidity : TextView      // 습도
    lateinit var tvSky: TextView           // 하늘 상태
    lateinit var tvTemp: TextView          // 온도


    var base_date = "20240110"  // 발표 일자
    var base_time = "0500"      // 발표 시각
    var nx = "55"               // 예보지점 X 좌표
    var ny = "125"              // 예보지점 Y 좌표

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        tvSky = findViewById(R.id.tvSky)
        tvTemp = findViewById(R.id.tvTemp)
        tvRainRatio = findViewById(R.id.tvSky)
        tvRainType = findViewById(R.id.tvSky)
        tvHumidity = findViewById(R.id.tvSky)
        // nx, ny지점의 날씨 가져와서 설정하기
        setWeather(nx, ny)
    }

    fun setWeather(nx : String, ny : String) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val time = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시간
        // API 가져오기 적당하게 변환
        base_time = getTime(time)
        Log.d("basetime",base_time)
        Log.d("basedate",base_date)
        // 동네예보  API는 3시간마다 현재시간+4시간 뒤의 날씨 예보를 알려주기 때문에
        // 현재 시각이 00시가 넘었다면 어제 예보한 데이터를 가져와야함
        if (base_time >= "2000") {
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }

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
                                Log.d("aa",items[i].category.toString())
                                when(items[i].category) {
                                    "POP" -> rainRatio = items[i].fcstValue    // 강수 기온
                                    "PTY" -> rainType = items[i].fcstValue    // 강수 형태
                                    "REH" -> humidity = items[i].fcstValue     // 습도
                                    "SKY" -> sky = items[i].fcstValue          // 하늘 상태
                                    "TMP" -> temp = items[i].fcstValue         // 기온

                                    else -> continue
                                }
                            }
                            Log.d("test",rainRatio)
                            Log.d("test2",rainType)
                            Log.d("test3",humidity)
                            Log.d("test4",sky)
                            Log.d("test5",temp)

                            // 날씨 정보 텍스트뷰에 보이게 하기
                            setWeather(rainRatio, rainType, humidity, sky, temp)

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


            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api fail", t.message.toString())
            }
        })
    }
    // 텍스트 뷰에 날씨 정보 보여주기
    fun setWeather(rainRatio : String, rainType : String, humidity : String, sky : String, temp : String) {
        // 강수 확률

        // 강수 형태
        var result = ""
        when(rainType) {

            "0" -> result = "없음"
            "1" -> result = "비"
            "2" -> result = "비/눈"
            "3" -> result = "눈"
            "4" -> result = "소나기"
            "5" -> result = "빗방울"
            "6" -> result = "빗방울/눈날림"
            "7" -> result = "눈날림"
            else -> "오류"
        }


        // 하능 상태
        result = ""
        when(sky) {
            "1" -> result = "맑음"
            "3" -> result = "구름 많음"
            "4" -> result = "흐림"
            else -> "오류"
        }
        tvSky.text = result
        // 온도
        tvTemp.text = temp + "°"
    }

    // 시간 설정하기
    // 동네 예보 API는 3시간마다 현재시각+4시간 뒤의 날씨 예보를 보여줌
    // 따라서 현재 시간대의 날씨를 알기 위해서는 아래와 같은 과정이 필요함. 자세한 내용은 함께 제공된 파일 확인
    fun getTime(time : String) : String {
        var result = ""
        when(time) {
            in "00".."02" -> result = "2000"    // 00~02
            in "03".."05" -> result = "2300"    // 03~05
            in "06".."08" -> result = "0200"    // 06~08
            in "09".."11" -> result = "0500"    // 09~11
            in "12".."14" -> result = "0800"    // 12~14
            in "15".."17" -> result = "1100"    // 15~17
            in "18".."20" -> result = "1400"    // 18~20
            else -> result = "1700"             // 21~23
        }
        return result
    }
}