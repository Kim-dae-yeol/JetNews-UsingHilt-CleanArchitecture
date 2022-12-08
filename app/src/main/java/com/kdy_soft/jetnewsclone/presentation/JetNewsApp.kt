package com.kdy_soft.jetnewsclone.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetNewsApp(
    navController: NavHostController = rememberNavController(),
    widthSizeClass: WindowWidthSizeClass
) {
    //Todo : hoisting event, state...
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        currentBackStackEntry?.destination?.route ?: JetNewsNavigationRoute.HOME_ROUTE
    val actions = remember { JetNewsNavigationActions(navController) }

    //Todo : Width size 를 이용해서 큰 화면을 지원해보자.
    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navToHome = actions.navToHome,
                navToInterests = actions.navToInterests,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState
    ) {

        NavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
}