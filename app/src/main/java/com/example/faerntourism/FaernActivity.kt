package com.example.faerntourism


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faerntourism.ui.screens.general.ArticlesViewModel
import com.example.faerntourism.ui.screens.detailed.ArticleScreen
import com.example.faerntourism.ui.screens.detailed.PlaceScreen
import com.example.faerntourism.ui.screens.general.AccountScreen
import com.example.faerntourism.ui.screens.general.CultureScreen
import com.example.faerntourism.ui.screens.general.HomeScreen
import com.example.faerntourism.ui.screens.general.ToursScreen
import com.example.faerntourism.ui.AuthViewModel
import com.example.faerntourism.ui.screens.general.PlacesViewModel
import com.example.faerntourism.ui.theme.FaernTourismTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaernActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FaernTourismTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()

                    FaernNavHost(navController, authViewModel)
                }
            }
        }
    }

}

@Composable
fun FaernNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(navController = navController, startDestination = Home.route) {
        composable(route = Home.route) {
            val placesViewModel: PlacesViewModel = hiltViewModel()
            HomeScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onPlaceClick = { placeId ->
                    navController.navigateToSinglePlace(placeId)
                },
                placesViewState = placesViewModel.placesViewStateFlow.collectAsState().value,
                retryAction = placesViewModel::getPlacesData
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
            val articlesViewModel: ArticlesViewModel = hiltViewModel()
            CultureScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onArticleClick = { articleId ->
                    navController.navigateToSingleArticle(articleId)
                },
                articlesViewState = articlesViewModel.articlesViewStateFlow.collectAsState().value,
                retryAction = articlesViewModel::getArticlesData
            )
        }
        composable(route = Account.route) {
            AccountScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                viewModel = authViewModel
            )
        }
        /* composable(route = Account.route) { // TODO
             FavScreen()
         }*/

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
