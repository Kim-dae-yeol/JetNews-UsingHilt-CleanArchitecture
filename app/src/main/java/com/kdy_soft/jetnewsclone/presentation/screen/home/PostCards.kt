package com.kdy_soft.jetnewsclone.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Preview
@Composable
private fun PostCardPreview() {
    SimplePostCard(
        post = post1,
        navigateToArticle = {},
        isFavorite = false,
        onToggleFavorite = { }
    )
}

@Preview
@Composable
private fun PostCardPreviewBookmarked() {
    SimplePostCard(
        post = post1,
        navigateToArticle = {},
        isFavorite = true,
        onToggleFavorite = { }
    )
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
        PostImage(post)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
            PostTitle(post)
            AuthorAndReadTime(post)
            BookmarkButton(
                isBookmarked = isFavorite,
                onClick = onToggleFavorite,
                modifier = Modifier
                    .clearAndSetSemantics { }
                    .padding(vertical = 2.dp, horizontal = 6.dp)
            )
        }
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
                R.string.author_and_time,
                formatArgs = arrayOf(post.metadata.author.name, post.metadata.readTimeMinutes)
            ),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}