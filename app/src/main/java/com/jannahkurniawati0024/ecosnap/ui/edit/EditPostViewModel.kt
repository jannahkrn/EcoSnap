package com.jannahkurniawati0024.ecosnap.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jannahkurniawati0024.ecosnap.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditPostUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class EditPostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _uiState = MutableStateFlow(EditPostUiState())
    val uiState: StateFlow<EditPostUiState> = _uiState

    fun updatePost(
        postId: String,
        description: String,
        userId: String,
        userName: String,
        userEmail: String,
        userPhotoUrl: String,
        imageUrl: String,
        createdAt: String
    ) {
        if (description.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Deskripsi tidak boleh kosong"
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.updatePost(
                postId, description, userId,
                userName, userEmail, userPhotoUrl,
                imageUrl, createdAt
            )
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Gagal mengedit postingan"
                    )
                }
            )
        }
    }
}