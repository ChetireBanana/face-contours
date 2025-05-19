package com.example.facecontours.presentation.facecontours


import androidx.compose.ui.geometry.Size
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facecontours.data.local.DataStoreInterface
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceContoursViewModel @Inject constructor(
    private val dataStore: DataStoreInterface
) : ViewModel() {

    val isFirstLaunch = dataStore.isFirstLaunchFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun setOnboardingShown() {
        viewModelScope.launch {
            dataStore.setFirstLaunchDone()
        }
    }

    private val _faces = mutableStateOf<List<Face>>(emptyList())
    val faces: State<List<Face>> get() = _faces

    val isFacesDetected: Boolean
        get() = _faces.value.isNotEmpty()

    fun updateFaces(newFaces: List<Face>) {
        _faces.value = newFaces
    }

    private val _imageSize = mutableStateOf(Size(0f, 0f))
    val imageSize: State<Size> get() = _imageSize

    fun updateImageSize(width: Int, height: Int) {
        _imageSize.value = Size(width.toFloat(), height.toFloat())
    }

}