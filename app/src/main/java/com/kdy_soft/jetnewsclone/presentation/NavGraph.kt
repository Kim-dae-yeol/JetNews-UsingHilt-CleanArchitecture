package com.kdy_soft.jetnewsclone.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kdy_soft.jetnewsclone.presentation.screen.home.HomeRoute
import com.kdy_soft.jetnewsclone.presentation.screen.interests.InterestsRoute

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: ()->Unit
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = JetNewsNavigationRoute.HOME_ROUTE
    ) {
        composable(JetNewsNavigationRoute.HOME_ROUTE) {
            HomeRoute(openDrawer= openDrawer)
        }

        composable(JetNewsNavigationRoute.INTERESTS_ROUTE) {
            InterestsRoute(openDrawer= openDrawer)
        }
    }
}