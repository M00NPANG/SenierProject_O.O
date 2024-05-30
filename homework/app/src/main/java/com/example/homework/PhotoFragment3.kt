package com.example.homework

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoFragment3 : Fragment() {
    companion object {
        fun newInstance(isLastFragment: Boolean): PhotoFragment3 {
            val fragment = PhotoFragment3()
            val args = Bundle()
            args.putBoolean("isLastFragment", isLastFragment)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo3, container, false)
        val textViewBottomRight = view.findViewById<TextView>(R.id.textViewBottomRight)
        val backgroundImageView = view.findViewById<FrameLayout>(R.id.backgroundImageView)

        // 인자에서 마지막 프래그먼트 여부를 확인
        val isLastFragment = arguments?.getBoolean("isLastFragment") ?: false
        if (isLastFragment) {
            textViewBottomRight.text = "완료"
            textViewBottomRight.setOnClickListener {
                startActivity(Intent(activity, LobbyActivity::class.java))
            }
        } else {
            textViewBottomRight.text = "옆으로 넘겨보세요!"
        }
        loadAndDisplayImage(backgroundImageView)
        return view
    }
    private fun loadAndDisplayImage(frameLayout: FrameLayout) {
        lifecycleScope.launch {
            val email = SharedPreferencesUtils.loadEmail(requireActivity())!!
            val imageUrl = getFaceUrl(email) // 서버에서 얼굴 이미지 URL을 받아오는 함수
            val backgroundImageView = view?.findViewById<FrameLayout>(R.id.backgroundImageView)

            withContext(Dispatchers.Main) {
                // dp 단위로 변환
                val widthInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics
                ).toInt()
                val heightInPixels = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 150f, resources.displayMetrics
                ).toInt()

                // 이미지 뷰 생성 및 설정
                val imageView = ImageView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        widthInPixels, // 너비
                        heightInPixels  // 높이
                    ).also {
                        it.gravity = Gravity.CENTER
                    }
                }


                // Glide를 사용하여 이미지 로드 후 ImageView에 설정
                Glide.with(this@PhotoFragment3)
                    .load(imageUrl)
                    .centerCrop() // 이미지가 프레임 레이아웃을 꽉 채우도록 조정
                    .into(imageView)
                frameLayout.addView(imageView)
            }
        }
    }

}