package com.ride_sharing.linc

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ride_sharing.linc.navigation.MainNavigationHost
import com.ride_sharing.linc.ui.theme.LincRideshareTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNavigationToGetToPickupScreen() {
        composeTestRule.setContent {
            LincRideshareTheme {
                val navController = rememberNavController()
                MainNavigationHost(navController = navController)
            }
        }

        // Simulate clicking the "Offer Ride" button
        composeTestRule.onNodeWithText("Offer Ride").performClick()

        // Verify navigation to "GetToPickup" screen
        composeTestRule.onNodeWithText("GetToPickup").assertExists()
    }

    @Test
    fun testNavigationToRiderIsArrivingScreen() {
        composeTestRule.setContent {
            LincRideshareTheme {
                val navController = rememberNavController()
                MainNavigationHost(navController = navController)
            }
        }

        // Simulate clicking the "Share Ride Info" button
        composeTestRule.onNodeWithText("Share Ride Info").performClick()

        // Verify navigation to "RiderIsArriving" screen
        composeTestRule.onNodeWithText("RiderIsArriving").assertExists()
    }

    @Test
    fun testApiKeyIsInjectedInBuildConfig() {
        // Assert that the API key is present and not empty
        assert(BuildConfig.MAPS_API_KEY.isNotEmpty()) {
            "MAPS_API_KEY should not be empty. Check your build.gradle and local.properties setup."
        }
    }

    // If fetchRoute is refactored to be injectable/testable, you could do:
    //@Test
    //fun testApiKeyIsUsedInFetchRoute() {
    //    val testOrigin = LatLng(0.0, 0.0)
    //    val testDestination = LatLng(1.0, 1.0)
    //    val url = buildDirectionsUrl(testOrigin, testDestination, BuildConfig.MAPS_API_KEY)
    //    assert(url.contains(BuildConfig.MAPS_API_KEY))
    //}
}
