package com.univenn.fireimage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.univenn.fireimage.R
import java.io.ByteArrayOutputStream

fun loadAsBitmap(url: String, context: Context, onBitmapLoaded: (Bitmap) -> Unit) {
    val uri = Uri.parse(url)
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    onBitmapLoaded(bitmap)
}

@Composable
fun rememberImagePickLauncher(onImagePicked: (Uri?) -> Unit): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImagePicked(uri) }
    )
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}


@Composable
fun AutoSendCloseOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        shape = shape,
        colors = colors,
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
}