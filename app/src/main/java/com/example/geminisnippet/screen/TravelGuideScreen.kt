package com.example.geminisnippet.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.geminisnippet.R
import com.example.geminisnippet.uistate.TravelGuideUiState
import com.example.geminisnippet.viewmodel.TravelGuideViewModel

@Composable
fun TravelGuideScreen(viewModel: TravelGuideViewModel = viewModel()) {
    val uiState by viewModel.uiState
    TravelGuide(
        uiState = uiState,
        onUpdateShowDialog = { showDialog -> viewModel.updateShowDialog(showDialog) },
        onImageCaptured = { imageBitmap ->
            // TODO: Handle image captured from camera
        },
        onSelectImage = { uri ->
            viewModel.updateImageUri(uri)
        }
    )
}

@Composable
fun TravelGuide(
    uiState: TravelGuideUiState,
    onUpdateShowDialog: (Boolean) -> Unit,
    onImageCaptured: (ImageBitmap) -> Unit,
    onSelectImage: (Uri?) -> Unit
) {
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { imageBitmap ->
        //onImageCaptured(imageBitmap)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onSelectImage(uri)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            if (uiState.imageUri != null) {
                AsyncImage(
                    model = uiState.imageUri,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onUpdateShowDialog(true) }) {
            Text("Capture/Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.analysisResult != null) {
            Text(uiState.analysisResult)
        }

        if (uiState.showDialog) {
            AlertDialog(
                onDismissRequest = { onUpdateShowDialog(false) },
                title = { Text("Choose an option") },
                text = { Text("Take a photo or select from gallery") },
                confirmButton = {
                    Button(onClick = {
                        cameraLauncher.launch()
                        onUpdateShowDialog(false)
                    }) {
                        Text("Take Photo")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        galleryLauncher.launch("image/*")
                        onUpdateShowDialog(false)
                    }) {
                        Text("Choose from Gallery")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TravelGuidePreview() {
    TravelGuide(
        uiState = TravelGuideUiState(),
        onUpdateShowDialog = { },
        onImageCaptured = { },
        onSelectImage = { })
}