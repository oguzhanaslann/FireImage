package com.univenn.fireimage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.toBitmap
import com.univenn.fireimage.ui.theme.FireImageTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageGenerationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.setContext(this)
        setContent {
            FireImageTheme {
                ImageGenerationScreen(
                    viewModel = viewModel,
                    modifier = Modifier
                )
            }
        }
    }
}

fun loadAsBitmap(
    url: String,
    context: Context,
    onSuccess: (Bitmap) -> Unit
) {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .listener { request, result ->
            onSuccess(result.image.toBitmap())
        }
        .build()
    imageLoader.enqueue(request)
}

@Composable
fun ImageGenerationScreen(
    viewModel: ImageGenerationViewModel,
    modifier: Modifier = Modifier
) {
    val prompt by viewModel.prompt.collectAsStateWithLifecycle()
    val bitmap by viewModel.bitmap.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val url = it.toString()
            loadAsBitmap(url, context) { bitmap ->
                viewModel.handleSelectedImage(bitmap)
            }
        }
    }

    CreateScreen(
        modifier = modifier,
        prompt = prompt,
        image = bitmap,
        isLoading = isLoading,
        isEditing = isEditing,
        onPromptChange = { viewModel.setPrompt(it) },
        onSend = { 
            if (isEditing) {
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
        }
    )
}

/**
 * The Firebase AI Logic SDKs support image generation using either a Gemini model or an Imagen model. For most use cases, start with Gemini, and then choose Imagen for specialized tasks where image quality is critical.
 * No Image Input
 */
// multi turn chat


