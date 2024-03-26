package com.example.homework

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import dev.eren.removebg.RemoveBg
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class ReceivetestActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receivetest)
        val bitmap : Bitmap? = loadBitmapFromJpeg("image")
        imageView = findViewById(R.id.receiveImage)

        lifecycleScope.launch {
            val remover = RemoveBg(this@ReceivetestActivity)

            var outputImage: Bitmap?

            remover.clearBackground(bitmap!!).collect { output ->
                if (output != null) {
                    outputImage = output
                    imageView.setImageBitmap(outputImage)
                    val endPoint = "/testUpload"
                    val url = ipAddr + endPoint
                    uploadBitmap(outputImage!!, url)
                }
            }
        }
    }

    private fun loadBitmapFromJpeg(name: String): Bitmap? {
        // 내부 저장소 캐시 디렉토리에서 파일을 가져옵니다.
        val storage = cacheDir

        // 가져올 파일 이름
        val fileName = "$name.jpg"

        // 저장된 파일의 전체 경로를 설정합니다.
        val file = File(storage, fileName)

        // 파일이 존재하는지 확인합니다.
        if (file.exists()) {
            try {
                // 파일을 읽을 FileInputStream을 생성합니다.
                val stream = FileInputStream(file)

                // FileInputStream을 Bitmap으로 디코딩합니다.
                val bitmap = BitmapFactory.decodeStream(stream)

                // FileInputStream을 닫습니다.
                stream.close()

                // Bitmap을 반환합니다.
                return bitmap
            } catch (e: FileNotFoundException) {
                Log.e("MyTag", "FileNotFoundException : " + e.message)
            } catch (e: IOException) {
                Log.e("MyTag", "IOException : " + e.message)
            }
        } else {
            Log.e("MyTag", "File not found.")
        }
        return null
    }


}