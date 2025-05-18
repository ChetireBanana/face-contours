package com.example.facecontours.presentation.facecontours


import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.facecontours.data.local.camera.CameraPermissionHandler
import com.example.facecontours.data.local.camera.ImageAnalyzer
import com.example.facecontours.navigation.Screen
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour


@Composable
fun FaceContoursContainer(navController: NavController) {
    val viewModel: FaceContoursViewModel = hiltViewModel()

    val faces by viewModel.faces

    val analyzer = remember {
        ImageAnalyzer { newFaces ->
            viewModel.updateFaces(newFaces)
        }
    }

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState(initial = true)
    Log.d("FaceContours", "Container Inite isFirstLaunch: $isFirstLaunch")

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.setOnboardingShown()
            navController.navigate(Screen.OnBoarding.route)
        }
    }

    if (!isFirstLaunch) {
        CameraPermissionHandler(
            onPermissionGranted = {
                FaceContours(faces = faces, analyzer = analyzer)
            },
            onPermissionDenied = {
                navController.navigate(Screen.ErrorScreen.route)
            }
        )

    }


}

@Composable
fun FaceContours(
    faces: List<Face>,
    analyzer: ImageAnalysis.Analyzer
) {

    var showContours by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CameraPreview(analyzer = analyzer, faces = faces)

        val borderColor = if (faces.isNotEmpty()) Color.Green else Color.Red

        Box(
            modifier = Modifier
                .matchParentSize()
                .border(4.dp, borderColor)
        ) {

            Button(
                onClick = { showContours = !showContours },
                enabled = faces.isNotEmpty(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(if (!showContours) "Show Contours" else "Hide Contours")
            }
        }

        if (showContours) {
            FaceContoursOverlay(faces = faces)
        }
    }

}

@Composable
fun FaceContoursOverlay(
    faces: List<Face>
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        for (face in faces) {
            val faceContour = face.getContour(FaceContour.FACE)?.points
            if (faceContour != null) {
                for (point in faceContour) {
                    drawCircle(
                        color = Color.Red,
                        radius = 4f,
                        center = Offset(
                            point.x,
                            point.y
                        )
                    )
                }
            }
        }
    }

}


@Composable
fun CameraPreview(analyzer: ImageAnalysis.Analyzer, faces: List<Face>) {
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
