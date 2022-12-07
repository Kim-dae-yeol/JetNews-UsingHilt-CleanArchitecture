package com.kdy_soft.jetnewsclone.presentation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination


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