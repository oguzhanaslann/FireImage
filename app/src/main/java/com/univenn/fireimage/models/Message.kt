package com.univenn.fireimage.models

import android.graphics.Bitmap

data class Message(
    val text: String,
    val isFromMe: Boolean = false,
    val bitmap: Bitmap? = null
) 