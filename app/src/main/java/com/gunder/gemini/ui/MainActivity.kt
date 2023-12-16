package com.gunder.gemini.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.gunder.gemini.ui.theme.GeminiTheme
import com.gunder.gemini.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()
        setContent {
            GeminiTheme {
                val promptText by remember {
                    mutableStateOf("")
                }
                val conversations = mainViewModel.conversations
                val isGenerating by mainViewModel.isGenerating
                val keyboardController = LocalSoftwareKeyboardController.current
                val imageBitmaps: SnapshotStateList<Bitmap> = remember {
                    mutableStateListOf()
                }
                val context = LocalContext.current
                val photoPicker =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
                        uris.forEach { uri ->
                            imageBitmaps.add(
                                MediaStore.Images.Media.getBitmap(
                                    context.contentResolver,
                                    uri
                                )
                            )
                        }
                    }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = "Gemini Bot") }) }, bottomBar = {}) {

                    }
                }
            }
        }
    }
}
