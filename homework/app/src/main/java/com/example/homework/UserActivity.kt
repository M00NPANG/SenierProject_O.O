package com.example.homework


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val home: ImageView = findViewById(R.id.home)
        home.setOnClickListener {
            intent = Intent(this, LobbyActivity::class.java)
            startActivity(intent)
        }

        val closet: ImageView = findViewById(R.id.closet)
        closet.setOnClickListener {
            intent = Intent(this, ClosetActivity::class.java)
            startActivity(intent)
        }

        val checkPC :LinearLayout= findViewById(R.id.checkPersonalColor)
        checkPC.setOnClickListener {
            val email = SharedPreferencesUtils.loadEmail(this)
            lifecycleScope.launch {
                val percol = checkUserPersonalColor(email!!)
                Toast.makeText(this@UserActivity, "현재 퍼컬 : $percol", Toast.LENGTH_SHORT).show()
                intent = Intent(this@UserActivity,PercolResult::class.java)
                intent.putExtra("userPercol",percol)
                startActivity(intent)
            }
        }

        val percolCheck : LinearLayout = findViewById(R.id.percolCheckBtn)
        percolCheck.setOnClickListener{
            startActivity(Intent(this@UserActivity,FaceActivity::class.java))
        }
    }
}