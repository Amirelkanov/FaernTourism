package com.amel.faerntourism

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

/**
 * Contact for information needed on every navigation destination
 * */
interface FaernDestination {
    val activeIcon: ImageVector
    val inactiveIcon: ImageVector

    @get:StringRes
    val titleResource: Int
    val topAppBarTitle: String
    val route: String
}

interface DetailedFaernDestination {
    val arguments: List<NamedNavArgument>
    val route: String
}

/**
 * Faern app navigation destinations
 */

object Home : FaernDestination {
    override val activeIcon: ImageVector = Icons.Filled.Home
    override val inactiveIcon: ImageVector = Icons.Outlined.Home
    override val titleResource: Int = R.string.home
    override val topAppBarTitle: String = "Что посетить?"
    override val route: String = "home"
}

object Tours : FaernDestination {
    override val activeIcon: ImageVector = Icons.Filled.Landscape
    override val inactiveIcon: ImageVector = Icons.Outlined.Landscape
    override val titleResource: Int = R.string.tours
    override val topAppBarTitle: String = "Туры"
    override val route: String = "tours"
}

object Articles : FaernDestination {
    override val activeIcon: ImageVector = Icons.AutoMirrored.Filled.LibraryBooks
    override val inactiveIcon: ImageVector = Icons.AutoMirrored.Outlined.LibraryBooks
    override val titleResource: Int = R.string.articles
    override val topAppBarTitle: String = "Статьи"
    override val route: String = "articles"
}

object Account : FaernDestination {
    override val activeIcon: ImageVector = Icons.Filled.AccountCircle
    override val inactiveIcon: ImageVector = Icons.Outlined.AccountCircle
    override val titleResource: Int = R.string.account
    override val topAppBarTitle: String = "Об аккаунте"
    override val route: String = "account"
}

object SinglePlace : DetailedFaernDestination {
    const val PLACE_ID_ARG = "place_id"
    override val route: String = "single_place"
    override val arguments: List<NamedNavArgument> =
        listOf(navArgument(PLACE_ID_ARG) { type = NavType.StringType })
    val routeWithArgs = "$route/{$PLACE_ID_ARG}"
    val deepLinks = listOf(navDeepLink {
        uriPattern = "faern://${route}/{${PLACE_ID_ARG}}"
    })
}

object SingleArticle : DetailedFaernDestination {
    const val ARTICLE_ID_ARG = "article_id"
    override val route: String = "single_article"
    override val arguments: List<NamedNavArgument> =
        listOf(navArgument(ARTICLE_ID_ARG) { type = NavType.StringType })
    val routeWithArgs = "$route/{$ARTICLE_ID_ARG}"
}

// Screens to be displayed in the navigation bottom bar
val faernBottomNavigationBarScreens = listOf(Home, Tours, Articles, Account)