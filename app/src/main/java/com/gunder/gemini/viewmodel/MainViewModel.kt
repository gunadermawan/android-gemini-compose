package com.gunder.gemini.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val apiKey = "AIzaSyAE66Eni7v7cnIVF00kX_iipW3yLy4zYf0"
    private val geminiProModel by lazy {
        GenerativeModel(modelName = "gemini-pro", apiKey = apiKey).apply { startChat() }
    }
    private val geminiProVisionModel by lazy {
        GenerativeModel(modelName = "gemini-pro-vision", apiKey = apiKey)
    }

    val isGenerating = mutableStateOf(false)
    val conversations = mutableListOf<Triple<String, String, List<Bitmap>?>>()

    fun sendText(txtPrompt: String, img: SnapshotStateList<Bitmap>) {
        isGenerating.value = true
        conversations.add(
            Triple(
                "Sent", txtPrompt, img.toList()
            )
        )
        conversations.add(Triple("received", "", null))
        val generativeModel = if (img.isNotEmpty()) geminiProVisionModel else geminiProModel
        val inputContent = content {
            img.forEach { imgBitmap ->
                image(imgBitmap)
            }
            text(txtPrompt)
        }
        viewModelScope.launch {
            generativeModel.generateContentStream(inputContent)
                .collect { it ->
                    conversations[conversations.lastIndex] =
                        Triple("received", conversations.last().second + it.text, null)
                }
            isGenerating.value = false
        }
    }
}
