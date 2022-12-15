package com.kdy_soft.jetnewsclone.presentation.screen.interests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestsRoute(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {}, navigationIcon = {
            IconButton(
                onClick = openDrawer
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        })
        InterestsScreen()
    }
}