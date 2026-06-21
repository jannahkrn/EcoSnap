package com.jannahkurniawati0024.ecosnap.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jannahkurniawati0024.ecosnap.data.model.Post
import com.jannahkurniawati0024.ecosnap.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FeedUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FeedViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getFeed()
            result.fold(
                onSuccess = { posts ->
                    _uiState.value = _uiState.value.copy(
                        posts = posts,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Gagal memuat data"
                    )
                }
            )
        }
    }

    fun deletePost(post: Post, userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repository.deletePost(post.id, userId)
            result.fold(
                onSuccess = {
                    val updatedPosts = _uiState.value.posts.filter { it.id != post.id }
                    _uiState.value = _uiState.value.copy(
                        posts = updatedPosts,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Gagal menghapus postingan"
                    )
                }
            )
        }
    }
}