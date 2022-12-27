package com.kdy_soft.jetnewsclone.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.kdy_soft.jetnewsclone.R
import com.kdy_soft.jetnewsclone.presentation.JetNewsNavigationRoute.HOME_ROUTE
import com.kdy_soft.jetnewsclone.presentation.JetNewsNavigationRoute.INTERESTS_ROUTE

enum class JetNewsMainNavigation(
    val route: String,
    @StringRes val labelId: Int,
    val icon: ImageVector
) {
    Home(HOME_ROUTE, R.string.home_title,Icons.Filled.Home),
    Interest(INTERESTS_ROUTE,R.string.interest_title ,Icons.Filled.ListAlt)
}

/**
 * Main-Destinations in app, currently showing on drawer
 * */
object JetNewsNavigationRoute {
    const val HOME_ROUTE = "home"
    const val INTERESTS_ROUTE = "interests"
}

/**
 * In app,responsible for navigation action model class
 * */
class JetNewsNavigationActions(navController: NavController) {
    private fun NavController.isLifecycleResumed() =
        currentBackStackEntry?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) == true

    val navToHome: () -> Unit = {
        if (navController.isLifecycleResumed()) {
            navController.navigate(JetNewsNavigationRoute.HOME_ROUTE) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val navToInterests: () -> Unit = {
        if (navController.isLifecycleResumed()) {
            navController.navigate(JetNewsNavigationRoute.INTERESTS_ROUTE) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }

                launchSingleTop = true
                restoreState = true
            }
        }
    }


}