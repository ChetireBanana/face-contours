package com.example.facecontours.presentation.facecontours

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.facecontours.data.local.camera.CameraPermissionHandler
import com.example.facecontours.navigation.Screen


@Composable
fun FaceContoursContainer(navController: NavController) {
    val viewModel: FaceContoursViewModel = hiltViewModel()

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState(initial = true)

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            navController.navigate(Screen.OnBoarding.route)
        }
    }

    if (!isFirstLaunch) {
        CameraPermissionHandler(
            onPermissionGranted = {
                FaceContours()
            },
            onPermissionDenied = {
                navController.navigate(Screen.ErrorScreen.route)
            }
        )

    }


}

@Composable
fun FaceContours(

) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraPreview()
    }

}


@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    AndroidView(factory = { previewView }) { view ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.surfaceProvider = view.surfaceProvider
            }

            val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (e: Exception) {
                Log.e("CameraPreview", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}