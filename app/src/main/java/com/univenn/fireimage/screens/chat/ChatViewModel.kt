package com.univenn.fireimage.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univenn.fireimage.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _selectedImage = MutableStateFlow<String?>(null)
    val selectedImage: StateFlow<String?> = _selectedImage.asStateFlow()

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val message = Message(
                text = text,
                isFromMe = true,
                imageUrl = _selectedImage.value
            )
            _messages.value = _messages.value + message

            // Clear selected image after sending
            _selectedImage.value = null

            // Simulate response after 1 second
            kotlinx.coroutines.delay(1000)
            val response = Message(
                text = "I received your message: $text",
                isFromMe = false
            )
            _messages.value = _messages.value + response
        }
    }

    fun setSelectedImage(url: String?) {
        _selectedImage.value = url
    }
} 