package com.univenn.fireimage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ImageGenerationScreen(
    viewModel: ImageGenerationViewModel,
    onChatClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val photoPickerLauncher = rememberImagePickLauncher { uri: Uri? ->
        uri?.let {
            val url = it.toString()
            loadAsBitmap(url, context) { bitmap ->
                viewModel.handleSelectedImage(bitmap)
            }
        }
    }

    CreateScreen(
        modifier = modifier,
        prompt = uiState.prompt,
        image = uiState.bitmap,
        isLoading = uiState.isLoading,
        isEditing = uiState.isEditing,
        onPromptChange = { viewModel.setPrompt(it) },
        onSend = {
            if (uiState.isEditing) {
                viewModel.editImage()
            } else {
                viewModel.generateImage()
            }
        },
        onEditClick = { viewModel.toggleEditMode() },
        onAddPhotoClicked = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onChatClicked = onChatClicked
    )
}
