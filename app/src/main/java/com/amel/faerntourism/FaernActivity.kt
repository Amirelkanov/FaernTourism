package com.amel.faerntourism


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.amel.faerntourism.service.createFcmNotificationChannel
import com.amel.faerntourism.ui.AuthViewModel
import com.amel.faerntourism.ui.PermissionsViewModel
import com.amel.faerntourism.ui.UpdateViewModel
import com.amel.faerntourism.ui.screens.detailed.ArticleScreen
import com.amel.faerntourism.ui.screens.detailed.PlaceScreen
import com.amel.faerntourism.ui.screens.general.AccountScreen
import com.amel.faerntourism.ui.screens.general.ArticlesScreen
import com.amel.faerntourism.ui.screens.general.HomeScreen
import com.amel.faerntourism.ui.screens.general.ToursScreen
import com.amel.faerntourism.ui.theme.FaernTourismTheme
import com.amel.faerntourism.worker.InterestingPlaceNotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FaernActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel by viewModels<AuthViewModel>()
        val updateViewModel by viewModels<UpdateViewModel>()

        val periodicRequest = PeriodicWorkRequestBuilder<InterestingPlaceNotificationWorker>(
            12, TimeUnit.HOURS,
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            DAILY_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        createFcmNotificationChannel(this)

        setContent {
            FaernTourismTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    val factory = rememberPermissionsControllerFactory()
                    val controller = remember(factory) { factory.createPermissionsController() }

                    BindEffect(controller)

                    val permissionsViewModel = viewModel { PermissionsViewModel(controller) }

                    permissionsViewModel.provideOrRequestRecordAllPermissions()

                    FaernNavHost(
                        navController,
                        authViewModel,
                        updateViewModel,
                        permissionsViewModel
                    )
                }
            }
        }
    }
}

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
