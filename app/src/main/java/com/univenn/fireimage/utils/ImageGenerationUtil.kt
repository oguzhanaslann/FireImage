package com.univenn.fireimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.io.ByteArrayOutputStream

object ImageGenerationUtil {
    private val functions: FirebaseFunctions = Firebase.functions

    suspend fun generateImage(prompt: String): Bitmap {
        val data = hashMapOf(
            "prompt" to prompt
        )

        val result = functions
            .getHttpsCallable("generateImage")
            .call(data)
            .await()

        val imageString = (result.data as HashMap<*, *>)["image"] as String
        return base64ToBitmap(imageString)
    }

    suspend fun editImage(prompt: String, image: Bitmap): Bitmap {
        val imageString = bitmapToBase64(image)

        val data = hashMapOf(
            "prompt" to prompt,
            "image" to imageString
        )

        val result = functions
            .getHttpsCallable("editImage")
            .call(data)
            .await()

        val editedImageString = (result.data as HashMap<*, *>)["image"] as String
        return base64ToBitmap(editedImageString)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
} 