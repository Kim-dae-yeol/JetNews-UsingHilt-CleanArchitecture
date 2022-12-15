package com.kdy_soft.jetnewsclone.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kdy_soft.jetnewsclone.presentation.screen.home.HomeRoute
import com.kdy_soft.jetnewsclone.presentation.screen.home.HomeViewModel
import com.kdy_soft.jetnewsclone.presentation.screen.interests.InterestsRoute

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    isExpandedScreen: Boolean,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit,
    startDestination: String = JetNewsNavigationRoute.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        composable(JetNewsNavigationRoute.HOME_ROUTE) {
            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeRoute(
                openDrawer = openDrawer,
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen
            )
        }

        composable(JetNewsNavigationRoute.INTERESTS_ROUTE) {
            InterestsRoute(openDrawer = openDrawer)
        }
    }
}