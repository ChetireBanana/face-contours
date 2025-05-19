@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.facecontours.data.local.camera

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPermissionHandler(
    onPermissionGranted: @Composable () -> Unit,
    onPermissionDenied: () -> Unit
) {

    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    var showRationale by remember { mutableStateOf(false) }

    if (permissionState.status.isGranted) {
        onPermissionGranted()
    } else {
        if (permissionState.status.shouldShowRationale && !showRationale) {
            showRationale = true
        } else if (!permissionState.status.shouldShowRationale && !showRationale) {
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
        }
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text("Camera permission required") },
            text = { Text(text = "This app needs camera permission to work properly") },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    permissionState.launchPermissionRequest()
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationale = false
                    onPermissionDenied()
                }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

}