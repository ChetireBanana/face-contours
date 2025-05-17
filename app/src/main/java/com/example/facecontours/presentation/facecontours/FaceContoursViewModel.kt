package com.example.facecontours.presentation.facecontours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facecontours.data.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceContoursViewModel @Inject constructor(
    private val dataStore: DataStore
): ViewModel() {

    val isFirstLaunch = dataStore.isFirstLaunchFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun setOnboardingShown() {
        viewModelScope.launch {
            dataStore.setFirstLaunchDone()
        }
    }

}