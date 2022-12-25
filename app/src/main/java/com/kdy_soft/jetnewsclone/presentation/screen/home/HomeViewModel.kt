package com.kdy_soft.jetnewsclone.presentation.screen.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdy_soft.jetnewsclone.R
import com.kdy_soft.jetnewsclone.data.repositories.interfaces.PostRepository
import com.kdy_soft.jetnewsclone.model.Post
import com.kdy_soft.jetnewsclone.model.PostsFeed
import com.kdy_soft.jetnewsclone.util.ErrorMessage
import com.kdy_soft.jetnewsclone.util.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
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
        val searchInput = savedStateHandle.get<String>(SEARCH_INPUT_KEY)

        refreshPosts()
        viewModelScope.launch {
            postRepository.observeFavorites().collect { favorites ->
                _uiState.update { it.copy(favorites = favorites) }
                Logger.d("favorites are changed${_uiState.value.favorites}")
            }
            Logger.d("end of collect")

        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, postsFeed = null) }
            postRepository.getPostsFeed()
                .onFailure { th ->
                    Logger.e(msg = th.stackTraceToString())
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessages = it.errorMessages + ErrorMessage.ResourceMessage(
                                id = UUID.randomUUID().mostSignificantBits,
                                resId = R.string.refresh_failure
                            )
                        )
                    }
                }
                .onSuccess { feed ->
                    _uiState.update {
                        it.copy(
                            postsFeed = feed,
                            isLoading = false
                        )
                    }

                }

        }
    }

    fun toggleFavorites(favorite: String) {
        Logger.d("Toggle favorites:$favorite")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            postRepository.toggleFavorite(favorite)
            _uiState.update { it.copy(isLoading = false) }
            Logger.d("after toggle : ${_uiState.value.favorites}")
        }
    }

    fun selectArticle(postId: String) {
        interactedWithArticleDetails(postId)
    }

    fun errorShown(errorId: Long) {
        _uiState.update {
            it.copy(
                errorMessages = it.errorMessages.filterNot { error -> error.id == errorId }
            )
        }
    }

    fun interactedWithFeed() {
        _uiState.update {
            it.copy(
                selectedPostId = null,
                isArticleOpen = false
            )
        }
    }

    fun interactedWithArticleDetails(postId: String) {
        _uiState.update {
            it.copy(
                selectedPostId = postId,
                isArticleOpen = true
            )
        }
    }

    fun onSearchInputChanged(searchInput: String) {
        _uiState.update { it.copy(searchInput = searchInput) }
    }

    companion object {
        private const val SEARCH_INPUT_KEY = "home_search"

    }
}