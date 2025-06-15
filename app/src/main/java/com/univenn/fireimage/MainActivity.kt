package com.univenn.fireimage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import com.univenn.fireimage.ui.theme.FireImageTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ImageGenerationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireImageTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = Screen.ImageGeneration.route,
                    modifier = Modifier
                ) {
                    composable(Screen.ImageGeneration.route) {
                        ImageGenerationScreen(
                            viewModel = viewModel,
                            onChatClicked = { navController.navigate(Screen.Chat.route) },
                            modifier = Modifier
                        )
                    }
                    composable(Screen.Chat.route) {
                        ChatScreen(
                            onBackPressed = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGenerationScreen(
    viewModel: ImageGenerationViewModel,
    onChatClicked: () -> Unit,
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
        },
        onChatClicked = onChatClicked
    )
}
