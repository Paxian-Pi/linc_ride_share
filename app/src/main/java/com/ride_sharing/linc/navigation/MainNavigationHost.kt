package com.ride_sharing.linc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ride_sharing.linc.view.GetToPickupScreen
import com.ride_sharing.linc.view.HeadToDropOffScreen
import com.ride_sharing.linc.view.HomeDriverScreen
import com.ride_sharing.linc.view.RiderIsArrivingScreen
import com.ride_sharing.linc.view.TripEnded
import com.ride_sharing.linc.viewmodel.TimerViewModel

@Composable
fun MainNavigationHost(navController: NavHostController, timerViewModel: TimerViewModel = TimerViewModel()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeDriverScreen(navController = navController) }
        composable("get_to_pickup") { GetToPickupScreen(navController = navController) }
        composable("rider_is_arriving") { RiderIsArrivingScreen(navController = navController, timerViewModel = timerViewModel) }
        composable("head_to_drop_off") { HeadToDropOffScreen(navController = navController) }
        composable("trip_ended") { TripEnded(navController = navController) }
    }
}