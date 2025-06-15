package com.univenn.fireimage

import android.graphics.Bitmap

data class Message(
    val text: String,
    val bitmap: Bitmap? = null,
    val isFromMe: Boolean
)