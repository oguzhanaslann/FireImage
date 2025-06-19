package com.univenn.fireimage.screens.image_generation

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.univenn.fireimage.R
import com.univenn.fireimage.models.CreateScreenCallbacks
import com.univenn.fireimage.models.CreateScreenUiState
import com.univenn.fireimage.ui.theme.FireImageTheme
import androidx.core.graphics.createBitmap
import com.univenn.fireimage.utils.AutoSendCloseOutlinedTextField

@Composable
fun CreateScreen(
    uiState: CreateScreenUiState,
    callbacks: CreateScreenCallbacks,
    modifier: Modifier = Modifier
) {
    Scaffold(
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                canAddPhoto = uiState.image != null,
                onAddPhotoClicked = callbacks.onAddPhotoClicked,
                onChatClicked = callbacks.onChatClicked
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GeneratedImageSection(
                image = uiState.image,
                isEditing = uiState.isEditing,
                isLoading = uiState.isLoading,
                onEditClick = callbacks.onEditClick,
                onAddPhotoClicked = callbacks.onAddPhotoClicked
            )
            Spacer(modifier = Modifier.weight(1f))
            InputSection(
                prompt = uiState.prompt,
                onPromptChange = callbacks.onPromptChange,
                onSend = callbacks.onSend
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    canAddPhoto: Boolean,
    onAddPhotoClicked: () -> Unit,
    onChatClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(stringResource(R.string.create), style = MaterialTheme.typography.headlineMedium) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        actions = {
            AnimatedVisibility(visible = canAddPhoto) {
                AddPhotoButton(onAddPhotoClicked)
            }

           IconButton(
               onClick = onChatClicked
           ) {
               Icon(
                   painter = painterResource(R.drawable.baseline_chat_bubble_outline_24),
                   contentDescription = stringResource(R.string.chat_section),
                   tint = Color.White
               )
           }
        }
    )
}

@Composable
private fun AddPhotoButton(onAddPhotoClicked: () -> Unit) {
    IconButton(onClick = { onAddPhotoClicked() }) {
        Icon(
            painter = painterResource(R.drawable.baseline_upload_24),
            contentDescription = stringResource(R.string.upload),
            tint = Color.White
        )
    }
}

@Composable
private fun GeneratedImageSection(
    image: Bitmap?,
    isEditing: Boolean,
    isLoading: Boolean,
    onEditClick: () -> Unit,
    onAddPhotoClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape)
            .background(Color.Gray)
            .border(1.dp, Color.White, shape),
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = stringResource(R.string.generated_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            EditButton(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd),
                onEditClick = onEditClick,
                isEditing = isEditing
            )
        } ?: AddPhotoIcon(onAddPhotoClicked)

        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun AddPhotoIcon(onAddPhotoClicked: () -> Unit) {
    Icon(
        modifier = Modifier
            .clickable { onAddPhotoClicked() }
            .padding(20.dp)
            .sizeIn(minWidth = 64.dp, minHeight = 64.dp),
        painter = painterResource(id = R.drawable.baseline_add_photo_alternate_24),
        contentDescription = stringResource(R.string.add_image),
        tint = Color.White
    )
}

@Composable
private fun EditButton(
    onEditClick: () -> Unit,
    isEditing: Boolean,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onEditClick,
        modifier = modifier
            .size(40.dp)
            .background(Color(0xFF1A1A1A).copy(alpha = 0.7f), CircleShape)
            .then(
                if (isEditing) {
                    Modifier.border(
                        width = 2.dp,
                        color = Color(0xFF0088FF),
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )

    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.edit_image),
            tint = if (isEditing) Color(0xFF0088FF) else Color.White
        )
    }
}

@Composable
private fun InputSection(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AutoSendCloseOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = prompt,
            onValueChange = onPromptChange,
            placeholder = {
                Text(
                    stringResource(R.string.enter_a_prompt),
                    color = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFF5A5A5A),
                focusedBorderColor = Color(0xFF5A5A5A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background
            ),
           onSend = onSend
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSend,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                stringResource(R.string.send),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputSectionPreview() {
    FireImageTheme {
        InputSection(
            prompt = "Sample input text",
            onPromptChange = {},
            onSend = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    FireImageTheme {
        CreateScreen(
            uiState = CreateScreenUiState(
                prompt = "Sample prompt",
                image = createDummyBitmap(),
                isLoading = false,
                isEditing = true
            ),
            callbacks = createNoOpCallbacks()
        )
    }
}

private fun createDummyBitmap() = createBitmap(100, 100)

private fun createNoOpCallbacks() = CreateScreenCallbacks(
    onPromptChange = {},
    onSend = {},
    onEditClick = {},
    onAddPhotoClicked = {},
    onChatClicked = {}
)

@Preview(showBackground = true)
@Composable
fun CreateScreenLoadingPreview() {
    FireImageTheme {
        CreateScreen(
            uiState = CreateScreenUiState(
                prompt = "Generating a beautiful sunset...",
                image = createDummyBitmap(),
                isLoading = true,
                isEditing = false
            ),
            callbacks = createNoOpCallbacks()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenEmptyStatePreview() {
    FireImageTheme {
        CreateScreen(
            uiState = CreateScreenUiState(
                prompt = "",
                image = null,
                isLoading = false,
                isEditing = false
            ),
            callbacks = createNoOpCallbacks()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenNormalStatePreview() {
    FireImageTheme {
        CreateScreen(
            uiState = CreateScreenUiState(
                prompt = "A beautiful mountain landscape",
                image = createDummyBitmap(),
                isLoading = false,
                isEditing = false
            ),
            callbacks = createNoOpCallbacks()
        )
    }
}
