package com.ride_sharing.linc.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RideViewModel : ViewModel() {
    private val _eta = MutableStateFlow("Calculating...")
    private val _progressValue = MutableStateFlow(0.0f)

    val eta = _eta.asStateFlow()
    val progressValue = _progressValue.asStateFlow()

    fun setEta(value: String) {
        _eta.value = value
    }

    fun updateProgress(newProgress: Float) {
        _progressValue.value = newProgress
    }
}