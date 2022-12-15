package com.kdy_soft.jetnewsclone.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.kdy_soft.jetnewsclone.presentation.screen.article.ArticleScreen
import com.kdy_soft.jetnewsclone.util.Logger

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = homeViewModel.uiState.value,
        key1 = lifecycle,
        key2 = homeViewModel
    ) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            homeViewModel.uiState.collect { value = it }
        }
    }

    HomeRoute(
        uiState = uiState,
        isExpandedScreen = isExpandedScreen,
        openDrawer = openDrawer,
        snackbarHostState = snackbarHostState,
        onToggleFavorite = homeViewModel::toggleFavorites,
        onSelectPost = homeViewModel::selectArticle,
        onRefreshPost = homeViewModel::refreshPosts,
        onErrorDismiss = homeViewModel::errorShown,
        onInteractWithPost = homeViewModel::interactedWithFeed,
        onInteractWithArticleDetails = homeViewModel::interactedWithArticleDetails,
        onSearchInputChanged = homeViewModel::onSearchInputChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onToggleFavorite: (String) -> Unit,
    onSelectPost: (String) -> Unit,
    onRefreshPost: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithPost: () -> Unit,
    onInteractWithArticleDetails: (String) -> Unit,
    onSearchInputChanged: (String) -> Unit
) {
    val homeLazyListState = rememberLazyListState()
    val articleDetailLazyListState = when (uiState) {
        is HomeUiState.HasPosts -> uiState.postsFeed.allPosts
        is HomeUiState.NoPosts -> emptyList()
    }.associate { post ->
        key(post.id) {
            post.id to rememberLazyListState()
        }
    }
    val homeScreenType = getHomeScreenType(isExpandedScreen, uiState)
    Logger.d("homeScreenType is $homeScreenType")

    when (homeScreenType) {
        HomeScreenType.FeedWithArticleDetails -> {
            HomeFeedWithArticleDetailsScreen(
                uiState = uiState,
                showTopAppBar = !isExpandedScreen,
                onToggleFavorite = onToggleFavorite,
                onSelectPost = onSelectPost,
                onRefreshPost = onRefreshPost,
                onErrorDismiss = onErrorDismiss,
                onInteractWithList = onInteractWithPost,
                onInteractWithDetail = onInteractWithArticleDetails,
                onSearchInputChanged = onSearchInputChanged,
                openDrawer = openDrawer,
                homeListLazyListState = homeLazyListState,
                articleDetailLazyListStates = articleDetailLazyListState,
                snackbarHostState = snackbarHostState
            )
        }

        HomeScreenType.Feed -> HomeFeedScreen()
        HomeScreenType.ArticleDetails -> {
            check(uiState is HomeUiState.HasPosts)
            ArticleScreen(
                post = uiState.selectedPost,
                isExpanded = isExpandedScreen,
                isFavorite = uiState.favorites.contains(uiState.selectedPost.id),
                onToggleFavorite = { onToggleFavorite(uiState.selectedPost.id) },
                onBack = onInteractWithPost,
                lazyListState = articleDetailLazyListState.getValue(uiState.selectedPost.id)
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {}, navigationIcon = {
            IconButton(
                onClick = openDrawer
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        })
        HomeFeedScreen()
    }
}

private enum class HomeScreenType {
    FeedWithArticleDetails,
    Feed,
    ArticleDetails
}

private fun getHomeScreenType(
    isExpandedScreen: Boolean,
    uiState: HomeUiState
): HomeScreenType = if (isExpandedScreen) {
    HomeScreenType.FeedWithArticleDetails
} else {
    when (uiState) {
        is HomeUiState.HasPosts -> {
            if (uiState.isArticleOpen) {
                HomeScreenType.ArticleDetails
            } else {
                HomeScreenType.Feed
            }
        }
        is HomeUiState.NoPosts -> HomeScreenType.Feed
    }
}
