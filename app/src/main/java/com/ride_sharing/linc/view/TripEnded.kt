package com.ride_sharing.linc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Icon

@Composable
@Preview(showBackground = true)
fun TripEnded(navController: NavController = rememberNavController()) {
    Scaffold(
        containerColor = Color(0xFF009688),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        Box(
                            modifier = Modifier
                                .padding(top = 40.dp, start = 20.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(60.dp)
                                )
                                .clickable {
                                    navController.navigate("home") {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                        ) {
                            Icon(
                                modifier = Modifier.padding(all = 5.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "User",
                                tint = Color.Black,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .background(
                                color = Color(0xFF393E42).copy(alpha = 0.1f),
                                shape = RoundedCornerShape(60.dp)
                            )
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(all = 5.dp)
                                .width(100.dp)
                                .height(100.dp),
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = Color.White.copy(alpha = 0.5f),
                        )
                    }

                    Text(
                        "Trip Complete! Thank You.",
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.W500, color = Color.White),
                        modifier = Modifier.padding(top = 30.dp)
                    )

                    Text(
                        "Another successful trip, well done!",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W500, color = Color.White),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            modifier = Modifier.padding(all = 5.dp),
//                            imageVector = Icons.Default.WbSunny,
//                            contentDescription = "Sunny",
//                            tint = Color(0xFFFF9800),
//                        )
//                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            "Carbon Emission Avoided: ~1.2 km private car equivalent",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column {
                        // Handler

                        Text(
                            "You helped 4 riders get to their destinations.",
                            style = TextStyle(fontSize = 12.sp),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        Text(
                            "Rate your passengers",
                            style = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.W500),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        //
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
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
                                        Text(
                                            "Thomas Morr",
                                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W600)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Icon(
                                            modifier = Modifier
                                                .width(18.dp)
                                                .height(18.dp),
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
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "User",
                                            tint = Color(0xFFFFC107),
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Box(modifier = Modifier.width(200.dp)) {
                                            Text(
                                                "Pick-up Point: Ikeja City Mall, Lagos",
                                                style = TextStyle(fontSize = 12.sp)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFF2196F3).copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                            ) {
                                Text(
                                    "Rate now", style = TextStyle(fontSize = 10.sp), modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        //
                        Text(
                            "Earnings for This Trip",
                            style = TextStyle(fontSize = 19.sp, fontWeight = FontWeight.W500),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        //
                        LazyColumn {
                            items(3) { item ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                ) {
                                    Text(
                                        if (item == 0) "Net Earnings" else if (item == 1) "Bonus" else "Linc Commission",
                                        style = TextStyle(fontSize = 16.sp),
                                        modifier = Modifier.padding(vertical = 15.dp)
                                    )
                                    Text(
                                        if (item == 0) "₦6,500.00" else if (item == 1) "₦500.00" else "₦500.00",
                                        style = TextStyle(fontSize = 16.sp),
                                        modifier = Modifier.padding(vertical = 15.dp)
                                    )
                                }
                            }

                        }

                        // Button
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Button(
                                onClick = { navController.navigate("trip_ended") },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
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