package com.univenn.fireimage.models

data class CreateScreenCallbacks(
    val onPromptChange: (String) -> Unit,
    val onSend: () -> Unit,
    val onEditClick: () -> Unit = {},
    val onAddPhotoClicked: () -> Unit = {},
    val onChatClicked: () -> Unit = {}
) 