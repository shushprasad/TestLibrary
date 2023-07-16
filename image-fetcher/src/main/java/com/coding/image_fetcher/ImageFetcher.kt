package com.coding.image_fetcher

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONObject
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageFetcher {

    private val dogImages: MutableList<String> = mutableListOf()
    private var currentIndex: Int = -1

    suspend fun initialize() {
        withContext(Dispatchers.IO) {
            val apiUrl = "https://dog.ceo/api/breeds/image/random" // Fetch 10 dog images
            val response = URL(apiUrl).readText()
            val json = JSONObject(response)
            val status = json.getString("status")
            if (status == "success") {
                val message = json.getJSONArray("message")
                for (i in 0 until message.length()) {
                    dogImages.add(message.getString(i))
                }
            }
        }
    }

    suspend fun getImage(): Bitmap? {
        if (dogImages.isEmpty()) {
            return null
        }
        return withContext(Dispatchers.IO) {
            val imageUrl = dogImages[currentIndex]
            try {
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getImages(number: Int): List<Bitmap?> {
        val images = mutableListOf<Bitmap?>()
        repeat(number) {
            val image = getImage()
            images.add(image)
            getNextImage()
        }
        return images
    }

    fun getNextImage() {
        if (currentIndex < dogImages.size - 1) {
            currentIndex++
        }
    }

    fun getPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--
        }
    }
}
