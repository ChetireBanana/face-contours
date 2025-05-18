package com.example.facecontours.data.local.camera

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class ImageAnalyzer (
    private val onFaceDetected: (faces:List<Face>, width: Int, height: Int) -> Unit,
) : ImageAnalysis.Analyzer {


    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("ImageAnalyzer", "Analyzing image")
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val width = if (rotationDegrees == 90 || rotationDegrees == 270) mediaImage.height else mediaImage.width
            val height = if (rotationDegrees == 90 || rotationDegrees == 270) mediaImage.width else mediaImage.height

            detector.process(image)
                .addOnSuccessListener { faces ->
                    Log.d("ImageAnalyzer", "Face detected")
                    onFaceDetected(faces, width, height)
                }
                .addOnFailureListener {
                    Log.d("ImageAnalyzer", "On Failure")
                   onFaceDetected(emptyList(), width, height)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

}