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
    object EditPost : Screen("edit_post/{postId}/{description}") {
        fun createRoute(postId: String, description: String): String {
            return "edit_post/$postId/${Uri.encode(description)}"
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
                onNavigateToEdit = { postId, description ->
                    navController.navigate(Screen.EditPost.createRoute(postId, description))
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
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val description = Uri.decode(
                backStackEntry.arguments?.getString("description") ?: ""
            )
            EditPostScreen(
                postId = postId,
                initialDescription = description,
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