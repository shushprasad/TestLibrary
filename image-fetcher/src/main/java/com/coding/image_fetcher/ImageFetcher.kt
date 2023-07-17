package com.coding.image_fetcher

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ImageFetcher {

     val dogImages: MutableList<String> = mutableListOf()
     var currentIndex: Int = -1

    private val dogApiService: DogApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DogApiService::class.java)
    }

    suspend fun initialize(number: Int) {
        withContext(Dispatchers.IO) {
            try {
                dogImages.clear()
                repeat(number) {
                    val response = dogApiService.getRandomDogImage()
                    val imageUrl = response.message
                    dogImages.add(imageUrl)
                }
                currentIndex = 0
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getRandomDogImages(number: Int): List<String> {
        val images = mutableListOf<String>()
        withContext(Dispatchers.IO) {
            repeat(number) {
                try {
                    val response = dogApiService.getRandomDogImage()
                    if (response.status == "success") {
                        val imageUrl = response.message
                        images.add(imageUrl)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return images
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
        if (number > 0) {
            val startIndex = currentIndex
            for (i in 0 until number) {
                val index = (startIndex + i) % dogImages.size
                val imageUrl = dogImages[index]
                val image = loadBitmapFromUrl(imageUrl)
                images.add(image)
            }
        }
        return images
    }

    private suspend fun loadBitmapFromUrl(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
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


