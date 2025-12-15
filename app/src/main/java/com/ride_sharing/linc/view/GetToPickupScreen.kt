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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.ride_sharing.linc.utils.AssetImage
import com.ride_sharing.linc.utils.EmergencyWidget
import com.ride_sharing.linc.utils.ProgressDemo
import com.ride_sharing.linc.viewmodel.RideViewModel
import kotlin.math.roundToInt

@Composable
@Preview(showBackground = true)
fun GetToPickupScreen(
    navController: NavController = rememberNavController(),
    rideViewModel: RideViewModel = RideViewModel()
) {
    val showBox = remember { mutableStateOf(true) }
//    val progress = rideViewModel.progressValue.collectAsState(initial = 0f) // Use ViewModel's progress state

    // Observe the driver's location updates from the RideViewModel
//    val driverLocation = rideViewModel.driverLocation.collectAsState(initial = LatLng(0.0, 0.0)).value
//    val totalDistance = rideViewModel.totalDistanceToPickup.collectAsState().value ?: 1f // Default to 1 to avoid division by zero
//    val pickupLocation = LatLng(6.6144, 3.3581) // Pickup location
//
//    // Calculate progress based on the distance between the driver and the pickup location
//    LaunchedEffect(driverLocation, totalDistance) {
//        val remainingDistance = FloatArray(1)
//        android.location.Location.distanceBetween(
//            driverLocation.latitude, driverLocation.longitude,
//            pickupLocation.latitude, pickupLocation.longitude,
//            remainingDistance
//        )
//
//        // Update progress proportionally in the ViewModel
//        // val newProgress = 1f - (remainingDistance[0] / totalDistance).coerceIn(0f, 1f)
//        // rideViewModel.updateProgress(newProgress)
//    }

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
                    GetToPickupContent(
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
                                        .padding(horizontal = 20.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        "Get to pickup...",
                                        fontWeight = FontWeight.W500
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = Color(0xFF4AA8F3).copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(30.dp)
                                            )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(
                                                end = 15.dp,
                                                top = 8.dp,
                                                bottom = 8.dp
                                            )
                                        ) {
                                            AssetImage(iconName = "time.png")
                                            Text(if (rideViewModel.eta.collectAsState().value == "Calculating...") "--" else "${rideViewModel.eta.collectAsState().value} away")
                                        }
                                    }
                                }

                                Log.d("Progress", "${rideViewModel.progressValue.collectAsState().value}")

                                // Progress
                                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                    ProgressDemo(progress = rideViewModel.progressValue.collectAsState().value)
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                // Divider
                                HorizontalDivider(thickness = 0.5.dp)
                                Spacer(modifier = Modifier.height(10.dp))

                                //
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp)
                                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                                        .border(
                                            width = 5.dp,
                                            color = Color(0xFF2196F3).copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier
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

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color = Color.Green.copy(alpha = 0.1f),
                                                    shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                                                ),
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(all = 15.dp)
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column {
                                                    Text("Pick-up point", style = TextStyle(fontSize = 10.sp))
                                                    Text(
                                                        "1, Wemco Junction",
                                                        style = TextStyle(
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.W500
                                                        )
                                                    )
                                                }

                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = Color(0xFFA2F8F0),
                                                            shape = RoundedCornerShape(8.dp)
                                                        )
                                                ) {
                                                    Log.d("ETA", rideViewModel.eta.collectAsState().value)

                                                    Text(
                                                        "ETA  \u2022  ${rideViewModel.eta.collectAsState().value}",
                                                        style = TextStyle(fontSize = 12.sp),
                                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))

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

                                //
                                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    Button(
                                        onClick = { },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        contentPadding = PaddingValues(8.dp),
                                        modifier = Modifier
                                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                            .background(color = Color.Black, shape = RoundedCornerShape(60.dp))
                                    ) {
                                        Text(
                                            "Share Ride Info",
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
fun GetToPickupContent(onMapTap: () -> Unit, rideViewModel: RideViewModel, navController: NavController) {

    GoogleMapView(
        onMapTap = onMapTap,
        toPickup = true,
        rideViewModel = rideViewModel,
        navController = navController
    )
}
