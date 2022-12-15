package com.kdy_soft.jetnewsclone.data.sources.local

import com.kdy_soft.jetnewsclone.data.sources.PostSource
import com.kdy_soft.jetnewsclone.data.sources.posts
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


object FakePostSource : PostSource {
    private val postFeed: PostsFeed = posts

    private val favorites = mutableSetOf<String>()
    private val favoritesFlow: MutableStateFlow<Set<String>> = MutableStateFlow(favorites)

    override suspend fun getPost(postId: String?): Result<Post> = kotlin.runCatching {
        postFeed.allPosts.find { it.id == postId } ?: throw NoSuchElementException()
    }

    override suspend fun getPostsFeed(): Result<PostsFeed> = kotlin.runCatching {
        postFeed
    }

    override fun observeFavorites(): Flow<Set<String>> = favoritesFlow

    override suspend fun toggleFavorite(postId: String) {
        val added = !favorites.add(postId)
        if (added) {
            favorites.remove(postId)
        }
        favoritesFlow.emit(favorites)
    }

}