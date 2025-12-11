package com.ride_sharing.linc.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RideViewModel : ViewModel() {
    private val _eta = MutableStateFlow<String?>("Calculating...")
    private val _progressValue = MutableStateFlow(0.0f)
    
    private val _progressValue2 = MutableStateFlow(0.0f)
    private val _driverLocation = MutableStateFlow(LatLng(0.0, 0.0))
    private val _totalDistanceToPickup = MutableStateFlow<Float?>(null)

    val eta = _eta.asStateFlow()
    val progressValue = _progressValue.asStateFlow()
    val progressValue2 = _progressValue2.asStateFlow()
    val driverLocation = _driverLocation.asStateFlow()
    val totalDistanceToPickup = _totalDistanceToPickup.asStateFlow()

    fun setEta(value: String) {
        _eta.value = value
    }

    fun updateProgress2(value: Float) {
        _progressValue2.value = value
    }

    fun setDriverLocation(location: LatLng) {
        _driverLocation.value = location
    }

    fun setTotalDistanceToPickup(distance: Float) {
        _totalDistanceToPickup.value = distance
    }

    fun updateProgress(newProgress: Float) {
        _progressValue.value = newProgress
    }
}