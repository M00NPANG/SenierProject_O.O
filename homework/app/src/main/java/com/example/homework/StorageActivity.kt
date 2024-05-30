package com.example.homework

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.InvalidationTracker
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext



class StorageActivity : AppCompatActivity() {
    lateinit var gridView: GridView
    lateinit var emptyCartTextView: TextView
    lateinit var emptyCartIcon: ImageView
    lateinit var clothesList: MutableList<Clothes>
    lateinit var adapter: StorageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        gridView = findViewById(R.id.gridViewToStorage)
        emptyCartTextView = findViewById(R.id.tvEmptyCart)
        emptyCartIcon = findViewById(R.id.emptyCartIcon)

        // clothes 데이터베이스에서 모든 clothes 데이터 가져오기
        lifecycleScope.launch {
            clothesList = loadClothesData().toMutableList()
            if (clothesList.isEmpty()) {
                emptyCartTextView.visibility = View.VISIBLE
                emptyCartIcon.visibility = View.VISIBLE
                gridView.visibility = View.GONE
            } else {
                adapter = StorageAdapter(this@StorageActivity, clothesList)
                gridView.adapter = adapter
                emptyCartTextView.visibility = View.GONE
                emptyCartIcon.visibility = View.GONE
                gridView.visibility = View.VISIBLE
            }
        }
    }

    // suspend 함수를 사용하여 clothes 데이터 로드
    private suspend fun loadClothesData(): List<Clothes> {
        return withContext(Dispatchers.IO) {
            DatabaseClient.getDatabase(this@StorageActivity).clothesDao().getAllClothes()
        }
    }

    // 휴지통 버튼을 눌렀을 때
    fun deleteClothes(clothes: Clothes) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                DatabaseClient.getDatabase(this@StorageActivity).clothesDao().deleteClothesById(clothes.cl_id!!)
                withContext(Dispatchers.Main) {
                    clothesList.remove(clothes)
                    adapter.notifyDataSetChanged()
                    if (clothesList.isEmpty()) {
                        emptyCartTextView.visibility = View.VISIBLE
                        gridView.visibility = View.GONE
                    }
                    Toast.makeText(this@StorageActivity, "삭제 성공", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("StorageActivity", "Delete failed: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@StorageActivity, "삭제 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

class StorageAdapter(context: Context, private val clothesList: List<Clothes>) :
    ArrayAdapter<Clothes>(context, 0, clothesList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.stored_clothes, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val clothes = getItem(position)
        viewHolder.bindData(context, clothes)

        return view!!
    }

    private class ViewHolder(view: View) {
        private val clothesImageView: ImageView = view.findViewById(R.id.storedClothes)
        private val gotoLinkTextView: TextView = view.findViewById(R.id.gotoLink)
        private val deleteImageView: ImageView = view.findViewById(R.id.delete)

        fun bindData(context: Context, clothes: Clothes?) {
            Glide.with(clothesImageView.context)
                .load(clothes?.cl_photo_path)
                .into(clothesImageView)

            gotoLinkTextView.setOnClickListener {
                val url = clothes?.cl_url
                if (url.isNullOrEmpty()) {
                    Toast.makeText(context, "접근할 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            }

            deleteImageView.setOnClickListener {
                clothes?.let {
                    if (context is StorageActivity) {
                        context.deleteClothes(it)
                    }
                }
            }
        }
    }
}

