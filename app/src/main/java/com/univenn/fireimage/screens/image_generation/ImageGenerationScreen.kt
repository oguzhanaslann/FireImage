package com.univenn.fireimage.screens.image_generation

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.univenn.fireimage.utils.loadAsBitmap
import com.univenn.fireimage.models.CreateScreenCallbacks
import com.univenn.fireimage.models.CreateScreenUiState
import com.univenn.fireimage.models.ImageGenerationUiState
import com.univenn.fireimage.utils.rememberImagePickLauncher

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

    val createScreenCallbacks = getCreateScreenCallbacks(viewModel, uiState, photoPickerLauncher, onChatClicked)

    CreateScreen(
        modifier = modifier,
        uiState = uiState.toCreateScreenUiState(),
        callbacks = createScreenCallbacks
    )
}

fun ImageGenerationUiState.toCreateScreenUiState(): CreateScreenUiState {
    return CreateScreenUiState(
        prompt = prompt,
        image = bitmap,
        isLoading = isLoading,
        isEditing = isEditing
    )
}

@Composable
private fun getCreateScreenCallbacks(
    viewModel: ImageGenerationViewModel,
    uiState: ImageGenerationUiState,
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    onChatClicked: () -> Unit
): CreateScreenCallbacks {
    val createScreenCallbacks = CreateScreenCallbacks(
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
    return createScreenCallbacks
}