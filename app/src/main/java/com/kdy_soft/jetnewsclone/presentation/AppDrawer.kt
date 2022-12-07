package com.kdy_soft.jetnewsclone.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(
    currentRoute: String,
    navToHome: () -> Unit,
    navToInterests: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.width(120.dp)) {
        IconButton(onClick = closeDrawer) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "close drawer")
        }

        TextButton(onClick = navToHome) {
            val isCurrentRoute = currentRoute == JetNewsNavigationRoute.HOME_ROUTE
            Text(
                text = "home",
                fontWeight = if (isCurrentRoute) FontWeight.Bold else FontWeight.Normal
            )
        }

        TextButton(onClick = navToInterests) {
            val isCurrentRoute = currentRoute == JetNewsNavigationRoute.INTERESTS_ROUTE
            Text(
                text = "interests",
                fontWeight = if (isCurrentRoute) FontWeight.Bold else FontWeight.Normal
            )
        }
    }

}