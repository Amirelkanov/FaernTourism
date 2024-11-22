package com.example.faerntourism

import FavScreen
import PlaceScreen
import ToursScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.faerntourism.models.service.GoogleAuthUiService
import com.example.faerntourism.screens.FaernTourismAppPortrait
import com.example.faerntourism.ui.theme.FaernTourismTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiService(
            context = applicationContext,
            credentialManager = CredentialManager.create(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun NavGraphBuilder.routesGraph(navController: NavController) {
       // TODO: think about flow that will return result of sign in etc
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