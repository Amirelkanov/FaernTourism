package com.amel.faerntourism

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amel.faerntourism.ui.AuthViewModel
import com.amel.faerntourism.ui.PermissionsViewModel
import com.amel.faerntourism.ui.UpdateViewModel
import com.amel.faerntourism.ui.screens.detailed.ArticleScreen
import com.amel.faerntourism.ui.screens.detailed.PlaceScreen
import com.amel.faerntourism.ui.screens.general.AccountScreen
import com.amel.faerntourism.ui.screens.general.ArticlesScreen
import com.amel.faerntourism.ui.screens.general.HomeScreen
import com.amel.faerntourism.ui.screens.general.ToursScreen


@Composable
fun FaernNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    updateViewModel: UpdateViewModel,
    permissionsViewModel: PermissionsViewModel
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
                updateViewModel = updateViewModel,
                permissionsViewModel = permissionsViewModel
            )
        }
        composable(route = Tours.route) {
            ToursScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                updateViewModel = updateViewModel,
            )
        }
        composable(route = Articles.route) {
            ArticlesScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                onArticleClick = { articleId ->
                    navController.navigateToSingleArticle(articleId)
                },
                updateViewModel = updateViewModel,
            )
        }
        composable(route = Account.route) {
            AccountScreen(
                onBottomTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                authViewModel = authViewModel,
                updateViewModel = updateViewModel,
            )
        }

        composable(
            route = SinglePlace.routeWithArgs,
            arguments = SinglePlace.arguments,
            deepLinks = SinglePlace.deepLinks
        ) { navBackStackEntry ->
            val placeId = navBackStackEntry.arguments?.getString(SinglePlace.PLACE_ID_ARG)
            if (placeId != null) {
                PlaceScreen(
                    placeId = placeId,
                    navigateBack = { navController.popBackStack() },
                    permissionsViewModel = permissionsViewModel
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
