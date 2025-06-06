package com.univenn.fireimage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun editImage() {
        val bitmap = _bitmap.value ?: return
        val prompt = _prompt.value
        viewModelScope.launch {
            _isLoading.value = true
            _bitmap.value = editImage(prompt, bitmap)
            _isLoading.value = false
        }
    }
}