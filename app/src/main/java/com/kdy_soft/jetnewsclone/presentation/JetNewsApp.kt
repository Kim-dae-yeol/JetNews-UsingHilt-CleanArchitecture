package com.kdy_soft.jetnewsclone.presentation

import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetNewsApp(
    navController: NavHostController = rememberNavController(),
    widthSizeClass: WindowWidthSizeClass
) {
    //Todo : hoisting event, state...
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    //Todo : Width size 를 이용해서 큰 화면을 지원해보자.
    ModalNavigationDrawer(
        drawerContent = {/*TODO*/ },
        drawerState = drawerState
    ) {
        NavGraph(navController = navController)
    }
}