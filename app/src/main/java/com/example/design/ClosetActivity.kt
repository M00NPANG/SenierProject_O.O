package com.example.design

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class ClosetActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2
    private lateinit var currentPhotoPath: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var clothButton: Button
    private lateinit var codyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet)

        val makeCodyImageView = findViewById<ImageView>(R.id.makecody)
        makeCodyImageView.setOnClickListener {
            val intent = Intent(this, CreateOutfitActivity::class.java)
            startActivity(intent)
        }

        val clothes = arrayOf("모자", "상의", "하의")
        val codis = arrayOf("코디1", "코디2", "코디3")

        recyclerView = findViewById(R.id.recyclerViewClothes)
        adapter = MyAdapter(clothes) // 초기 데이터로 '옷' 설정
        recyclerView.adapter = adapter

        clothButton = findViewById(R.id.cloth)
        codyButton = findViewById(R.id.cody)

        // 초기 상태 설정
        updateButtonBackground(true)

        clothButton.setOnClickListener {
            adapter.setData(clothes) // '옷' 데이터로 어댑터 설정
            updateButtonBackground(true)
        }

        codyButton.setOnClickListener {
            adapter.setData(codis) // '코디' 데이터로 어댑터 설정
            updateButtonBackground(false)
        }

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        val fabGallery = findViewById<FloatingActionButton>(R.id.fab_gallery)
        val fabCamera = findViewById<FloatingActionButton>(R.id.fab_camera)

        fabAdd.setOnClickListener {
            toggleFabVisibility(fabGallery, fabCamera)
        }

        fabGallery.setOnClickListener {
            openGallery()
        }

        fabCamera.setOnClickListener {
            openCamera()
        }
    }

    private fun toggleFabVisibility(vararg fabs: FloatingActionButton) {
        fabs.forEach { fab ->
            fab.visibility = if (fab.visibility == FloatingActionButton.VISIBLE) {
                FloatingActionButton.INVISIBLE
            } else {
                FloatingActionButton.VISIBLE
            }
        }
    }

    private fun updateButtonBackground(isClothSelected: Boolean) {
        if (isClothSelected) {
            clothButton.setBackgroundResource(R.drawable.selected_button_background)
            codyButton.setBackgroundResource(R.drawable.unselected_button_background)
        } else {
            clothButton.setBackgroundResource(R.drawable.unselected_button_background)
            codyButton.setBackgroundResource(R.drawable.selected_button_background)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: Exception) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}

class MyAdapter(private var data: Array<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    fun setData(newData: Array<String>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 뷰 홀더 생성 로직 구현
        return TODO("Provide the return value")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 데이터 바인딩 로직 구현
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 뷰 홀더 구현
    }
}
