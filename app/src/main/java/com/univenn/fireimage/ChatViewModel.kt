package com.univenn.fireimage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ai.Chat
import com.google.firebase.ai.type.asImageOrNull
import com.google.firebase.ai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(
        listOf(
            Message(
                "Hi! I'm your AI image generation assistant. I can help you create, edit, and transform images based on your descriptions. What would you like to create today?",
                isFromMe = false
            ),
        )
    )
    val messages: StateFlow<List<Message>> = _messages

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText

    private val _selectedImageToSend = MutableStateFlow<Bitmap?>(null)
    val selectedImageToSend: StateFlow<Bitmap?> = _selectedImageToSend

    var chat: Chat? = null

    fun setMessageText(text: String) {
        _messageText.value = text
    }

    fun setSelectedImage(bitmap: Bitmap?) {
        _selectedImageToSend.value = bitmap
    }

    fun sendMessage() {
        val chat = chat ?: return

        val prompt = messageText.value.takeUnless { it.isEmpty() } ?: return
        val image = selectedImageToSend.value
        viewModelScope.launch {

            _messages.value += Message(
                text = prompt,
                isFromMe = true,
                bitmap = image
            )
            _messageText.value = ""
            _selectedImageToSend.value = null

            val response = chat.sendMessage(
                content {
                    image?.let(::image)
                    text(prompt)
                }
            )

            val generatedImageAsBitmap = response
                .candidates
                .firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.asImageOrNull()

            _messages.value += Message(
                text = response.text.toString().trim(),
                isFromMe = false,
                bitmap = generatedImageAsBitmap
            )
        }
    }

    fun startChat() {
        val model = generatorModel()
        chat = model.startChat(
            history = _messages.value.map {
                content(
                    if (it.isFromMe) {
                        "user"
                    } else {
                        "model"
                    }
                ) {
                    it.bitmap?.let(::image)
                    text(it.text)
                }
            }
        )
    }
} 