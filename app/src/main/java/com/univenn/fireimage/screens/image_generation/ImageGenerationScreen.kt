package com.univenn.fireimage.screens.image_generation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.univenn.fireimage.screens.create.CreateScreen
import com.univenn.fireimage.models.CreateScreenCallbacks
import com.univenn.fireimage.models.CreateScreenUiState
import com.univenn.fireimage.utils.loadAsBitmap
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

    val createScreenState = CreateScreenUiState(
        prompt = uiState.prompt,
        image = uiState.bitmap,
        isLoading = uiState.isLoading,
        isEditing = uiState.isEditing
    )

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

    CreateScreen(
        modifier = modifier,
        uiState = createScreenState,
        callbacks = createScreenCallbacks
    )
} 