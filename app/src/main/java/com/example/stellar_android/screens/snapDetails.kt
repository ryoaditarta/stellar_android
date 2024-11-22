package com.example.stellar_android.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stellar_android.components.Snap
import com.example.stellar_android.components.SnapRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.stellar_android.components.Typography
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun snapDetails(navController: NavController, snapId: Int?) {
    var snap by remember { mutableStateOf<Snap?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var nameText by remember { mutableStateOf(TextFieldValue("")) }
    var showDeleteDialog by remember { mutableStateOf(false) } // State for showing the delete confirmation dialog

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(snapId) {
        snapId?.let {
            snap = SnapRepository.getSnaps().firstOrNull { it.id == snapId }
            nameText = TextFieldValue(snap?.name.orEmpty())
        }
    }

    if (snap == null) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack() // Navigasi kembali ke halaman sebelumnya
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { showDeleteDialog = true } // Show the delete confirmation dialog
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                }

                snap?.let { nonNullSnap ->
                    val file = File(nonNullSnap.filePath)
                    val bitmap = if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null

                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Snap Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        if (isEditing) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                                    .height(180.dp)
                                    .background(Color(0xFFFFFFFF))
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                BasicTextField(
                                    value = nameText,
                                    onValueChange = { nameText = it },
                                    textStyle = Typography.bodyMedium,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize()
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                                    .height(180.dp)
                                    .background(Color(0xFF2D2D2D))
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = snap?.name.orEmpty(),
                                    style = Typography.bodyMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFB286FD))
                        .clickable {
                            if (isEditing) {
                                snap?.name = nameText.text
                                coroutineScope.launch {
                                    snap?.let { SnapRepository.updateSnap(it) }
                                }
                            }
                            isEditing = !isEditing
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                        contentDescription = if (isEditing) "Save" else "Edit",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )

                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = if (isEditing) "Save" else "Edit",
                        color = Color.White,
                        style = Typography.bodyMedium
                    )
                }
            }

            // Confirmation Dialog
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Confirm Delete") },
                    text = { Text("Are you sure you want to delete this snap? This action cannot be undone.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                coroutineScope.launch {
                                    snap?.let { SnapRepository.deleteSnap(it) }
                                }
                                navController.popBackStack()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

