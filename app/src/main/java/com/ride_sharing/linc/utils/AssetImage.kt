package com.ride_sharing.linc.utils

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AssetImage(
    modifier: Modifier = Modifier,
    iconName: String,
    width: Dp = 25.dp,
    height: Dp = 25.dp,
    contentDescription: String = "",
) {
    val context = LocalContext.current
    val bitmap = remember {
        context.assets.open(iconName).use { BitmapFactory.decodeStream(it) }
    }

    Image(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .width(width)
            .height(height),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription
    )
}