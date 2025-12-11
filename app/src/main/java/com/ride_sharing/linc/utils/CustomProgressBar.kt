package com.ride_sharing.linc.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ride_sharing.linc.R
import kotlin.math.roundToInt

@Composable
fun CarProgressBar(
    progress: Float,              // 0f → 1f
    modifier: Modifier = Modifier,
    endGradientColor: Color,
    carImage: Painter             // painterResource(R.drawable.car_top)
) {
    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val carOffset = progress * (screenWidthPx - with(density) { 24.dp.toPx() })

    Box(
        modifier = modifier
            .height(40.dp)        // adjust as needed
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {

        // --- Background Track ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.3f))
        )

        // --- Gradient Progress Fill ---
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFBFD7FF),
                            endGradientColor
                        )
                    )
                )
        )

        // --- Moving Car Image ---
//        Image(
//            painter = carImage,
//            contentDescription = null,
//            modifier = Modifier
//                .offset { IntOffset(carOffset.roundToInt(), 0) }
//                .size(70.dp)
//        )

        // --- End Dot (Knob) ---
        Box(
            modifier = Modifier
                .offset { IntOffset(carOffset.roundToInt(), 0) }
                .size(24.dp)
                .clip(CircleShape)
                .background(endGradientColor)
                .border(4.dp, Color.White, CircleShape)
        )
    }
}

@Composable
fun ProgressDemo(endGradientColor: Color = Color(0xFF2F6BFF), progress: Float) {
//    val progress = remember { mutableStateOf(0.2f) }
    val animatedProgress by animateFloatAsState(
        targetValue = android.R.attr.progress.toFloat(),
        animationSpec = tween(800)
    )

    CarProgressBar(
        progress = progress,
        endGradientColor = endGradientColor,   
        carImage = painterResource(id = R.drawable.ic_launcher_foreground)
    )
}
