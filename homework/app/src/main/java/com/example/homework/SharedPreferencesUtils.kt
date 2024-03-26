package com.example.homework

import android.content.Context

object SharedPreferencesUtils {
    fun saveEmail(context: Context, loginEmail: String) {
        val pref = context.getSharedPreferences("userEmail", Context.MODE_PRIVATE) //shared key 설정
        val edit = pref.edit() // 수정모드
        edit.putString("email", loginEmail) // 값 넣기
        edit.apply() // 적용하기
    }

    fun loadEmail(context: Context): String? {
        val pref = context.getSharedPreferences("userEmail", Context.MODE_PRIVATE) // 같은 shared key 사용
        return pref.getString("email", null) // "email" 키에 해당하는 값을 불러옴, 기본값은 null
    }
}
