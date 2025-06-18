package com.univenn.fireimage

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageGenerationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ImageGenerationUiState())
    val uiState: StateFlow<ImageGenerationUiState> = _uiState.asStateFlow()

    fun handleSelectedImage(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(bitmap = bitmap)
    }

    fun generateImage() {
        val prompt = _uiState.value.prompt
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val generatedBitmap = geminiImageGen(prompt)
            _uiState.value = _uiState.value.copy(
                bitmap = generatedBitmap,
                isLoading = false,
                prompt = ""
            )
        }
    }

    fun setPrompt(newPrompt: String) {
        _uiState.value = _uiState.value.copy(prompt = newPrompt)
    }

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(
            isEditing = !_uiState.value.isEditing,
            prompt = ""
        )
    }

    private fun resetPrompt() {
        _uiState.value = _uiState.value.copy(prompt = "")
    }

    fun editImage() {
        val currentState = _uiState.value
        val bitmap = currentState.bitmap ?: return
        val prompt = currentState.prompt
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true)
            val editedBitmap = editImage(prompt, bitmap)
            _uiState.value = currentState.copy(
                bitmap = editedBitmap,
                isLoading = false,
                prompt = ""
            )
        }
    }
}