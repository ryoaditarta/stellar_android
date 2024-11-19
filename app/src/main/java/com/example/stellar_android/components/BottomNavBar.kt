package com.example.stellar_android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController

// Reusable Bottom Navigation Bar
@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    var showMenu by remember { mutableStateOf(false) }
    // Bottom Navigation Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF101010))
            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
    ) {
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Keep it at the bottom
                .fillMaxWidth()
                .padding(0.dp),
            containerColor = Color(0xFFB286FD) // Ensure it's above the menu
        ) {
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("Homepage") },
                icon = {
                    // Change icon based on selection state
                    if (currentRoute == "Homepage") {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    } else {
                        Icon(Icons.Outlined.Home, contentDescription = "Home")
                    }
                },
                modifier = Modifier.padding(0.dp)
//                label = { Text("Home") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("journalPage") },
                icon = {
                    // Change icon based on selection state
                    if (currentRoute == "journalPage") {
                        Icon(Icons.Default.Create, contentDescription = "Journal")
                    } else {
                        Icon(Icons.Outlined.Create, contentDescription = "Journal")
                    }
                },
                modifier = Modifier.padding(0.dp)
//                label = { Text("Journal") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("capturePage") },
                icon = {
                    Icon(
                        Icons.Default.AddCircle,
                        contentDescription = "Add",
                        modifier = Modifier.size(40.dp)  // Adjust the size of the icon
                    )
                },
                modifier = Modifier.padding(0.dp)
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("snapsPage") },
                icon = {
                    // Change icon based on selection state
                    if (currentRoute == "snapsPage") {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Snaps")
                    } else {
                        Icon(Icons.Outlined.PhotoCamera, contentDescription = "Snaps")
                    }
                },
                modifier = Modifier.padding(0.dp)
//                label = { Text("Snaps") }
            )
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate("profilePage") },
                icon = {
                    // Change icon based on selection state
                    if (currentRoute == "profilePage") {
                        Icon(Icons.Default.AccountBox, contentDescription = "Profile")
                    } else {
                        Icon(Icons.Outlined.AccountBox, contentDescription = "Profile")
                    }
                },
                modifier = Modifier.padding(0.dp)
//                label = { Text("Profile") }
            )
        }
    }
}
