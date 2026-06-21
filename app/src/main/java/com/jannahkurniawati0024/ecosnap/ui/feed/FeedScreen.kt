package com.jannahkurniawati0024.ecosnap.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jannahkurniawati0024.ecosnap.R
import com.jannahkurniawati0024.ecosnap.ui.components.LoadingOverlay
import com.jannahkurniawati0024.ecosnap.ui.components.PostCard
import com.jannahkurniawati0024.ecosnap.utils.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEdit: (String, String) -> Unit,
    feedViewModel: FeedViewModel = viewModel()
) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val userProfile by authManager.userProfileFlow.collectAsState(initial = null)
    val uiState by feedViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🌿 " + stringResource(R.string.feed_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32)
                ),
                actions = {
                    IconButton(onClick = { feedViewModel.loadFeed() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profil",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Buat Postingan")
            }
        },
        containerColor = Color(0xFFF1F8E9)
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                uiState.isLoading -> {
                    LoadingOverlay()
                }
                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "⚠️ ${uiState.errorMessage}",
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { feedViewModel.loadFeed() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2E7D32)
                            )
                        ) {
                            Text("Coba Lagi", color = Color.White)
                        }
                    }
                }
                uiState.posts.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🌱 " + stringResource(R.string.no_posts),
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.posts, key = { it.id }) { post ->
                            PostCard(
                                post = post,
                                currentUserId = userProfile?.id ?: "",
                                onDeleteClick = { deletedPost ->
                                    feedViewModel.deletePost(
                                        deletedPost,
                                        userProfile?.id ?: ""
                                    )
                                },
                                onEditClick = { editPost ->
                                    onNavigateToEdit(editPost.id, editPost.description)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}