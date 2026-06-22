package com.jannahkurniawati0024.ecosnap.ui.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jannahkurniawati0024.ecosnap.data.repository.PostRepository
import com.jannahkurniawati0024.ecosnap.utils.CloudinaryUploader
import com.jannahkurniawati0024.ecosnap.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CreatePostUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val uploadProgress: String = ""
)

class CreatePostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState

    fun createPost(
        context: Context,
        userId: String,
        userEmail: String,
        userName: String,
        userPhotoUrl: String,
        description: String,
        imageUri: Uri
    ) {
        if (description.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Deskripsi tidak boleh kosong"
            )
            return
        }

        // ✅ Cek koneksi internet sebelum upload
        if (!NetworkUtils.isInternetAvailable(context)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Tidak ada koneksi internet. Periksa jaringan kamu dan coba lagi."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                uploadProgress = "Mengupload gambar..."
            )

            val uploadResult = CloudinaryUploader.uploadImage(context, imageUri)

            uploadResult.fold(
                onSuccess = { imageUrl ->
                    _uiState.value = _uiState.value.copy(
                        uploadProgress = "Menyimpan postingan..."
                    )

                    val postResult = repository.createPost(
                        userId, userEmail, userName,
                        userPhotoUrl, description, imageUrl
                    )

                    postResult.fold(
                        onSuccess = {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isSuccess = true,
                                uploadProgress = ""
                            )
                        },
                        onFailure = { error ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Gagal menyimpan postingan",
                                uploadProgress = ""
                            )
                        }
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal upload gambar: ${error.message}",
                        uploadProgress = ""
                    )
                }
            )
        }
    }
}