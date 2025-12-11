package com.ride_sharing.linc.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Icon
import com.ride_sharing.linc.components.GoogleMapView
import com.ride_sharing.linc.utils.AssetImage
import com.ride_sharing.linc.utils.EmergencyWidget
import com.ride_sharing.linc.viewmodel.TimerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RiderIsArrivingScreen(
    navController: NavController = rememberNavController(),
    timerViewModel: TimerViewModel? = null
) {
    val showBox = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            val targetBoxHeight = if (showBox.value) 300.dp else 0.dp

            val animatedBoxHeight by animateDpAsState(
                targetValue = targetBoxHeight,
                animationSpec = tween(durationMillis = 350),
                label = "boxHeightAnim"
            )

            // Use BoxWithConstraints to get the available height for the map
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                // allow box to overlay map by 30.dp
                val mapHeight = (this.maxHeight - animatedBoxHeight) + 30.dp

                Box(
                    modifier = Modifier
                        .height(mapHeight)
                        .fillMaxWidth()
                ) {
                    RiderIsArrivingContent(onMapTap = { showBox.value = !showBox.value }, navController = navController)
                }

                val dragOffset = remember { mutableStateOf(0f) }
                val minPx = 0f
                val maxPx = 300f // or any reasonable max drag offset

                // Custom Bottom sheet
                AnimatedVisibility(
                    visible = showBox.value,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 350)
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 350)
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                val newValue = dragOffset.value + delta
                                dragOffset.value = newValue.coerceIn(minPx, maxPx)
                            },
                        )
                        .offset { IntOffset(0, dragOffset.value.roundToInt()) }
                ) {
                    Column {
                        EmergencyWidget()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                //                            .height(animatedBoxHeight + 40.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                                ),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column {
                                // Handler
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .height(5.dp)
                                        .width(70.dp)
                                        .background(
                                            color = Color.Gray.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .align(Alignment.CenterHorizontally)

                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                //
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(horizontal = 15.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        "Rider is arriving...",
                                        style = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.W500)
                                    )
                                    
                                    CountdownTimer(totalTime = 120)
                                }

                                //
                                Column {
                                    Row(
                                        modifier = Modifier
                                            //                                        .padding(horizontal = 20.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.padding(all = 15.dp)) {
                                            Text("To Pick-up", style = TextStyle(fontSize = 12.sp))
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Row {
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = Color(0xFF2196F3).copy(alpha = 0.1f),
                                                            shape = RoundedCornerShape(20.dp)
                                                        )
                                                ) {
                                                    Icon(
                                                        modifier = Modifier.padding(all = 5.dp),
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = "User",
                                                        tint = Color.Black,
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Column {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text("Thomas Morr", style = TextStyle(fontSize = 12.sp))
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                        Icon(
                                                            modifier = Modifier
                                                                .width(20.dp)
                                                                .height(20.dp),
                                                            imageVector = Icons.Default.Verified,
                                                            contentDescription = "User",
                                                            tint = Color(0xFF4CAF50),
                                                        )
                                                    }
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(
                                                            modifier = Modifier
                                                                .width(20.dp)
                                                                .height(20.dp),
                                                            imageVector = Icons.Default.Star,
                                                            contentDescription = "User",
                                                            tint = Color(0xFFFFC107),
                                                        )
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                        Text("4.7", style = TextStyle(fontSize = 12.sp))
                                                    }
                                                }
                                            }
                                        }

                                        Row(modifier = Modifier.padding(end = 15.dp)) {
                                            Icon(
                                                modifier = Modifier.padding(all = 5.dp),
                                                imageVector = Icons.AutoMirrored.Filled.Message,
                                                contentDescription = "User",
                                                tint = Color.Black,
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Icon(
                                                modifier = Modifier.padding(all = 5.dp),
                                                imageVector = Icons.Default.Call,
                                                contentDescription = "User",
                                                tint = Color.Black,
                                            )
                                        }
                                    }

                                    HorizontalDivider(thickness = 0.5.dp)

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Column(
                                            modifier = Modifier
                                                .padding(all = 15.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text("Pick-up point", style = TextStyle(fontSize = 10.sp))
                                            Text(
                                                "Ikeja City Mall, Lagos",
                                                style = TextStyle(
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                )
                                            )
                                        }
                                    }
                                }
                                //                            Spacer(modifier = Modifier.height(10.dp))

                                Swipeable(navController = navController)

                                //
                                Row {
                                    Row(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .padding(horizontal = 20.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Available", style = TextStyle(fontSize = 12.sp))
                                            Text("Seat", style = TextStyle(fontSize = 12.sp))
                                        }
                                        Text("2", style = TextStyle(fontSize = 12.sp))
                                    }

                                    //
                                    Row(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .padding(horizontal = 20.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Passenger", style = TextStyle(fontSize = 12.sp))
                                            Text("Accepted", style = TextStyle(fontSize = 12.sp))
                                        }
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color(0xFF2196F3).copy(alpha = 0.1f),
                                                    shape = RoundedCornerShape(20.dp)
                                                )
                                        ) {
                                            Icon(
                                                modifier = Modifier.padding(all = 5.dp),
                                                imageVector = Icons.Default.Person,
                                                contentDescription = "User",
                                                tint = Color.Black,
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                // Button
                                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    Button(
                                        onClick = { },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        contentPadding = PaddingValues(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                            .background(color = Color.Transparent, shape = RoundedCornerShape(60.dp))
                                            .border(
                                                width = 1.dp,
                                                color = Color.Black.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(60.dp)
                                            )
                                    ) {
                                        Text(
                                            "Share Ride Info",
                                            style = TextStyle(color = Color.Black),
                                            modifier = Modifier.padding(horizontal = 30.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiderIsArrivingContent(onMapTap: () -> Unit, navController: NavController) {
    GoogleMapView(onMapTap, navController = navController)
}

@Composable
fun CountdownTimer(
    totalTime: Int = 30,
    onFinished: () -> Unit = {}
) {
    var timeLeft by remember { mutableStateOf(totalTime) }
    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%02d:%02d", m, s)
    }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else {
            onFinished()
        }
    }

    Column(horizontalAlignment = Alignment.End) {
        Text(formatTime(timeLeft), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W600))
        Text(if(timeLeft == 0) "Waiting time elapsed" else "Waiting time", style = TextStyle(fontSize = 12.sp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Swipeable(navController: NavController) {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { mutableStateOf(0f) }
    val isSwiped = remember { mutableStateOf(false) }
    val minPx = 0f
    val maxPx = 500f // Define the range for the draggable offset

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newValue = dragOffset.value + delta
                    dragOffset.value = newValue.coerceIn(minPx, maxPx)
                },
                onDragStopped = {
                    if (dragOffset.value > maxPx / 2 && !isSwiped.value) {
                        isSwiped.value = true // Ensure navigation happens only once
                        scope.launch {
                            delay(500)
                            navController.navigate("head_to_drop_off")
                        }
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (dragOffset.value < maxPx / 2) {
            swipeableText(text = "Picked up", isSwiped = false)
        } else {
            swipeableText(text = "Routing to destination", isSwiped = true)
        }
    }
}

@Composable
fun swipeableText(text: String, isSwiped: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (!isSwiped) listOf(
                        Color(0xFFF44336),
                        Color(0xFF009688)
                    ) else listOf(
                        Color(0xFF2196F3).copy(alpha = 0.3f),
                        Color(0xFF2196F3).copy(alpha = 0.3f)
                    )
                )
            ),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = if (!isSwiped) Arrangement.SpaceBetween else Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!isSwiped)
                Row {
                    AssetImage(iconName = "chevron_left.png", width = 40.dp, height = 40.dp)
                    Text(
                        "Didn't show",
                        style = TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            Row {
                Text(
                    text,
                    style = TextStyle(color = if (!isSwiped) Color.White else Color.Black, fontSize = 16.sp),
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                if (!isSwiped)
                    AssetImage(iconName = "chevron_right.png", width = 40.dp, height = 40.dp)
            }
        }
    }
}
