package com.example.faerntourism


import FavScreen
import android.Manifest
import PlaceScreen
import ToursScreen
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.faerntourism.models.service.GoogleAuthUiService
import com.example.faerntourism.models.service.LocationService
import com.example.faerntourism.screens.FaernTourismAppPortrait
import com.example.faerntourism.ui.theme.FaernTourismTheme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiService(
            context = applicationContext,
            credentialManager = CredentialManager.create(applicationContext)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                "daily_notification_channel",
                "Daily Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(channel)

        scheduleDailyNotification()

        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }

        setContent {
            FaernTourismTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = HOME_SCREEN) {
                        routesGraph(navController)
                    }
                }
            }
        }
    }

    private fun scheduleDailyNotification() {
        val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
            12, TimeUnit.HOURS,
            1, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyNotification",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }


    private fun NavGraphBuilder.routesGraph(navController: NavController) {
        composable(HOME_SCREEN) {
            FaernTourismAppPortrait(
                userData = googleAuthUiClient.getSignedInUser(),
                onSignInClick = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signIn()
                        navController.navigate(HOME_SCREEN)
                    }
                },
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        navController.navigate(HOME_SCREEN)
                    }
                },
                openScreen = { route -> navController.navigate(route) }
            )
        }
        composable(FAV_SCREEN) {
            FavScreen(openScreen = { route -> navController.navigate(route) })
        }
        composable(TOURS_SCREEN) {
            ToursScreen(openScreen = { route -> navController.navigate(route) })
        }
        composable(
            route = "$PLACE_SCREEN$PLACE_ID_ARG",
            arguments = listOf(
                navArgument(name = PLACE_ID) {
                    defaultValue = PLACE_DEFAULT_ID
                }
            )
        ) { backstackEntry ->
            PlaceScreen(
                placeId = backstackEntry.arguments?.getString(PLACE_ID)?.toInt(),
            )
        }
    }
}