package com.kdy_soft.jetnewsclone.data.repositories.impl

import com.kdy_soft.jetnewsclone.data.repositories.interfaces.PostRepository
import com.kdy_soft.jetnewsclone.data.sources.PostSource
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postSource: PostSource
) : PostRepository {
    override suspend fun getPost(postId: String): Result<Post> = postSource.getPost(postId)

    override suspend fun getPostsFeed(): Result<PostsFeed> = postSource.getPostsFeed()

    override fun observeFavorites(): Flow<Set<String>> = postSource.observeFavorites()

    override suspend fun toggleFavorite(postId: String) = postSource.toggleFavorite(postId)
}