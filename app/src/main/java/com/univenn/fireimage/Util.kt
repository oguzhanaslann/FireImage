package com.univenn.fireimage

import android.content.Context
import android.graphics.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap

fun loadAsBitmap(
    url: String,
    context: Context,
    onSuccess: (Bitmap) -> Unit
) {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .listener { request, result ->
            onSuccess(result.image.toBitmap())
        }
        .build()
    imageLoader.enqueue(request)
}
