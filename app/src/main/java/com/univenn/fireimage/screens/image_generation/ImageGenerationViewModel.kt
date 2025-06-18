package com.univenn.fireimage.screens.image_generation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univenn.fireimage.models.ImageGenerationUiState
import com.univenn.fireimage.utils.ImageGenerationUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageGenerationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ImageGenerationUiState())
    val uiState: StateFlow<ImageGenerationUiState> = _uiState.asStateFlow()

    fun setPrompt(prompt: String) {
        _uiState.value = _uiState.value.copy(prompt = prompt)
    }

    fun handleSelectedImage(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(bitmap = bitmap)
    }

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(isEditing = !_uiState.value.isEditing)
    }

    fun generateImage() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val bitmap = ImageGenerationUtil.generateImage(_uiState.value.prompt)
                _uiState.value = _uiState.value.copy(bitmap = bitmap)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun editImage() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val bitmap = ImageGenerationUtil.editImage(
                    _uiState.value.prompt,
                    _uiState.value.bitmap!!
                )
                _uiState.value = _uiState.value.copy(bitmap = bitmap)
            } catch (e: Exception) {
                // Handle error
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
} 