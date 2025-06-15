package com.univenn.fireimage

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.univenn.fireimage.ui.theme.FireImageTheme

@Composable
fun CreateScreen(
    prompt: String,
    image: Bitmap?,
    onPromptChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEditing: Boolean = false,
    onEditClick: () -> Unit = {},
    onAddPhotoClicked: () -> Unit = {},
    onChatClicked: () -> Unit = {}
) {
    Scaffold(
        contentColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                canAddPhoto = image != null,
                onAddPhotoClicked = onAddPhotoClicked,
                onChatClicked = onChatClicked
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
                image = image,
                isEditing = isEditing,
                isLoading = isLoading,
                onEditClick = onEditClick,
                onAddPhotoClicked = onAddPhotoClicked
            )
            Spacer(modifier = Modifier.weight(1f))
            InputSection(prompt, onPromptChange, onSend)
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
        title = { Text("Create", style = MaterialTheme.typography.headlineMedium) },
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
                   contentDescription = "Chat Section",
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
            contentDescription = "Upload",
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Gray)
            .border(1.dp, Color.White, RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        image?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Generated image",
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
        contentDescription = "Add Image",
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
            contentDescription = "Edit image",
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
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = prompt,
            onValueChange = onPromptChange,
            placeholder = {
                Text(
                    "Enter a prompt",
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
            keyboardActions = KeyboardActions(
                onSend = {
                    onSend()
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            )
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
                "Send",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    FireImageTheme {
        CreateScreen(
            prompt = "Sample prompt",
            image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            onPromptChange = {},
            onSend = {},
            isLoading = false,
            isEditing = true,
            onEditClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenLoadingPreview() {
    FireImageTheme {
        CreateScreen(
            prompt = "Generating a beautiful sunset...",
            image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            onPromptChange = {},
            onSend = {},
            isLoading = true,
            isEditing = false,
            onEditClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenEmptyStatePreview() {
    FireImageTheme {
        CreateScreen(
            prompt = "",
            image = null,
            onPromptChange = {},
            onSend = {},
            isLoading = false,
            isEditing = false,
            onEditClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenNormalStatePreview() {
    FireImageTheme {
        CreateScreen(
            prompt = "A beautiful mountain landscape",
            image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            onPromptChange = {},
            onSend = {},
            isLoading = false,
            isEditing = false,
            onEditClick = {}
        )
    }
}
