package com.coding.image_fetcher.Api

import com.coding.image_fetcher.Api.Response.DogApiResponse
import retrofit2.http.GET

interface DogApiService {
    @GET("breeds/image/random")
    suspend fun getRandomDogImage(): DogApiResponse
}
