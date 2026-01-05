package com.ride_sharing.linc.components

import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material3.Icon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.ride_sharing.linc.BuildConfig
import com.ride_sharing.linc.viewmodel.RideViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.core.graphics.scale

@Composable
//fun GoogleMapView(onMapTap: () -> Unit, toPickup: Boolean = false, onEtaUpdate: (String) -> Unit) {
fun GoogleMapView(
    onMapTap: () -> Unit,
    toPickup: Boolean = false,
    isPickup: Boolean = false,
    rideViewModel: RideViewModel? = null,
    navController: NavController // Added navController parameter
) {
    val driverLocation = remember { mutableStateOf(LatLng(6.630833, 3.354242)) } // Wemco Junction
    val pickupLocation = LatLng(6.6144, 3.3581) // Ikeja City mall
    val destinationLocation = LatLng(6.5105, 3.3771) // Destination coordinates

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(driverLocation.value, 17f)
    }

    val context = LocalContext.current
    val bitmap = remember {
        val originalBitmap =
            if (toPickup || isPickup) context.assets.open("routing_car.png").use { BitmapFactory.decodeStream(it) }
            else context.assets.open("driver_marker.png").use { BitmapFactory.decodeStream(it) }

        originalBitmap.scale((originalBitmap.width * 0.7).toInt(), (originalBitmap.height * 0.7).toInt())

//        createScaledBitmap(
//            originalBitmap,
//            (originalBitmap.width * 0.7).toInt(),
//            (originalBitmap.height * 0.7).toInt(),
//            true
//        )
    }

    val markerState = remember { MarkerState(driverLocation.value) }
    val coroutineScope = rememberCoroutineScope()
    val routePoints = remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // Modify the Polyline logic to remove the route after the marker as the driver moves past a location
    LaunchedEffect(toPickup, isPickup) {
        if (toPickup || isPickup) {
            coroutineScope.launch {
                val route = if (toPickup) fetchRoute(driverLocation.value, pickupLocation) else fetchRoute(
                    pickupLocation,
                    destinationLocation
                )
                routePoints.value = route

                val totalDistance = calculateTotalDistance(route)
                var distanceCovered = 0.0f

                val steps = route.size
                val delayPerStep = 1000L // 1 second per step

                for (i in 1 until steps) {
                    delay(delayPerStep)
                    val currentLocation = route[i]
                    val previousLocation = route[i - 1]

                    // Update marker position
                    markerState.position = currentLocation

                    // Calculate distance covered
                    distanceCovered += calculateDistance(previousLocation, currentLocation)

                    // Update progress and ETA
                    val progress = distanceCovered / totalDistance
                    rideViewModel?.updateProgress(progress)
                    val eta = calculateEta(totalDistance - distanceCovered)
                    rideViewModel?.setEta(eta)

                    // Remove the completed portion of the route
                    routePoints.value = routePoints.value.drop(1)

                    // Move the camera to focus on the driver's marker
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(markerState.position))
                }

                // Notify that the routing simulation is completed
                if (toPickup) {
                    navController.navigate("rider_is_arriving")
                } else {
                    navController.navigate("trip_ended")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            onMapClick = { onMapTap() }
        ) {
            Marker(
                state = markerState,
                title = "Driver",
                icon = BitmapDescriptorFactory.fromBitmap(bitmap)
            )

            // Draw polyline for the route
            if (routePoints.value.isNotEmpty()) {
                Polyline(
                    points = routePoints.value,
                    color = Color(0xFF6B530E),
                    width = 7f
                )
            }
        }

        if (toPickup || isPickup)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp), horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(20.dp)
                        ),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.padding(start = 10.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = "User",
                            tint = Color.White,
                        )
                        Text(
                            text = "Stop new requests",
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
    }
}

suspend fun fetchRoute(origin: LatLng, destination: LatLng): List<LatLng> {
    val apiKey = BuildConfig.MAPS_API_KEY
    val url =
        "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&key=$apiKey"

    Log.d("API_KEY_CHECK", "API KEY: ${BuildConfig.MAPS_API_KEY}")

    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        if (responseBody != null) {
            val json = JSONObject(responseBody)
            val routes = json.getJSONArray("routes")
            if (routes.length() > 0) {
                val overviewPolyline = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")
                return@withContext PolyUtil.decode(overviewPolyline)
            }
        }
        emptyList()
    }
}

// Helper function to calculate total distance of the route
private fun calculateTotalDistance(route: List<LatLng>): Float {
    var totalDistance = 0.0f
    for (i in 1 until route.size) {
        totalDistance += calculateDistance(route[i - 1], route[i])
    }
    return totalDistance
}

// Helper function to calculate distance between two LatLng points
private fun calculateDistance(start: LatLng, end: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(
        start.latitude, start.longitude,
        end.latitude, end.longitude,
        results
    )
    return results[0]
}

// Helper function to calculate ETA based on remaining distance
private fun calculateEta(remainingDistance: Float): String {
    val averageSpeed = 40f // Average speed in km/h
    val timeInHours = remainingDistance / 1000 / averageSpeed
    val timeInMinutes = (timeInHours * 60).toInt()
    return "$timeInMinutes min"
}
