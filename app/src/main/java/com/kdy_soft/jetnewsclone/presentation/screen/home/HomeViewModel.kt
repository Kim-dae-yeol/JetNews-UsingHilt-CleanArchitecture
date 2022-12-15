package com.kdy_soft.jetnewsclone.presentation.screen.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdy_soft.jetnewsclone.data.repositories.interfaces.PostRepository
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import com.kdy_soft.jetnewsclone.util.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>
    val searchInput: String

    data class NoPosts(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String,
    ) : HomeUiState

    data class HasPosts(
        val postsFeed: PostsFeed,
        val selectedPost: Post,
        val isArticleOpen: Boolean,
        val favorites: Set<String>,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
        override val searchInput: String
    ) : HomeUiState
}

private data class HomeViewModeState(
    val postsFeed: PostsFeed? = null,
    val selectedPostId: String? = null,
    val isArticleOpen: Boolean = false,
    val favorites: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = ""
) {
    fun toUiState(): HomeUiState {
        return if (postsFeed == null) {
            HomeUiState.NoPosts(
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        } else {
            HomeUiState.HasPosts(
                postsFeed = postsFeed,
                isArticleOpen = isArticleOpen,
                favorites = favorites,
                selectedPost = postsFeed.allPosts.find { it.id == selectedPostId }
                    ?: postsFeed.highlightedPost,
                isLoading = isLoading,
                errorMessages = errorMessages,
                searchInput = searchInput
            )
        }
    }
}

/**
 * 특이점 :
 *  Post 가 있는 경우와 없는 경우 두가지로 컴포저블 함수가 나뉘기 때문에
 *  sealed interface 로 두 가지로 분기 할 수 있도록 만든 후 내부적으로 두가지의 경우를 직접 사용해서 쓰는 것이 아닌,
 *  ViewModel 에서만 사용하는 상태 클래스를 만든 후
 *  ui 에서는 sealed class 를 노출시킨다.
 *  ----> 이런 경우는 처음보지만 직관적으로 와닿는 것은 더 큰 느낌?
 *
 * events
 *  1. refreshPosts
 *  2. toggleFavourites
 *  3. selectArticle
 *  4. errorShown : delete errorMessage from List
 *  5. interactedWithFeed
 *  6. interactedWithArticleDetails
 *  7. onSearchInputChanged
 * */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val postRepository: PostRepository /*TODO : all event conduct by this instance*/
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeViewModeState(isLoading = true))
    val uiState = _uiState.map(HomeViewModeState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _uiState.value.toUiState()
        )


    init {
        refreshPosts()
        viewModelScope.launch {

        }
    }
    fun refreshPosts() {

    }

    fun toggleFavorites(favorite: String) {

    }

    fun selectArticle(postId: String) {

    }

    fun errorShown(errorId: Long) {

    }

    fun interactedWithFeed() {

    }

    fun interactedWithArticleDetails(postId: String) {

    }

    fun onSearchInputChanged(searchInput: String) {

    }


}