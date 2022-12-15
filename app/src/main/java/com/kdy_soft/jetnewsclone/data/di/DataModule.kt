package com.kdy_soft.jetnewsclone.data.di

import com.kdy_soft.jetnewsclone.data.repositories.impl.PostRepositoryImpl
import com.kdy_soft.jetnewsclone.data.repositories.interfaces.PostRepository
import com.kdy_soft.jetnewsclone.data.sources.PostSource
import com.kdy_soft.jetnewsclone.data.sources.local.FakePostSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun providesPostSource(): PostSource {
        return FakePostSource
    }

    @Provides
    fun providesPostRepository(
        postSource: PostSource
    ): PostRepository {
        return PostRepositoryImpl(postSource)
    }
}