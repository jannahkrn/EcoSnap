package com.jannahkurniawati0024.ecosnap.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jannahkurniawati0024.ecosnap.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

data class CreatePostUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class CreatePostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState

    fun createPost(
        userId: String,
        userEmail: String,
        userName: String,
        userPhotoUrl: String,
        description: String,
        imageFile: File
    ) {
        if (description.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Deskripsi tidak boleh kosong"
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.createPost(
                userId, userEmail, userName, userPhotoUrl, description, imageFile
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
                        errorMessage = error.message ?: "Gagal membuat postingan"
                    )
                }
            )
        }
    }
}