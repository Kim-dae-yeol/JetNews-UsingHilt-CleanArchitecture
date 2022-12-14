package com.kdy_soft.jetnewsclone.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdy_soft.jetnewsclone.R
import com.kdy_soft.jetnewsclone.data.sources.post1
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.presentation.utils.BookmarkButton
import com.kdy_soft.jetnewsclone.ui.theme.JetNewsCloneTheme

@Preview
@Composable
private fun PostCardPreview() {
    JetNewsCloneTheme {
        Surface {
            SimplePostCard(
                post = post1,
                navigateToArticle = {},
                isFavorite = false,
                onToggleFavorite = { }
            )
        }
    }
}

@Preview
@Composable
private fun PostCardPreviewBookmarked() {
    JetNewsCloneTheme {
        Surface {
            SimplePostCard(
                post = post1,
                navigateToArticle = {},
                isFavorite = true,
                onToggleFavorite = { }
            )
        }
    }
}


@Composable
fun PostCardHistory(
    post: Post,
    navigateToArticle: (postId: String) -> Unit,
    doNotRecommend: (postId: String) -> Unit,
    addToFavorites: (postId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(false) }

    Row(modifier = modifier.clickable { navigateToArticle(post.id) }) {
        PostImage(post = post, modifier = Modifier.padding(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.home_post_based_on_history),
                style = MaterialTheme.typography.labelMedium
            )
            PostTitle(post = post)
            AuthorAndReadTime(post = post, modifier = Modifier.padding(top = 4.dp))
        }
        IconButton(onClick = { openDialog = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = stringResource(id = R.string.cd_more_button)
            )
        }

    }
    if (openDialog) {
        AlertDialog(
            modifier = Modifier.padding(20.dp),
            onDismissRequest = { openDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.fewer_stroies),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    stringResource(id = R.string.fewer_stories_content),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.agree),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { openDialog = false }
                )
            }
        )
    }
}

@Composable
fun SimplePostCard(
    post: Post,
    navigateToArticle: (postId: String) -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actionLabel =
        if (isFavorite) stringResource(id = R.string.bookmark) else stringResource(id = R.string.unbookmark)

    Row(
        modifier = modifier
            .clickable { navigateToArticle(post.id) }
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction(
                        actionLabel,
                        action = { onToggleFavorite();true })
                )
            }
    ) {
        PostImage(post, modifier = Modifier.padding(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            PostTitle(post)
            AuthorAndReadTime(post)
        }
        BookmarkButton(
            isBookmarked = isFavorite,
            onClick = onToggleFavorite,
            modifier = Modifier
                .clearAndSetSemantics { }
                .padding(vertical = 2.dp, horizontal = 6.dp)
        )

    }
}


@Composable
fun PostImage(post: Post, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = post.imageThumbId),
        contentDescription = null,
        modifier = modifier
            .size(40.dp, 40.dp)
            .clip(MaterialTheme.shapes.small)
    )
}

@Composable
fun PostTitle(post: Post) {
    Text(
        text = post.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun AuthorAndReadTime(post: Post, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(
                R.string.home_post_min_read,
                formatArgs = arrayOf(post.metadata.author.name, post.metadata.readTimeMinutes)
            ),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}