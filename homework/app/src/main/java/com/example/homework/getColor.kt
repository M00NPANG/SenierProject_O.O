package com.example.homework

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

fun getPersonalColorType(rgb: Int): String {
    val hsv = FloatArray(3)
    Color.colorToHSV(rgb, hsv)
    val hue = hsv[0]  // 색상
    val saturation = hsv[1]  // 채도
    val value = hsv[2]  // 명도

    /*
    // 무채색 판별 임계값
    val thresholdValue = 0.2f // 명도 임계값 (0.0 ~ 1.0)

    // 명도 값이 임계값보다 작으면 무채색으로 간주
    val isMonochrome = value <= thresholdValue

    // 무채색인 경우 "무채색" 반환
    if (isMonochrome) {
        Log.d("무채색","무채색")
        return "Monochrome"
    }*/
    val saturationThreshold = 0.035f // 채도 임계값 (0.0 ~ 1.0) 이 값을 늘리면 더 많은 색이 무채색이 됨
    val valueThreshold = 0.15f // 명도 임계값 (0.0 ~ 1.0)

    // 채도 또는 명도 값이 임계값보다 작으면 무채색으로 간주
    val isMonochrome = saturation <= saturationThreshold || value <= valueThreshold

    // 무채색인 경우 "무채색" 반환
    if (isMonochrome) {
        Log.d("무채색","무채색")
        return "Monochrome"
    }
    //0313190221
    //퍼스널 컬러 유형 결정
    return when {
        hue >= 0 && hue < 30 -> "autumn"
        hue >= 30 && hue < 90 -> "spring"
        hue >= 90 && hue < 210 -> "summer"
        hue >= 210 && hue < 330 -> "winter"
        hue >= 330 && hue <= 360 -> "autumn"
        else -> "ERROR"
    }
}

suspend fun decidePersonalColorFromImage(bitmap: Bitmap): Pair<Int, String> {
    return withContext(Dispatchers.IO) { // 백그라운드 스레드에서 실행
        //val palette = Palette.from(bitmap).resizeBitmapArea(10).generate()  <- 너무 단순화시키니까 갈색 인식을 못함
        val palette = Palette.from(bitmap).generate()                 // <- 그래서 단순화를 줄임
        val colorUsedForPersonalColorType = palette.getDominantColor(0)
        val personalColorType = getPersonalColorType(colorUsedForPersonalColorType)
        Pair(colorUsedForPersonalColorType, personalColorType) // 색상과 퍼스널 컬러 유형을 반환
    }
}

fun findClosestCSSColor(color: Int, cssColors: List<CSSColor>): CSSColor {
    val r = (color shr 16) and 0xFF
    val g = (color shr 8) and 0xFF
    val b = color and 0xFF

    return cssColors.minByOrNull { cssColor ->
        val rDiff = r - cssColor.r
        val gDiff = g - cssColor.g
        val bDiff = b - cssColor.b
        sqrt((rDiff * rDiff + gDiff * gDiff + bDiff * bDiff).toDouble())
    }!!
}




fun logUniqueClosestCSSColorsForImage(bitmap: Bitmap, cssColors: List<CSSColor>) {
    val colorFrequencyMap = mutableMapOf<String, Int>()
    val totalPixels = bitmap.width * bitmap.height
    val onePercentOfPixels = totalPixels / 100

    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val pixel = bitmap.getPixel(x, y)
            val alpha = pixel ushr 24 and 0xff // Alpha 채널 값 추출

            if (alpha > 128) { // Alpha 값이 128보다 큰 경우만 처리
                val closestCSSColor = findClosestCSSColor(pixel, cssColors).name
                colorFrequencyMap[closestCSSColor] = colorFrequencyMap.getOrDefault(closestCSSColor, 0) + 1
            }
        }
    }

    // 1퍼센트 미만을 차지하는 색상 제외
    val relevantColorFrequencies = colorFrequencyMap.filter { it.value > onePercentOfPixels }

    // 빈도수에 따라 내림차순으로 정렬
    val sortedColorFrequencies = relevantColorFrequencies.toList().sortedByDescending { it.second }

    // 정렬된 고유 CSS 색상과 빈도수 로그에 출력
    sortedColorFrequencies.forEach { (colorName, frequency) ->
        val percentage = (frequency.toDouble() / totalPixels) * 100
        Log.d("UniqueCSSColorSorted", "CSS Color: $colorName, Frequency: ${"%.2f".format(percentage)}%")
    }
}










// 이미지의 색상 분포가 균일한지를 판단하는 함수. 균일한 분포를 가지면 true를, 그렇지 않으면 false를 반환합니다.
fun isUniformColorDistribution(palette: Palette, cssColors: List<CSSColor>, threshold: Float = 0.1f): Boolean {
    // 이미지에서 검출된 색상 swatches를 가져옵니다.
    val swatches = palette.swatches
    // 전체 픽셀 수를 계산합니다.
    val totalPixelCount = swatches.sumOf { it.population }
    // 각 CSS 색상별로, 해당 색상이 이미지에서 차지하는 픽셀 수를 저장할 맵입니다.
    val cssColorCountMap = mutableMapOf<String, Int>()

    // 각 swatch에 대해 반복 처리합니다.
    swatches.forEach { swatch ->
        // 현재 swatch의 색상과 가장 가까운 CSS 색상을 찾습니다.
        val closestCSSColor = findClosestCSSColor(swatch.rgb, cssColors)
        // 이미 맵에 저장된 해당 CSS 색상의 픽셀 수를 가져오거나, 없으면 0을 반환합니다.
        Log.d("맵에 저장된 색상 CSS" ,"$swatch.rgb")
        val currentCount = cssColorCountMap.getOrDefault(closestCSSColor.name, 0)
        // 맵을 업데이트하여, 해당 CSS 색상의 새로운 픽셀 수를 저장합니다.
        cssColorCountMap[closestCSSColor.name] = currentCount + swatch.population

        // 현재 swatch가 전체 이미지에서 차지하는 비율을 계산합니다.
        val populationPercentage = (swatch.population.toFloat() / totalPixelCount) * 100
        // 로그를 출력합니다. 원래 색상, 가장 가까운 CSS 색상, 그리고 비율을 표시합니다.
        Log.d("ColorDistribution", "Original Color: #${Integer.toHexString(swatch.rgb).toUpperCase()} -> " +
                "Closest CSS Color: ${closestCSSColor.name} (#${Integer.toHexString(closestCSSColor.r shl 16 or (closestCSSColor.g shl 8) or closestCSSColor.b).toUpperCase()}), " +
                "Percentage: ${"%.2f".format(populationPercentage)}%")
    }

    // 이미지 내 모든 CSS 색상의 평균 픽셀 수를 계산합니다.
    val averagePopulation = totalPixelCount.toFloat() / cssColorCountMap.size

    // 모든 CSS 색상에 대해, 각 색상이 평균 픽셀 수로부터 주어진 임계값 이내인지 확인합니다.
    // 모든 색상이 이 조건을 만족하면 true, 하나라도 만족하지 않으면 false를 반환합니다.
    return cssColorCountMap.values.all {
        val populationPercentage = (it.toFloat() / totalPixelCount) * 100
        Math.abs(it - averagePopulation) / averagePopulation < threshold
    }
}

fun getDominantColor(bitmap: Bitmap?): Int {
    if (null == bitmap) return Color.TRANSPARENT
    var redBucket = 0
    var greenBucket = 0
    var blueBucket = 0
    var alphaBucket = 0
    val hasAlpha = bitmap.hasAlpha()
    val pixelCount = bitmap.width * bitmap.height
    val pixels = IntArray(pixelCount)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    var y = 0
    val h = bitmap.height
    while (y < h) {
        var x = 0
        val w = bitmap.width
        while (x < w) {
            val color = pixels[x + y * w] // x + y * width
            redBucket += color shr 16 and 0xFF // Color.red
            greenBucket += color shr 8 and 0xFF // Color.greed
            blueBucket += color and 0xFF // Color.blue
            if (hasAlpha) alphaBucket += color ushr 24 // Color.alpha
            x++
        }
        y++
    }
    return Color.argb(
        if (hasAlpha) alphaBucket / pixelCount else 255,
        redBucket / pixelCount,
        greenBucket / pixelCount,
        blueBucket / pixelCount
    )
}


fun findPersonalColorFromImage(bitmap: Bitmap): String {  // 평균값을 바탕으로 퍼스널컬러를 구함
    val averageColor = calculateAverageColor(bitmap)
    return getPersonalColorType(averageColor)
}

fun calculateAverageColor(bitmap: Bitmap): Int {  // 이미지를 구성하는 색상들의 평균을 구함
    var redSum: Long = 0
    var greenSum: Long = 0
    var blueSum: Long = 0
    val pixelCount: Int = bitmap.width * bitmap.height
    for (y in 0 until bitmap.height) {
        for (x in 0 until bitmap.width) {
            val pixel = bitmap.getPixel(x, y)
            redSum += (pixel shr 16) and 0xFF
            greenSum += (pixel shr 8) and 0xFF
            blueSum += pixel and 0xFF
        }
    }

    val redAverage = (redSum / pixelCount).toInt()
    val greenAverage = (greenSum / pixelCount).toInt()
    val blueAverage = (blueSum / pixelCount).toInt()

    return (0xFF shl 24) or (redAverage shl 16) or (greenAverage shl 8) or blueAverage
}