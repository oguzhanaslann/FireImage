package com.univenn.fireimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.univenn.fireimage.ui.theme.FireImageTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageGenerationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireImageTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()) { innerPadding ->
                    ImageGenerationScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
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
        onEditClick = { viewModel.toggleEditMode() }
    )
}

/**
 * The Firebase AI Logic SDKs support image generation using either a Gemini model or an Imagen model. For most use cases, start with Gemini, and then choose Imagen for specialized tasks where image quality is critical.
 * No Image Input
 */
// multi turn chat


