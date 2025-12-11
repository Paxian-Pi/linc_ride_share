package com.ride_sharing.linc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

class TimerViewModel : ViewModel() {

    private val _timeLeft = MutableStateFlow(300)
    val timeLeft = _timeLeft.asStateFlow()

    fun startTimer() {
        viewModelScope.launch {
            for (i in 300 downTo 0) {
                _timeLeft.value = i
                delay(1000)
            }
        }
    }
}