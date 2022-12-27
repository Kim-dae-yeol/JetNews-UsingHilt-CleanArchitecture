package com.kdy_soft.jetnewsclone.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kdy_soft.jetnewsclone.R
import com.kdy_soft.jetnewsclone.presentation.JetNewsNavigationRoute.HOME_ROUTE
import com.kdy_soft.jetnewsclone.presentation.JetNewsNavigationRoute.INTERESTS_ROUTE
import com.kdy_soft.jetnewsclone.util.Logger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navToHome: () -> Unit,
    navToInterests: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(120.dp)
            .systemBarsPadding()
    ) {
        JetNewsLogo(modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp))
        IconButton(onClick = closeDrawer) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "close drawer")
        }

        val mainNaves = remember { JetNewsMainNavigation.values() }
        mainNaves.forEach { item ->
            val action = when (item.route) {
                HOME_ROUTE -> {
                    navToHome
                }
                INTERESTS_ROUTE -> {
                    navToInterests
                }
                else -> {
                    throw NoSuchElementException("Not supported route")
                }
            }

            NavigationDrawerItem(
                label = { Text(text = stringResource(item.labelId)) },
                selected = item.route == currentRoute,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelId)
                    )
                },
                onClick = { action();closeDrawer() },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }

}

@Composable
private fun JetNewsLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            painterResource(id = R.drawable.ic_jetnews_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_jetnews_wordmark),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}