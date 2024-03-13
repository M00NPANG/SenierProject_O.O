package com.example.homework

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PercolActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var CAMERA_PERMISSION_CODE = 101
    lateinit var currentPhotoPath: String
    lateinit var imageView : ImageView
    lateinit var button: Button
    lateinit var bitmapForPercol : Bitmap
    lateinit var sendButton : Button
    lateinit var userEmail : String
    lateinit var userName : String
    lateinit var userPassword : String
    var userPercol : String = ""
    var userStyle : String = ""
    var userColor : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percol)

        button = findViewById(R.id.percolButton)
        imageView = findViewById(R.id.imageForPercol)
        sendButton = findViewById(R.id.sendImageButton)
        userName = intent.getStringExtra("name").toString()
        userEmail = intent.getStringExtra("email").toString()
        userPassword = intent.getStringExtra("password").toString()

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                // 권한이 이미 허용되어 있음
                dispatchTakePictureIntent()
            }
        }

        sendButton.setOnClickListener {
            val endPoint = "/testUpload"
            val url = ipAddr + endPoint
            uploadBitmap(bitmapForPercol, url)
            userPercol = "winter_cool"
            var user : User
            user = User(
                name = userName,
                email = userEmail,
                password = userPassword,
                user_percol = userPercol,
                user_color = userColor,
                user_style = userStyle
            )
            val endPoint2 = "/api/join"
            val url2 = ipAddr + endPoint
            var result : Int
            lifecycleScope.launch {
                result = sendUserDataToJoin(user, url2)
                // UI 스레드에서 결과 처리
                withContext(Dispatchers.Main) {
                    if (result == 0) {
                        Toast.makeText(this@PercolActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@PercolActivity, LoginActivity::class.java)
                        // 현재 태스크에 속한 모든 액티비티를 제거하고 새로운 태스크를 시작
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@PercolActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }


    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 필요한 권한이 허용되었을 때
                dispatchTakePictureIntent()
            } else {
                // 사용자가 권한을 거부했을 때
                Toast.makeText(this, "Camera and storage permission are needed to take pictures", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: throw IOException("External Storage Access Error")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.homework.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            setPic()
        }
    }

    private fun setPic() {
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(currentPhotoPath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            val matrix = Matrix()
            matrix.postRotate(90f) // 오른쪽으로 90도 회전

            // 회전된 비트맵 생성
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            imageView.setImageBitmap(rotatedBitmap)
            bitmapForPercol = rotatedBitmap

            sendButton.visibility = View.VISIBLE

        }
    }
}