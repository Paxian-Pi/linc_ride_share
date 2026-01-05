package com.ride_sharing.linc.utils

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}