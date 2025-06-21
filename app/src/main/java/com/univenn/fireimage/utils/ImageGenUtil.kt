package com.univenn.fireimage.utils

import android.graphics.Bitmap
import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerateContentResponse
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.ImagenGenerationConfig
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.ResponseModality
import com.google.firebase.ai.type.TextPart
import com.google.firebase.ai.type.asImageOrNull
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig

suspend fun predict(prompt: String): String? {
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.0-flash")
    val response = model.generateContent(prompt)
    return response.text
}

suspend fun geminiImageGen(
    prompt: String,
    model: GenerativeModel = generatorModel()
): Bitmap {
    val generatedImageAsBitmap = model.generateContent(prompt)
        .candidates
        .first()
        .content
        .parts
        .firstNotNullOf { it.asImageOrNull() }

    return generatedImageAsBitmap
}

fun generatorModel(
    systemInstructions: String? = null
): GenerativeModel = Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
    modelName = "gemini-2.0-flash-preview-image-generation",
    // Configure the model to respond with text and images
    generationConfig = generationConfig {
        responseModalities = listOf(ResponseModality.TEXT, ResponseModality.IMAGE)
    },
    systemInstruction = systemInstructions?.let {
        content {
            text(systemInstructions)
        }
    }
)

suspend fun geminiImageGenAndText(
    prompt: String,
    model: GenerativeModel = generatorModel()
): Pair<Bitmap?, String?> {

    val responseContent = model.generateContent(prompt)
        .candidates
        .first()
        .content

    var generatedImageAsBitmap: Bitmap? = null
    var text: String? = null
    responseContent.parts.forEach { part ->
        when (part) {
            is ImagePart -> generatedImageAsBitmap = part.asImageOrNull()
            is TextPart -> text = part.text
        }
    }

    return generatedImageAsBitmap to text
}

suspend fun editImage(
    prompt: String,
    bitmap: Bitmap,
    model: GenerativeModel = generatorModel()
): Bitmap? {

    val promptContext = content {
        image(bitmap)
        text(prompt)
    }

    val generatedImageAsBitmap = model.generateContent(promptContext)
    generatedImageAsBitmap.text
    return generatedImageAsBitmap.image
}

suspend fun geminiImageGen(
    prompt: String,
    image: Bitmap?,
    model: GenerativeModel = generatorModel()
): Bitmap? {
    val promptContext = content {
        image?.let(::image)
        text(prompt)
    }

    val generatedImageAsBitmap = model.generateContent(promptContext)
    return generatedImageAsBitmap.image
}


val GenerateContentResponse.image: Bitmap?
    get() {
        val imagePart = candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.filterIsInstance<ImagePart>()
            ?.firstOrNull()
        return imagePart?.image
    }


suspend fun geminiImageGenAndText(
    prompt: String,
    image: Bitmap?,
    model: GenerativeModel = generatorModel()
): Pair<Bitmap?, String?> {

    val promptContext = content {
        image?.let(::image)
        text(prompt)
    }

    val responseContent = model.generateContent(promptContext)
    return responseContent.image to responseContent.text
}


/**
 * Imagen API is only accessible to billed users at this time.
 * @param prompt String
 * @return Bitmap
 */
@OptIn(PublicPreviewAPI::class)
suspend fun imageGen(prompt: String): Bitmap {
    val imagenModel =
        Firebase.ai(backend = GenerativeBackend.googleAI()).imagenModel("imagen-3.0-generate-002")
    val imageResponse = imagenModel.generateImages(prompt)
    val image = imageResponse.images.first()
    val bitmapImage = image.asBitmap()
    return bitmapImage
}

@OptIn(PublicPreviewAPI::class)
suspend fun imageGenMultiple(
    prompt: String,
    count: Int
): List<Bitmap> {
    val imagenModel = Firebase.ai(backend = GenerativeBackend.googleAI()).imagenModel(
        modelName = "imagen-3.0-generate-002",
        generationConfig = ImagenGenerationConfig(numberOfImages = count)
    )

    val imageResponse = imagenModel.generateImages(prompt)

    return imageResponse.images.map { it.asBitmap() }
}