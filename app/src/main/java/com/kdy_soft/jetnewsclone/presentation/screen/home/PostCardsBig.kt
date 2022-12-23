package com.kdy_soft.jetnewsclone.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.R

@Composable
fun PostCardBig(
    post: Post,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography
    //column
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        //image
        val imageModifier = Modifier
            .heightIn(180.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)

        val postPainter = painterResource(id = post.imageId)
        Image(
            painter = postPainter,
            contentDescription = post.title,
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(16.dp))
        //title
        Text(
            text = post.title,
            style = typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        //metadata.author.name
        Text(
            text = post.metadata.author.name,
            style = typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        //readTimeInMin
        Text(
            text = stringResource(
                id = R.string.home_post_min_read,
                formatArgs = arrayOf(
                    post.metadata.date,
                    post.metadata.readTimeMinutes
                )
            ),
            style = typography.labelLarge
        )
    }


}