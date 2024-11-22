package com.example.stellar_android.navigation

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stellar_android.screens.Homepage
import com.example.stellar_android.screens.LoginScreen
import com.example.stellar_android.screens.SignInScreen
import com.example.stellar_android.screens.SignInOrLoginScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.stellar_android.components.Snap
import com.example.stellar_android.components.SnapRepository
import com.example.stellar_android.screens.JournalDetail
import com.example.stellar_android.components.capturePage
import com.example.stellar_android.screens.journalPage
import com.example.stellar_android.screens.profilePage
import com.example.stellar_android.screens.snapDetails
import com.google.firebase.firestore.FirebaseFirestore
import snapsPage

@Composable
fun StellarNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val bitmapList = remember { mutableListOf<Bitmap>() }  // Shared mutable list
    val context = LocalContext.current

    // Inisialisasi database saat navigasi dimulai
    LaunchedEffect(key1 = Unit) {
        SnapRepository.initDatabase(context) // Inisialisasi database
    }

    val snaps = remember { mutableStateOf<List<Snap>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Ambil data snap dari repository setelah database diinisialisasi
        snaps.value = SnapRepository.getSnaps() // Mengambil data snaps dari repository
    }

    // Check if user is logged in when the navigation starts
    LaunchedEffect(key1 = Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            navController.navigate("Homepage") {
                popUpTo("SignInOrLoginScreen") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "SignInOrLoginScreen", modifier = modifier) {
        composable("LoginScreen") { LoginScreen(navController) }
        composable("SignInOrLoginScreen") { SignInOrLoginScreen(navController) }
        composable("SignInScreen") { SignInScreen(navController) }
        composable("Homepage") { Homepage(navController) }
        composable("profilePage") { profilePage(navController) }
        composable("journalPage") { journalPage(navController) }
        composable("snapsPage") {
            // Ambil data snaps dari SnapRepository
            val snaps = remember { mutableStateOf<List<Snap>>(emptyList()) }

            LaunchedEffect(Unit) {
                // Ambil data dari SnapRepository dan simpan di snaps
                snaps.value = SnapRepository.getSnaps()
            }

            // Kirim data snaps ke composable snapsPage
            snapsPage(navController, snaps)
        }
        composable("capturePage"){capturePage(navController, bitmapList = bitmapList)}
        // Updated JournalDetail route to include journalId
        composable(
            "JournalDetail/{journalId}/{title}/{content}/{created_at}/{updated_at}"
        ) { backStackEntry ->
            // Extract parameters from the route
            val journalId = backStackEntry.arguments?.getString("journalId") ?: "No ID"
            val title = backStackEntry.arguments?.getString("title") ?: "No Title"
            val content = backStackEntry.arguments?.getString("content") ?: "No Content"
            val created_at = backStackEntry.arguments?.getString("created_at") ?: "No Date"
            val updated_at = backStackEntry.arguments?.getString("updated_at") ?: "No Date"

            JournalDetail(
                navController = navController,
                journalId = journalId,
                title = title,
                content = content,
                created_at = created_at,
                updated_at = updated_at
            )
        }

        composable("snapDetails/{snapId}") { backStackEntry ->
            // Retrieve the snapId from the backStackEntry
            val snapId = backStackEntry.arguments?.getString("snapId")?.toInt()
            // Pass the snapId to the snapDetails composable
            snapDetails(navController = navController, snapId)
        }

    }
}
