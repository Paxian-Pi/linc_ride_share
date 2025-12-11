package com.ride_sharing.linc.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Icon

@Composable
fun EmergencyWidget () {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
            .background(
                color = Color(0xFF2196F3),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                imageVector = Icons.Default.ShieldMoon,
                contentDescription = "User",
                tint = Color.White,
            )
            Text(
                text = "Emergency",
                modifier = Modifier.padding(
                    start = 5.dp,
                    end = 10.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
                style = TextStyle(color = Color.White, fontSize = 14.sp)
            )
        }
    }
}