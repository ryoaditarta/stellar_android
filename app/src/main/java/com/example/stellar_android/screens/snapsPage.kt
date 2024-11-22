import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stellar_android.components.SnapRepository
import com.example.stellar_android.components.Snap
import java.io.File
import android.graphics.BitmapFactory
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import com.example.stellar_android.components.BottomNavBar
import kotlinx.coroutines.launch

@Composable
fun snapsPage(
    navController: NavController,
    snaps: MutableState<List<Snap>> // Menerima daftar snaps yang diteruskan
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "snapsPage")
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding())
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header Text
                    item {
                        Text(
                            text = "Snaps",
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Photos that represent your day",
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                    }

                    // Check if still loading or if snaps are available
                    if (snaps.value.isEmpty()) {
                        item {
                            Text(text = "Loading...", color = Color.White)
                        }
                    } else {
                        // Display all images stored in snaps
                        itemsIndexed(snaps.value) {index,snap -> // Use 'items' for a list of snaps
                            val file = File(snap.filePath)
                            val bitmap = if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null

                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Snap Image",
                                    modifier = Modifier
                                        .size(200.dp)
                                        .padding(16.dp)
                                        .background(Color.Gray, RoundedCornerShape(8.dp))
                                )
                            } ?: run {

                            }
                        }
                    }
                }
            }
        }
    )
}

