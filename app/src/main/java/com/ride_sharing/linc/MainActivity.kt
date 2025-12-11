package com.ride_sharing.linc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ride_sharing.linc.ui.theme.LincRideshareTheme
import com.ride_sharing.linc.navigation.MainNavigationHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LincRideshareTheme {
                val navController = rememberNavController()
                MainNavigationHost(navController = navController)
            }
        }
    }
}
