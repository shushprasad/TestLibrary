package com.coding.image_fetcher

import org.junit.Test
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*


class ImageFetcherTest {

    @Test
    fun testInitialize() = runBlocking {
        val imageFetcher = ImageFetcher()
        imageFetcher.initialize(5)
        assertEquals(5, imageFetcher.dogImages.size)
        assertEquals(0, imageFetcher.currentIndex)
    }

    @Test
    fun testGetNextImage() {
        val imageFetcher = ImageFetcher()
        imageFetcher.dogImages.addAll(listOf("image1", "image2", "image3"))
        imageFetcher.currentIndex = 1
        imageFetcher.getNextImage()
        assertEquals(2, imageFetcher.currentIndex)
    }

    @Test
    fun testGetPreviousImage() {
        val imageFetcher = ImageFetcher()
        imageFetcher.currentIndex = 2
        imageFetcher.getPreviousImage()
        assertEquals(1, imageFetcher.currentIndex)
    }
}
