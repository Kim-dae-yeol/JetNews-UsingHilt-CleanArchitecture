package com.kdy_soft.jetnewsclone.data.repositories.impl

import com.kdy_soft.jetnewsclone.data.repositories.interfaces.PostRepository
import com.kdy_soft.jetnewsclone.data.sources.PostSource
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    postSource: PostSource
) : PostRepository {
    override suspend fun getPost(postId: String?): Result<Post> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsFeed(): Result<PostsFeed> {
        TODO("Not yet implemented")
    }

    override fun observeFavorites(): Flow<Set<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavorite(postId: String) {
        TODO("Not yet implemented")
    }
}