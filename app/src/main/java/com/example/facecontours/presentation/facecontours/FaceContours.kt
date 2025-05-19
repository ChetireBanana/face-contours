package com.example.facecontours.presentation.facecontours


import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.facecontours.R
import com.example.facecontours.data.local.camera.CameraPermissionHandler
import com.example.facecontours.data.local.camera.ImageAnalyzer
import com.example.facecontours.navigation.Screen
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import kotlin.math.absoluteValue


@Composable
fun FaceContoursContainer(navController: NavController) {
    val viewModel: FaceContoursViewModel = hiltViewModel()

    val faces by viewModel.faces
    val imageSize by viewModel.imageSize

    val analyzer = remember {
        ImageAnalyzer { newFaces, width, height ->
            viewModel.updateFaces(newFaces)
            viewModel.updateImageSize(width, height)
        }
    }

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState(initial = true)

    LaunchedEffect(isFirstLaunch) {
        if (isFirstLaunch) {
            viewModel.setOnboardingShown()
            navController.navigate(Screen.OnBoarding.route)
        }
    }

    if (!isFirstLaunch) {
        CameraPermissionHandler(
            onPermissionGranted = {
                FaceContours(faces = faces, analyzer = analyzer, imageSize = imageSize)
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
    analyzer: ImageAnalysis.Analyzer,
    imageSize: Size,
    isInPreview: Boolean = false
) {

    var showContours by rememberSaveable { mutableStateOf(false) }
    var showGlass by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!isInPreview) {
            CameraPreview(analyzer = analyzer)
        }

        val borderColor = if (faces.isNotEmpty()) Color.Green else Color.Red

        Box(
            modifier = Modifier
                .matchParentSize()
                .border(4.dp, borderColor)
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { showContours = !showContours },
                    enabled = faces.isNotEmpty(),
                ) {
                    Text(if (!showContours) "Show Contours" else "Hide Contours")
                }

                Button(
                    onClick = { showGlass = !showGlass },
                    enabled = faces.isNotEmpty(),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(if (!showGlass) "Show Glasses" else "Hide Glasses")
                }
            }


        }

        if (showContours && !isInPreview) {
            FaceContoursOverlay(faces = faces, imageSize = imageSize)
        }

        if (showGlass && !isInPreview) {
            WearGlassesOverlay(faces = faces, imageSize = imageSize)
        }
    }

}

@Composable
fun FaceContoursOverlay(
    faces: List<Face>,
    imageSize: Size
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height
        val imageWidth = imageSize.width
        val imageHeight = imageSize.height

        for (face in faces) {
            val faceContour = face.getContour(FaceContour.FACE)?.points
            if (faceContour != null) {
                for (point in faceContour) {
                    val translatedPoint = translatePoint(
                        pointF = point,
                        imageWidth = imageWidth,
                        imageHeight = imageHeight,
                        canvasWidth = canvasWidth,
                        canvasHeight = canvasHeight,
                        isFrontCamera = true
                    )

                    drawCircle(
                        color = Color.Red,
                        radius = 4f,
                        center = translatedPoint
                    )
                }
            }
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun WearGlassesOverlay(
    faces: List<Face>,
    imageSize: Size
) {
    val glassesBitmap = ImageBitmap.imageResource(id = R.drawable.cobra_glass)


    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        val canvasWidth = size.width
        val canvasHeight = size.height
        val imageWidth = imageSize.width
        val imageHeight = imageSize.height
        for (face in faces) {
            val faceContourPoints = face.getContour(FaceContour.FACE)?.points

            if (faceContourPoints != null && faceContourPoints.size > 29) {
                val leftPoint = faceContourPoints[7]
                val rightPoint = faceContourPoints[29]

                val left = translatePoint(
                    pointF = leftPoint,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    isFrontCamera = true,
                )
                val right = translatePoint(
                    pointF = rightPoint,
                    imageWidth = imageWidth,
                    imageHeight = imageHeight,
                    canvasWidth = canvasWidth,
                    canvasHeight = canvasHeight,
                    isFrontCamera = true
                )

                val glassesWidth = (right.x - left.x).absoluteValue
                val glassesHeight =
                    glassesWidth * (glassesBitmap.height.toFloat() / glassesBitmap.width.toFloat())
                val topLeft = Offset(left.x, left.y - glassesHeight / 2)
                val dstOffset = IntOffset(topLeft.x.toInt(), topLeft.y.toInt())

                drawImage(
                    image = glassesBitmap,
                    dstOffset = dstOffset,
                    dstSize = IntSize(glassesWidth.toInt(), glassesHeight.toInt())
                )
            }
        }
    }
}


fun translatePoint(
    pointF: PointF,
    imageWidth: Float,
    imageHeight: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    isFrontCamera: Boolean,
): Offset {

    val scale = minOf(canvasWidth / imageWidth, canvasHeight / imageHeight)

    val dX = (canvasWidth - imageWidth * scale) / 2
    val dY = (canvasHeight - imageHeight * scale) / 2

    val x = if (isFrontCamera) {
        canvasWidth - (pointF.x * scale + dX)
    } else {
        pointF.x * scale + dX
    }

    val y = pointF.y * scale + dY

    return Offset(x, y)
}


@Composable
fun CameraPreview(analyzer: ImageAnalysis.Analyzer) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView =
        remember { PreviewView(context).apply { scaleType = PreviewView.ScaleType.FIT_CENTER } }

    AndroidView(factory = { previewView }) { view ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = view.surfaceProvider
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context), analyzer)
                }


            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("CameraPreview", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}


@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun FaceContoursPreview() {
    FaceContours(
        faces = listOf(),
        analyzer = ImageAnalysis.Analyzer {},
        imageSize = Size(640f, 480f),
        isInPreview = true
    )
}
