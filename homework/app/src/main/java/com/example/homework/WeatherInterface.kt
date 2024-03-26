package com.example.homework

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


// 결과 xml 파일에 접근해서 정보 가져오기
interface WeatherInterface {
    // getVilageFcst : 단기예보 조회
    @GET("getVilageFcst?serviceKey=O1RrlReE2z2dLF5Xko3fnUBM2g83q0R0jF%2FNEidZQXp1LxhCImVcGZyr4hatkEeFS7HyR5e7umJuSCAsm9e6zQ%3D%3D") //O1RrlReE2z2dLF5Xko3fnUBM2g83q0R0jF%2FNEidZQXp1LxhCImVcGZyr4hatkEeFS7HyR5e7umJuSCAsm9e6zQ%3D%3D

    fun GetWeather(@Query("dataType") data_type : String,
                   @Query("numOfRows") num_of_rows : Int,
                   @Query("pageNo") page_no : Int,
                   @Query("base_date") base_date : String,
                   @Query("base_time") base_time : String,
                   @Query("nx") nx : String,
                   @Query("ny") ny : String)
            : Call<WEATHER>
}