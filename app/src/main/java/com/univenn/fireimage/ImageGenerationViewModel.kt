package com.univenn.fireimage

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageGenerationViewModel: ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap
    private val _prompt = MutableStateFlow("")
    val prompt: StateFlow<String> = _prompt
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    fun handleSelectedImage(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun generateImage() {
        val prompt = _prompt.value
        viewModelScope.launch {
            _isLoading.value = true
            _bitmap.value = geminiImageGen(prompt)
            _isLoading.value = false
        }
    }

    fun setPrompt(newPrompt: String) {
        _prompt.value = newPrompt
    }

    fun toggleEditMode() {
        _isEditing.value = !_isEditing.value
        resetPrompt()
    }

    private fun resetPrompt() {
        _prompt.value = ""
    }

    fun editImage() {
        val bitmap = _bitmap.value ?: return
        val prompt = _prompt.value
        viewModelScope.launch {
            _isLoading.value = true
            _bitmap.value = editImage(prompt, bitmap)
            _isLoading.value = false
            resetPrompt()
        }
    }
}