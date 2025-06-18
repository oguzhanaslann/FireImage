package com.univenn.fireimage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
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

@Composable
fun rememberImagePickLauncher(
    onResult: (Uri?) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = onResult
)