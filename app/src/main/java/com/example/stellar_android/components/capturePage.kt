package com.example.stellar_android.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun capturePage(
    navController: NavController,
    bitmapList: MutableList<Bitmap> // Shared mutable list of bitmaps
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    val judul = rememberSaveable { mutableStateOf("") }
    val snapDao = remember {
        SnapDatabase.getAppDatabase(context).snapDao()
    }
    val interactionSource = remember { MutableInteractionSource() }

    // Track the pressed state
    val isPressed = interactionSource.collectIsPressedAsState()

    // Initialize the Camera Controller
    val imageCapture = remember { ImageCapture.Builder().build() }

    // State to manage the captured image
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // State to track if the camera is frozen (i.e., not updating)
    var isCameraFrozen by remember { mutableStateOf(false) }

    // ActivityResultLauncher to pick an image from the gallery
    // ActivityResultLauncher to pick an image from the gallery
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = loadBitmapFromUri(context, it)
            bitmap?.let { newBitmap ->
                capturedBitmap = newBitmap // Treat the gallery image like a captured image
                isCameraFrozen = true      // Freeze the camera
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (capturedBitmap != null) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            capturedBitmap = null // Reset captured image
                            isCameraFrozen = false // Unfreeze camera
                        },
                        // Position at top left
                    ) {
                        Text("Retry")
                    }

                    Button(
                        onClick = {
                            if (judul.value.isNullOrEmpty()) {
                                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
                            } else {
                                capturedBitmap?.let { bitmap ->
                                    val file = saveBitmapToFile(context, bitmap)
                                    val snap = Snap(
                                        name = judul.value!!, // Use the title provided
                                        timestamp = System.currentTimeMillis(),
                                        filePath = file.absolutePath
                                    )
                                    saveSnapToDatabase(snapDao, snap)
                                    bitmapList.add(bitmap)
                                    capturedBitmap = null
                                    isCameraFrozen = false
                                    navController.navigate("snapsPage")
                                    Toast.makeText(context, "Image saved with title!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            var hasCameraPermission by remember { mutableStateOf(false) }
            val cameraPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                hasCameraPermission = isGranted
            }

            LaunchedEffect(Unit) {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            if (hasCameraPermission) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Camera Preview only if the camera is not frozen
                    if (!isCameraFrozen) {
                        AndroidView(
                            modifier = Modifier.weight(1f),
                            factory = { context ->
                                PreviewView(context).apply {
                                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                                    scaleType = PreviewView.ScaleType.FILL_START
                                }
                            },
                            update = { previewView ->
                                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                                cameraProviderFuture.addListener({
                                    val cameraProvider = cameraProviderFuture.get()

                                    val preview = Preview.Builder().build()
                                    preview.setSurfaceProvider(previewView.surfaceProvider)

                                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                    try {
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            imageCapture
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        Toast.makeText(
                                            context,
                                            "Error starting camera: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }, ContextCompat.getMainExecutor(context))
                            }
                        )
                        Row(
                            modifier = Modifier
                                .height(120.dp)
                                .padding(20.dp)
                                .fillMaxWidth(), // Ensures row stretches across the screen
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically // Centers buttons vertically
                        ) {

                            IconButton(
                                onClick = {
                                    launcher.launch("image/*")  // Open gallery picker
                                },
                                modifier = Modifier
                                    .size(60.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = "Add from Gallery",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            // Camera Shutter Button (centered)
                            IconButton(
                                onClick = {
                                    // Capture image only if the camera is not frozen
                                    if (!isCameraFrozen) {
                                        takePicture(
                                            context = context,
                                            imageCapture = imageCapture,
                                            executor = executor,
                                            onImageCaptured = { bitmap ->
                                                capturedBitmap = bitmap // Save captured bitmap
                                                isCameraFrozen = true // Freeze camera
                                            },
                                            onError = { error ->
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                },
                                interactionSource = interactionSource,
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        Color(0xFFAAAAAA),
                                        shape = RoundedCornerShape(50.dp)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    contentDescription = "Capture",
                                    tint = Color.White,
                                    modifier = Modifier.size(80.dp)
                                )
                            }


                            IconButton(
                                onClick = {
                                    navController.navigate("Homepage")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }


                    }

                    capturedBitmap?.let { bitmap ->
                        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Display captured image
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Captured Image",
                                    modifier = Modifier
                                        .height(400.dp)
                                        .background(Color.Gray, RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // TextField for title
                                TextField(
                                    value = judul.value ?: "",
                                    onValueChange = { judul.value = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    label = { Text("Enter Title") },
                                    singleLine = true
                                )
                            }
                        }
                    }
                }
            } else {
                Text(text = "Camera permission is required to take a picture.")
            }
        }
    }
}


// Helper function to capture the image and convert it to a Bitmap
// Fungsi untuk memperbaiki orientasi gambar berdasarkan orientasi layar perangkat
fun fixImageOrientationBasedOnScreen(context: Context, imagePath: String): Bitmap {
    var bitmap = BitmapFactory.decodeFile(imagePath)
    val displayMetrics = context.resources.displayMetrics
    val isLandscape = displayMetrics.widthPixels > displayMetrics.heightPixels

    // Jika perangkat dalam mode landscape dan gambar portrait, putar 90 derajat
    if (isLandscape && bitmap.height > bitmap.width) {
        bitmap = rotateBitmap(bitmap, 90f)  // Putar gambar 90 derajat
    }
    // Jika perangkat dalam mode portrait dan gambar landscape, putar 90 derajat
    else if (!isLandscape && bitmap.width > bitmap.height) {
        bitmap = rotateBitmap(bitmap, 90f)  // Putar gambar 90 derajat
    }

    return bitmap
}

// Fungsi untuk memutar bitmap
fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

// Penempatan kode dalam pengambilan gambar
fun takePicture(
    context: Context,
    imageCapture: ImageCapture,
    executor: ExecutorService,
    onImageCaptured: (Bitmap) -> Unit,
    onError: (String) -> Unit
) {
    val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // Mengambil gambar dan memperbaiki orientasinya
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                val fixedBitmap = fixImageOrientationBasedOnScreen(context, file.absolutePath) // Memperbaiki orientasi
                onImageCaptured(fixedBitmap) // Kirim bitmap yang sudah diperbaiki
            }

            override fun onError(exception: ImageCaptureException) {
                onError("Failed to capture image: ${exception.message}")
            }
        }
    )
}


// A helper function to load a Bitmap from a URI
fun loadBitmapFromUri(context: android.content.Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
    val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return file
}

fun saveSnapToDatabase(snapDao: SnapDao, snap: Snap) {
    // Ensure this runs in a coroutine since Room DAO operates asynchronously
    CoroutineScope(Dispatchers.IO).launch {
        snapDao.insertSnap(snap)
    }
}
