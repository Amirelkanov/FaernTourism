package com.amel.faerntourism


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amel.faerntourism.ui.AuthViewModel
import com.amel.faerntourism.ui.screens.detailed.ArticleScreen
import com.amel.faerntourism.ui.screens.detailed.PlaceScreen
import com.amel.faerntourism.ui.screens.general.AccountScreen
import com.amel.faerntourism.ui.screens.general.ArticlesScreen
import com.amel.faerntourism.ui.screens.general.HomeScreen
import com.amel.faerntourism.ui.screens.general.ToursScreen
import com.amel.faerntourism.ui.theme.FaernTourismTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaernActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FaernTourismTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
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
            HomeScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onPlaceClick = { placeId ->
                    navController.navigateToSinglePlace(placeId)
                },
            )
        }
        composable(route = Tours.route) {
            ToursScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
            )
        }
        composable(route = Articles.route) {
            ArticlesScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onArticleClick = { articleId ->
                    navController.navigateToSingleArticle(articleId)
                }
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
        /* composable(route = Favourite.route) { // TODO
             FavScreen()
         }*/

        composable(
            route = SinglePlace.routeWithArgs,
            arguments = SinglePlace.arguments
        ) { navBackStackEntry ->
            val placeId = navBackStackEntry.arguments?.getString(SinglePlace.PLACE_ID_ARG)
            if (placeId != null) {
                PlaceScreen(
                    placeId = placeId,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = SingleArticle.routeWithArgs,
            arguments = SingleArticle.arguments
        ) { navBackStackEntry ->
            val articleId = navBackStackEntry.arguments?.getString(SingleArticle.ARTICLE_ID_ARG)
            if (articleId != null) {
                ArticleScreen(
                    articleId = articleId,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToSinglePlace(placeId: String) =
    this.navigateSingleTopTo("${SinglePlace.route}/$placeId")

private fun NavHostController.navigateToSingleArticle(articleId: String) =
    this.navigateSingleTopTo("${SingleArticle.route}/$articleId")
