package com.kdy_soft.jetnewsclone.presentation.screen.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kdy_soft.jetnewsclone.R
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.presentation.utils.BookmarkButton
import com.kdy_soft.jetnewsclone.presentation.utils.FavoriteButton
import com.kdy_soft.jetnewsclone.presentation.utils.ShareButton
import com.kdy_soft.jetnewsclone.presentation.utils.TextSettingsButton

/**
 * Stateless ArticleScreen that display Article Details
 * */
@Composable
fun ArticleScreen(
    post: Post,
    isExpanded: Boolean,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState() // ui element state
) {
    var unimplementedActionDialog by rememberSaveable { mutableStateOf(false) }
    if (unimplementedActionDialog) {
        FunctionalityNotAvailableDialog {
            unimplementedActionDialog = false
        }
    }

    Row(modifier.fillMaxSize()) {
        val context = LocalContext.current
        ArticleScreenContent(
            post = post,
            navigationIconContent = {
                if (!isExpanded) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            bottomBarContent = {
                if (!isExpanded) {
                    BottomAppBar(actions = {
                        FavoriteButton(onClick = { unimplementedActionDialog = true })
                        BookmarkButton(isBookmarked = isFavorite, onClick = onToggleFavorite)
                        ShareButton(onClick = { sharePost(post, context) })
                        TextSettingsButton(onClick = { unimplementedActionDialog = true })
                    })
                }
            },
            lazyListState = lazyListState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleScreenContent(
    post: Post,
    navigationIconContent: @Composable () -> Unit = {},
    bottomBarContent: @Composable () -> Unit = {},
    lazyListState: LazyListState = rememberLazyListState()
) {

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    Scaffold(
        topBar = {
            ArticleTopAppBar(
                title = post.publication?.name.orEmpty(),
                navigationIconContent = navigationIconContent,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = bottomBarContent
    ) { innerPadding ->
        PostContent(
            post = post,
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                //topBar and bottomBar's inner-padding
                .padding(innerPadding),
            state = lazyListState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleTopAppBar(
    title: String,
    navigationIconContent: @Composable () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.icon_article_background),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                )
                Text(
                    text = stringResource(R.string.published_in, title),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },
        navigationIcon = navigationIconContent,
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
private fun FunctionalityNotAvailableDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, text = {
        Text(
            text = stringResource(id = R.string.article_functionality_not_available),
            style = MaterialTheme.typography.bodyLarge
        )

    }, confirmButton = {
        TextButton(onClick = onDismiss) {
            Text(text = stringResource(id = R.string.close))
        }
    })
}

/**
 * show a share sheet for a post
 * @param post - to share
 * @param context Android Context to show share a sheet in
 * */
fun sharePost(
    post: Post,
    context: Context
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, post.title)
        putExtra(Intent.EXTRA_TEXT, post.url)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.article_share_post)
        )
    )

}









