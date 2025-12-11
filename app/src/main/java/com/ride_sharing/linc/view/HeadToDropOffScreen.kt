package com.ride_sharing.linc.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Icon
import com.ride_sharing.linc.components.GoogleMapView
import com.ride_sharing.linc.utils.EmergencyWidget
import com.ride_sharing.linc.utils.ProgressDemo
import com.ride_sharing.linc.viewmodel.RideViewModel
import kotlin.math.roundToInt

@Composable
@Preview(showBackground = true)
fun HeadToDropOffScreen(
    navController: NavController = rememberNavController(),
    rideViewModel: RideViewModel = RideViewModel()
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
                    HeadingToDropOffContent(
                        onMapTap = { showBox.value = !showBox.value },
                        rideViewModel = rideViewModel,
                        navController = navController
                    )
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
                                        "Heading to",
                                        style = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.W500)
                                    )
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            "Linc Ride",
                                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W600)
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("To drop-off", style = TextStyle(fontSize = 12.sp))
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Box(
                                                modifier = Modifier
                                                    .width(25.dp)
                                                    .height(25.dp)
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
                                }

                                Log.d("Progress", "${rideViewModel.progressValue.collectAsState().value}")

                                // Progress
                                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                    ProgressDemo(
                                        endGradientColor = Color(0xFF4CAF50),
                                        progress = rideViewModel.progressValue.collectAsState().value
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                //
                                Column {
                                    //
                                    HorizontalDivider(thickness = 0.5.dp)

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Column(
                                            modifier = Modifier
                                                .padding(all = 15.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text("Starting point", style = TextStyle(fontSize = 10.sp))
                                            Text(
                                                "Ikeja City Mall, Lagos",
                                                style = TextStyle(
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                )
                                            )
                                        }
                                    }

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Column(
                                            modifier = Modifier
                                                .padding(all = 15.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text("Destination", style = TextStyle(fontSize = 10.sp))
                                            Text(
                                                "1B, Birrel Avenue Yaba, Onike, Lagos",
                                                style = TextStyle(
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500
                                                )
                                            )
                                        }
                                    }
                                }

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
fun HeadingToDropOffContent(onMapTap: () -> Unit, rideViewModel: RideViewModel, navController: NavController) {
    GoogleMapView(
        onMapTap,
        navController = navController,
        isPickup = true,
        toPickup = false,
        rideViewModel = rideViewModel
    )
}