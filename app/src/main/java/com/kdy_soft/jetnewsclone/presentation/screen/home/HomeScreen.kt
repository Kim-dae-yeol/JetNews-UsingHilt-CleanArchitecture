package com.kdy_soft.jetnewsclone.presentation.screen.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.kdy_soft.jetnewsclone.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import com.kdy_soft.jetnewsclone.presentation.rememberContentPaddingForScreen
import com.kdy_soft.jetnewsclone.presentation.screen.article.postContentItems
import com.kdy_soft.jetnewsclone.presentation.screen.article.sharePost
import com.kdy_soft.jetnewsclone.presentation.utils.BookmarkButton
import com.kdy_soft.jetnewsclone.presentation.utils.FavoriteButton
import com.kdy_soft.jetnewsclone.presentation.utils.ShareButton
import com.kdy_soft.jetnewsclone.presentation.utils.TextSettingsButton
import com.kdy_soft.jetnewsclone.ui.theme.JetNewsCloneTheme
import com.kdy_soft.jetnewsclone.util.Logger
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

//Dynamic Ui 로 변경할 수 있기는 함

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeFeedWithArticleDetailsScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onToggleFavorite: (String) -> Unit,
    onSelectPost: (String) -> Unit,
    onRefreshPost: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithList: () -> Unit,
    onInteractWithDetail: (String) -> Unit,
    onSearchInputChanged: (String) -> Unit,
    openDrawer: () -> Unit,
    homeListLazyListState: LazyListState,
    articleDetailLazyListStates: Map<String, LazyListState>,
    snackbarHostState: SnackbarHostState
) {
    HomeScreenWithList(
        uiState = uiState,
        showTopAppBar = showTopAppBar,
        onRefreshPost = onRefreshPost,
        onErrorDismiss = onErrorDismiss,
        openDrawer = openDrawer,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) { hasPostUiState, contentModifier ->
        val contentPadding = rememberContentPaddingForScreen(
            additionalTop = if (showTopAppBar) 0.dp else 8.dp,
            excludeTop = showTopAppBar
        )

        Row(contentModifier) {
            PostList(
                postsFeed = hasPostUiState.postsFeed,
                favorites = hasPostUiState.favorites,
                showExpandedSearch = !showTopAppBar,
                onArticleTapped = onSelectPost,
                onToggleFavorite = onToggleFavorite,
                onSearchInputChanged = onSearchInputChanged,
                contentPadding = contentPadding,
                modifier = Modifier
                    .width(332.dp)
                    .notifyInput(onInteractWithList),
                state = homeListLazyListState,
                searchInput = hasPostUiState.searchInput,
            )
            Crossfade(targetState = hasPostUiState.selectedPost) { detail ->
                val detailLazyListState by remember {
                    derivedStateOf {
                        articleDetailLazyListStates.getValue(detail.id)
                    }
                }

                key(detail.id) {
                    LazyColumn(
                        state = detailLazyListState,
                        contentPadding = contentPadding,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                            .notifyInput { onInteractWithDetail(detail.id) }
                    ) {
                        stickyHeader {
                            val context = LocalContext.current
                            PostTopBar(
                                isFavorite = hasPostUiState.favorites.contains(detail.id),
                                onToggleFavorite = { onToggleFavorite(detail.id) },
                                onSharePost = { sharePost(detail, context) }
                            )
                        }

                        postContentItems(detail)
                    }
                }
            }
        }
    }
}

@Composable
private fun PostTopBar(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSharePost: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)),
        modifier = modifier.padding(end = 16.dp)
    ) {
        Row(Modifier.padding(horizontal = 8.dp)) {
            FavoriteButton(onClick = {/*TODO : implements event*/ })
            BookmarkButton(isFavorite, onClick = onToggleFavorite)
            ShareButton(onClick = onSharePost)
            TextSettingsButton(onClick = {/*TODO : implements event*/ })
        }

    }

}


@Composable
private fun PostList(
    postsFeed: PostsFeed,
    favorites: Set<String>,
    showExpandedSearch: Boolean,
    onArticleTapped: (postId: String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState(),
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        state = state
    ) {
        if (showExpandedSearch) {
            item {
                HomeSearch(
                    Modifier.padding(horizontal = 16.dp),
                    searchInput = searchInput,
                    onSearchInputChanged = onSearchInputChanged
                )
            }
        }
        item { PostListTopSection(postsFeed.highlightedPost, onArticleTapped) }
        if (postsFeed.recommendedPosts.isNotEmpty()) {
            item {
                PostListSimpleSection(
                    postsFeed.recommendedPosts,
                    onArticleTapped,
                    favorites,
                    onToggleFavorite
                )
            }
        }

        if (postsFeed.popularPosts.isNotEmpty() && !showExpandedSearch) {
            item {
                PostListPopularSection(
                    postsFeed.popularPosts, onArticleTapped
                )
            }
        }

        if (postsFeed.recentPosts.isNotEmpty()) {
            item {
                PostListHistorySection(
                    postsFeed.recentPosts,
                    onArticleTapped,
                    { postId ->/*TODO : implements event [doNotRecommend]*/ },
                    { postId ->/*TODO : implements event [addToFavorite]*/ }
                )
            }
        }
    }
}

@Composable
fun PostListSimpleSection(
    posts: List<Post>,
    navToDetail: (postId: String) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit
) {
    Column {
        posts.forEach { post ->
            SimplePostCard(
                post = post,
                navigateToArticle = navToDetail,
                isFavorite = favorites.contains(post.id),
                onToggleFavorite = { onToggleFavorite(post.id) }
            )
            PostListDivider()
        }
    }
}

@Composable
fun PostListHistorySection(
    posts: List<Post>,
    navToDetail: (postId: String) -> Unit,
    doNotRecommend: (postId: String) -> Unit,
    addToFavorite: (postId: String) -> Unit
) {
    Column {
        posts.forEach { post ->
            PostCardHistory(
                post = post,
                navigateToArticle = navToDetail,
                doNotRecommend = doNotRecommend,
                addToFavorites = addToFavorite
            )
            PostListDivider()
        }
    }

}

@Composable
fun PostListPopularSection(posts: List<Post>, navToDetail: (postId: String) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.home_popular_section_title),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { post ->
                PostCardPopular(post,navToDetail)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        PostListDivider()
    }
}

@Composable
fun PostListTopSection(
    post: Post,
    navToDetail: (postId: String) -> Unit
) {
    Text(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        text = stringResource(id = R.string.home_top_section_title),
        style = MaterialTheme.typography.titleMedium
    )
    PostCardBig(post = post, modifier = Modifier.clickable { navToDetail(post.id) })
    PostListDivider()
}

@Composable
private fun PostListDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    )

}

@Composable
fun HomeFeedScreen(
    homeUiState: HomeUiState,
    showTopAppBar: Boolean,
    onErrorDismiss: (Long) -> Unit,
    onRefreshPosts: () -> Unit,
    onSelectPost: (postId: String) -> Unit,
    openDrawer: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
    homeListLazyListState: LazyListState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    HomeScreenWithList(
        uiState = homeUiState,
        showTopAppBar = showTopAppBar,
        onRefreshPost = onRefreshPosts,
        onErrorDismiss = onErrorDismiss,
        openDrawer = openDrawer,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) { hasPostUiState, contentModifier ->

        PostList(
            postsFeed = hasPostUiState.postsFeed,
            favorites = hasPostUiState.favorites,
            showExpandedSearch = !showTopAppBar,
            onArticleTapped = onSelectPost,
            onToggleFavorite = onToggleFavorite,
            onSearchInputChanged = onSearchInputChanged,
            searchInput = searchInput,
            modifier = contentModifier,
            contentPadding = rememberContentPaddingForScreen(
                additionalTop = if (showTopAppBar) 0.dp else 8.dp,
                excludeTop = showTopAppBar
            ),
            state = homeListLazyListState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenWithList(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    onRefreshPost: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    openDrawer: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    hasPostsContent: @Composable (
        uiState: HomeUiState.HasPosts,
        modifier: Modifier
    ) -> Unit
) {
    val homeTopAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = homeTopAppBarState)
    Logger.d("showTopBar:$showTopAppBar")
    Scaffold(topBar = {
        if (showTopAppBar) {
            HomeTopAppBar(
                openDrawer = openDrawer,
                topAppBarState = homeTopAppBarState,
                scrollBehavior = scrollBehavior
            )
        }
    }) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)

        LoadingContent(
            empty = when (uiState) {
                is HomeUiState.NoPosts -> uiState.isLoading
                is HomeUiState.HasPosts -> false
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefreshPosts = onRefreshPost,
            content = {
                when (uiState) {
                    is HomeUiState.HasPosts -> {
                        hasPostsContent(uiState, contentModifier)
                    }
                    is HomeUiState.NoPosts -> {
                        if (uiState.errorMessages.isEmpty()) {
                            TextButton(
                                onClick = onRefreshPost,
                                modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.home_tap_to_load_content),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            //Box(modifier = contentModifier.fillMaxSize())
                        }
                    }

                }
            }
        )
    }

    if (uiState.errorMessages.isNotEmpty()) {
        val errorMessage = remember(uiState) { uiState.errorMessages.first() }

        val errorMessageText = errorMessage.getMessageString(LocalContext.current.resources)
        val retryMessageText = stringResource(id = R.string.retry)

        //상태로 저장함으로써 람다함수가 변경된 경우에도 LaunchedEffect 함수가 재실행 되지 않고 가장 최근의 람다함수가
        //LaunchedEffect 에서 사용되도록 remember
        val onRefreshPostState by rememberUpdatedState(onRefreshPost)
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        LaunchedEffect(errorMessageText, retryMessageText, snackbarHostState) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorMessageText,
                actionLabel = retryMessageText
            )

            if (snackbarResult == SnackbarResult.ActionPerformed) {
                onRefreshPostState()
            }

            onErrorDismissState(errorMessage.id)
        }
    }
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}


@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefreshPosts: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefreshPosts,
            content = content,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {
    val title = stringResource(id = R.string.app_name)

    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.ic_jetnews_wordmark),
                contentDescription = title,
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxSize()
            )
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painterResource(id = R.drawable.ic_jetnews_logo),
                    contentDescription = stringResource(id = R.string.cd_open_navigation_drawer)
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO : Open search*/ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.cd_search)
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun HomeListWithArticleDetailsScreen() {

}


@Composable
private fun HomeSearch(
    modifier: Modifier = Modifier,
    searchInput: String = "",
    onSearchInputChanged: (String) -> Unit
) {

}

private fun Modifier.notifyInput(block: () -> Unit) =
    composed {
        val blockState = rememberUpdatedState(block)
        pointerInput(Unit) {
            while (currentCoroutineContext().isActive) {
                awaitPointerEventScope {
                    awaitPointerEvent(PointerEventPass.Initial)
                    blockState.value()
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewHomeTopAppBar() {
    JetNewsCloneTheme {
        Surface {
            HomeTopAppBar(openDrawer = { /*TODO*/ })
        }
    }
}