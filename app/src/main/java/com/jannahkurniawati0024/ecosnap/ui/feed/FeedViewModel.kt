package com.jannahkurniawati0024.ecosnap.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jannahkurniawati0024.ecosnap.data.model.Post
import com.jannahkurniawati0024.ecosnap.data.repository.PostRepository
import com.jannahkurniawati0024.ecosnap.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FeedUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ✅ Ganti ViewModel -> AndroidViewModel agar bisa akses Context untuk cek internet
class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository()

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState

    init {
        loadFeed()
    }

    fun loadFeed() {
        // ✅ Cek koneksi internet sebelum request
        if (!NetworkUtils.isInternetAvailable(getApplication())) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Tidak ada koneksi internet. Periksa jaringan kamu dan coba lagi."
            )
            return
        }

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
        // ✅ Cek koneksi internet sebelum delete
        if (!NetworkUtils.isInternetAvailable(getApplication())) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Tidak ada koneksi internet. Tidak dapat menghapus postingan."
            )
            return
        }

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