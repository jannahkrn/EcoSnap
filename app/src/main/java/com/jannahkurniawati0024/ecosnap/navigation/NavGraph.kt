package com.jannahkurniawati0024.ecosnap.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jannahkurniawati0024.ecosnap.ui.auth.LoginScreen
import com.jannahkurniawati0024.ecosnap.ui.create.CreatePostScreen
import com.jannahkurniawati0024.ecosnap.ui.edit.EditPostScreen
import com.jannahkurniawati0024.ecosnap.ui.feed.FeedScreen
import com.jannahkurniawati0024.ecosnap.ui.profile.ProfileScreen
import com.jannahkurniawati0024.ecosnap.utils.AuthManager

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Feed : Screen("feed")
    object CreatePost : Screen("create_post")
    object EditPost : Screen(
        "edit_post/{postId}/{description}/{userName}/{userEmail}/{userPhotoUrl}/{imageUrl}/{createdAt}"
    ) {
        fun createRoute(
            postId: String,
            description: String,
            userName: String,
            userEmail: String,
            userPhotoUrl: String,
            imageUrl: String,
            createdAt: String
        ): String {
            return "edit_post/" +
                    "${Uri.encode(postId)}/" +
                    "${Uri.encode(description)}/" +
                    "${Uri.encode(userName)}/" +
                    "${Uri.encode(userEmail)}/" +
                    "${Uri.encode(userPhotoUrl)}/" +
                    "${Uri.encode(imageUrl)}/" +
                    Uri.encode(createdAt)
        }
    }
    object Profile : Screen("profile")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val userProfile by authManager.userProfileFlow.collectAsState(initial = null)

    val startDestination = if (userProfile != null) Screen.Feed.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Feed.route) {
            FeedScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.CreatePost.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToEdit = { postId, description, userName, userEmail, userPhotoUrl, imageUrl, createdAt ->
                    navController.navigate(
                        Screen.EditPost.createRoute(
                            postId, description, userName,
                            userEmail, userPhotoUrl, imageUrl, createdAt
                        )
                    )
                }
            )
        }

        composable(Screen.CreatePost.route) {
            CreatePostScreen(
                onPostSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditPost.route,
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("userName") { type = NavType.StringType },
                navArgument("userEmail") { type = NavType.StringType },
                navArgument("userPhotoUrl") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("createdAt") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            EditPostScreen(
                postId = Uri.decode(args?.getString("postId") ?: ""),
                initialDescription = Uri.decode(args?.getString("description") ?: ""),
                userName = Uri.decode(args?.getString("userName") ?: ""),
                userEmail = Uri.decode(args?.getString("userEmail") ?: ""),
                userPhotoUrl = Uri.decode(args?.getString("userPhotoUrl") ?: ""),
                imageUrl = Uri.decode(args?.getString("imageUrl") ?: ""),
                createdAt = Uri.decode(args?.getString("createdAt") ?: ""),
                onEditSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}