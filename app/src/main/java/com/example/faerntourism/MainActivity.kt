package com.example.faerntourism


import FavScreen
import PlaceScreen
import ToursScreen
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.faerntourism.models.Place
import com.example.faerntourism.models.service.GoogleAuthUiService
import com.example.faerntourism.models.service.LocationService
import com.example.faerntourism.screens.HomeScreen
import com.example.faerntourism.ui.theme.FaernTourismTheme
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

// TODO: Очевиднейшая заглушка
@Composable
fun places() = listOf(
    Place(
        1,
        painterResource(R.drawable.fatima_holding_sun_monument),
        "Фатима, держащая Солнце",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(43.075616, 44.656784)
    ),
    Place(
        2,
        painterResource(R.drawable.philharmonic),
        "Лютеранская Кирха",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(43.036756, 44.677827)
    ),
    Place(
        3,
        painterResource(R.drawable.kurtarin_cave),
        "Куртатинское ущелье",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(42.858264, 44.305158)
    ),
    Place(
        4,
        painterResource(R.drawable.sogdiana_cafe),
        "Кафе Согдиана",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.667506, 42.984168)
    ),
    Place(
        5,
        painterResource(R.drawable.saint_bogoroditsa_church_1),
        "Церковь Рождества Богородицы",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.686002, 43.019957)
    ),
    Place(
        6,
        painterResource(R.drawable.vachtangovs_house_museum),
        "Дом Евгения Вахтангова",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.681546, 43.021901)
    ),
    Place(
        7,
        painterResource(R.drawable.saint_bogoroditsa_church_1),
        "Церковь Рождества Богородицы",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
        LatLng(44.686002, 43.019957)
    )
)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDailyNotificationChannel()
            scheduleDailyNotification()
        }

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


    // TODO : как сделаешь лабу по воркерам, подумай, куда можно перетащить
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDailyNotificationChannel() {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            DAILY_NOTIFICATION_CHANNEL_ID,
            "Daily Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleDailyNotification() {
        val workRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
            12, TimeUnit.HOURS,
            30, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyNotification",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }


    private fun NavGraphBuilder.routesGraph(navController: NavController) {
        composable(HOME_SCREEN) {
            HomeScreen(
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