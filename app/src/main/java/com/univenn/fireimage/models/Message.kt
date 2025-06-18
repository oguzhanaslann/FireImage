package com.univenn.fireimage.models

data class Message(
    val text: String,
    val isFromMe: Boolean = false,
    val imageUrl: String? = null
) 