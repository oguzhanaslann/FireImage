package com.univenn.fireimage.screens.chat

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.univenn.fireimage.R
import com.univenn.fireimage.models.Message
import com.univenn.fireimage.utils.AutoSendCloseOutlinedTextField
import com.univenn.fireimage.utils.loadAsBitmap
import com.univenn.fireimage.utils.rememberImagePickLauncher

@Composable
fun ChatScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = ChatViewModel()
) {
    val messageText by viewModel.messageText.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val selectedImageToSend by viewModel.selectedImageToSend.collectAsState()

    val context = LocalContext.current

    val photoPickerLauncher = rememberImagePickLauncher { uri: Uri? ->
        uri?.let {
            val url = it.toString()
            loadAsBitmap(url, context) { bitmap ->
                viewModel.setSelectedImage(bitmap)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startChat()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF1A1A1A),
        topBar = { TopBar(onBackPressed = onBackPressed) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MessagesList(
                modifier = Modifier.weight(1f),
                messages = messages
            )

            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            InsertedImage(selectedImageToSend, viewModel)
            InputSection(photoPickerLauncher, messageText, viewModel)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MessagesList(
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.scrollToVeryBottom(messages)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        items(messages) { message ->
            MessageBubble(
                modifier = Modifier,
                message = message
            )
        }
    }
}

private suspend fun LazyListState.scrollToVeryBottom(
    messages: List<Message>
) {
    animateScrollToItem(
        index = messages.size - 1,
        scrollOffset = 0
    )
}

@Composable
private fun InputSection(
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    messageText: String,
    viewModel: ChatViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_photo_camera_24),
                contentDescription = stringResource(R.string.add_image),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        AutoSendCloseOutlinedTextField(
            value = messageText,
            onValueChange = { viewModel.setMessageText(it) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            placeholder = { Text(stringResource(R.string.type_a_message), color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            onSend = { viewModel.sendMessage() }
        )

        IconButton(
            onClick = { viewModel.sendMessage() }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun ColumnScope.InsertedImage(
    selectedImageToSend: Bitmap?,
    viewModel: ChatViewModel
) {
    AnimatedVisibility(
        visible = selectedImageToSend != null,
        enter = expandVertically(
            expandFrom = Alignment.Bottom
        ) + fadeIn(initialAlpha = 0.3f),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Bottom
        ) + fadeOut()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            selectedImageToSend?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = stringResource(R.string.selected_image),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.selected_image),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { viewModel.setSelectedImage(null) }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.remove_image),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                stringResource(R.string.chat),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
    )
}

@Composable
fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = if (message.isFromMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (message.isFromMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (message.isFromMe) 12.dp else 0.dp,
                        bottomEnd = if (message.isFromMe) 0.dp else 12.dp
                    )
                )
                .padding(8.dp)
        ) {
            Column {
                message.bitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = stringResource(R.string.message_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                if (message.bitmap != null && message.text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                message.text
                    .takeUnless(String::isEmpty)
                    ?.let {
                        Text(
                            text = message.text,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(
        onBackPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun MessageBubblesPreview() {
    // Create a sample bitmap for preview
    val sampleBitmap = Bitmap.createBitmap(300, 200, Bitmap.Config.ARGB_8888).apply {
        val canvas = Canvas(this)
        canvas.drawColor(android.graphics.Color.BLUE)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // User message
        MessageBubble(
            message = Message(
                text = "Hello! How are you?",
                isFromMe = true
            )
        )

        // Message with image
        MessageBubble(
            message = Message(
                text = "Check out this image!",
                isFromMe = true,
                bitmap = sampleBitmap
            )
        )

        // Other party message
        MessageBubble(
            message = Message(
                text = "I'm doing great, thanks for asking!",
                isFromMe = false
            )
        )

        // Message with image and empty text
        MessageBubble(
            message = Message(
                text = "",
                isFromMe = false,
                bitmap = sampleBitmap
            )
        )
    }
}