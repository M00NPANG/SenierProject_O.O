package com.example.homework


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class PercolResult : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percol_result)

        viewPager = findViewById(R.id.viewPager)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        // 인텐트로부터 결과 받기
        val userPercol = intent.getStringExtra("userPercol") ?: ""

        // 받은 결과에 따라 프래그먼트 순서 조정
        viewPagerAdapter.adjustFragmentOrder(userPercol)
    }
}

class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private var fragments = mutableListOf(
        PhotoFragment1.newInstance(false),
        PhotoFragment2.newInstance(false),
        PhotoFragment3.newInstance(false),
        PhotoFragment4.newInstance(false)
    )

    fun adjustFragmentOrder(percolResult: String) {
        Log.d("현재퍼컬", percolResult)
        fragments = when (percolResult) {
            "spring" -> mutableListOf(
                PhotoFragment1.newInstance(false),
                PhotoFragment2.newInstance(false),
                PhotoFragment3.newInstance(false),
                PhotoFragment4.newInstance(false)
            )
            "summer" -> mutableListOf(
                PhotoFragment2.newInstance(false),
                PhotoFragment1.newInstance(false),
                PhotoFragment3.newInstance(false),
                PhotoFragment4.newInstance(false)
            )
            "autumn" -> mutableListOf(
                PhotoFragment3.newInstance(false),
                PhotoFragment1.newInstance(false),
                PhotoFragment2.newInstance(false),
                PhotoFragment4.newInstance(false)
            )
            "winter" -> mutableListOf(
                PhotoFragment4.newInstance(false),
                PhotoFragment1.newInstance(false),
                PhotoFragment2.newInstance(false),
                PhotoFragment3.newInstance(false)
            )
            else -> fragments
        }

        fragments.forEachIndexed { index, fragment ->
            fragment.arguments?.putBoolean("isLastFragment", index == fragments.size - 1)
        }
        notifyDataSetChanged()
    }


    override fun createFragment(position: Int): Fragment = fragments[position]

    override fun getItemCount() = fragments.size
}