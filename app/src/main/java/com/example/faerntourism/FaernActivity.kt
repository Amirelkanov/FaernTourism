package com.example.faerntourism


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faerntourism.network.GoogleAuthUiService
import com.example.faerntourism.screens.detailed.ArticleScreen
import com.example.faerntourism.screens.detailed.PlaceScreen
import com.example.faerntourism.screens.general.AccountScreen
import com.example.faerntourism.screens.general.CultureScreen
import com.example.faerntourism.screens.general.FavScreen
import com.example.faerntourism.screens.general.HomeScreen
import com.example.faerntourism.screens.general.ToursScreen
import com.example.faerntourism.ui.theme.FaernTourismTheme


class FaernActivity : ComponentActivity() {

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

                    FaernNavHost(navController)
                }
            }
        }
    }

}

@Composable
fun FaernNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Home.route) {
        composable(route = Home.route) {
            HomeScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onPlaceClick = { placeId ->
                    navController.navigateToSinglePlace(placeId)
                }
            )
        }
        composable(route = Tours.route) {
            ToursScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
            )
        }
        composable(route = Culture.route) {
            CultureScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onArticleClick = { articleId ->
                    navController.navigateToSingleArticle(articleId)
                }
            )
        }
        composable(route = Account.route) { // TODO
            AccountScreen()
        }
        composable(route = Account.route) { // TODO
            FavScreen()
        }

        composable(
            route = SinglePlace.routeWithArgs,
            arguments = SinglePlace.arguments
        ) { navBackStackEntry ->
            val placeId = navBackStackEntry.arguments?.getString(SinglePlace.PLACE_ID_ARG)
            PlaceScreen(
                placeId = placeId,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = SingleArticle.routeWithArgs,
            arguments = SingleArticle.arguments
        ) { navBackStackEntry ->
            val articleId = navBackStackEntry.arguments?.getString(SingleArticle.ARTICLE_ID_ARG)
            ArticleScreen(
                articleId = articleId, // TODO: очев заглукша
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToSinglePlace(placeId: String) =
    this.navigateSingleTopTo("${SinglePlace.route}/$placeId")

private fun NavHostController.navigateToSingleArticle(articleId: String) =
    this.navigateSingleTopTo("${SingleArticle.route}/$articleId")
