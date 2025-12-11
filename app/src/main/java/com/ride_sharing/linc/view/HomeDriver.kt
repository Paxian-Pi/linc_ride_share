package com.ride_sharing.linc.view

//import androidx.compose.material3.ModalBottomSheetState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Icon
import com.ride_sharing.linc.components.GoogleMapView
import com.ride_sharing.linc.utils.AssetImage
import com.ride_sharing.linc.utils.CustomButton
import com.ride_sharing.linc.utils.EmergencyWidget
import com.ride_sharing.linc.viewmodel.ViewModelForSelectedTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// NavigationItem enum class
enum class NavigationItem(val label: String) {
    Home("Home"), History("History"), Profile("Profile");

    companion object {
        val items = entries
    }
}

@Composable
fun HomeDriverScreen(
    viewModel: ViewModelForSelectedTab = viewModel(),
    navController: NavController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val showBox = remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationItem.items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = { viewModel.selectTab(item) },
                        label = { Text(item.label) },
                        icon = { /* Optionally add icons */ })
                }
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (selectedTab) {
                // Home
                NavigationItem.Home -> {
                    // Animate the height of the box container
                    val targetBoxHeight = if (showBox.value) 200.dp else 0.dp
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
                            HomeContent(onMapTap = { showBox.value = !showBox.value }, navController = navController)
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
                                    onDragStopped = { velocity ->
                                        if (dragOffset.value > 100f) {
                                            // Instead of instantly dismissing, animate the offset out of view for smooth transition
                                            val dismissOffset = maxPx
                                            animateDismiss(
                                                dragOffset, dismissOffset,
                                                {
                                                    showBox.value = false
                                                    dragOffset.value = 0f
                                                },
                                                coroutineScope,
                                            )
                                        } else {
                                            // Spring back if not enough drag
                                            dragOffset.value = 0f
                                        }
                                    }
                                )
                                .offset { IntOffset(0, dragOffset.value.roundToInt()) }
                        ) {
                            Column {
                                EmergencyWidget()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(animatedBoxHeight + 40.dp)
                                        .background(
                                            color = Color(0xFF8EF58E),
                                            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                                        ),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Column() {
                                        Row {
                                            AssetImage(iconName = "campaign.png")
                                            Text(
                                                "1 Active campaign",
                                                fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(5.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(animatedBoxHeight)
                                                .background(
                                                    color = MaterialTheme.colorScheme.surface,
                                                    shape = RoundedCornerShape(
                                                        topStart = 30.dp,
                                                        topEnd = 30.dp
                                                    )
                                                ),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
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
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    "Choose your ride mode",
                                                    fontWeight = FontWeight.W500
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))

                                                Row(
                                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                                    modifier = Modifier
                                                        .padding(horizontal = 20.dp)
                                                        .fillMaxWidth()
                                                        .height(55.dp),
                                                ) {
                                                    var isDriver by remember { mutableStateOf(true) }

                                                    CustomButton(
                                                        mainText = "Join a Ride",
                                                        subText = "Book your seat",
                                                        containerColor = if (!isDriver) Color(0xFF4AA8F3) else Color.White,
                                                        iconName = "rider.png",
                                                        mainTextColor = if (!isDriver) Color.White else Color.Black,
                                                        subTextColor = if (!isDriver) Color.White else Color.Black
                                                    ) {
                                                        isDriver = !isDriver
                                                    }

                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    CustomButton(
                                                        mainText = "Offer Ride",
                                                        subText = "Share your ride",
                                                        containerColor = if (isDriver) Color(0xFF4AA8F3) else Color.White,
                                                        iconName = "driver.png",
                                                        mainTextColor = if (isDriver) Color.White else Color.Black,
                                                        subTextColor = if (isDriver) Color.White else Color.Black
                                                    ) {
                                                        isDriver = true

                                                        coroutineScope.launch {
                                                            delay(300)
                                                            showBox.value = false

                                                            coroutineScope.launch {
                                                                delay(500)
                                                                navController.navigate("get_to_pickup")
                                                            }
                                                        }
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(10.dp))

                                                var inputText by remember { mutableStateOf("") }
                                                OutlinedTextField(
                                                    value = inputText,
                                                    onValueChange = { inputText = it },
                                                    label = { Text("Where to?") },
                                                    leadingIcon = { AssetImage(iconName = "destination.png") },
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = Color.Transparent,
                                                        unfocusedBorderColor = Color.Transparent,
                                                        focusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                                                        unfocusedContainerColor = Color.Gray.copy(alpha = 0.1f),
                                                    ),
                                                    modifier = Modifier
                                                        .padding(horizontal = 20.dp)
                                                        .fillMaxWidth(),
                                                    shape = RoundedCornerShape(55.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // History
                NavigationItem.History -> {
                    HistoryContent()
                }

                // Profile
                NavigationItem.Profile -> {
                    ProfileContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(onMapTap: () -> Unit, navController: NavController) {
    GoogleMapView(onMapTap = onMapTap, navController = navController)
}

@Composable
fun HistoryContent() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text("History Screen")
    }
}

@Composable
fun ProfileContent() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text("Profile Screen")
    }
}

fun animateDismiss(
    dragOffset: MutableState<Float>,
    target: Float,
    onEnd: () -> Unit,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        val duration = 250
        val start = dragOffset.value
        val delta = target - start
        val steps = 20
        for (i in 1..steps) {
            dragOffset.value = start + delta * (i / steps.toFloat())
            delay((duration / steps).toLong())
        }
        onEnd()
    }
}
