package com.kdy_soft.jetnewsclone.data.repositories.interfaces

import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPost(postId: String?): Result<Post>

    suspend fun getPostsFeed(): Result<PostsFeed>

    fun observeFavorites(): Flow<Set<String>>

    suspend fun toggleFavorite(postId: String)
}