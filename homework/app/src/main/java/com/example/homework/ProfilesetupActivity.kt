package com.example.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProfilesetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilesetup)
        lifecycleScope.launch {
            receiveClothes(SharedPreferencesUtils.loadEmail(this@ProfilesetupActivity).toString())
        }
    }
}