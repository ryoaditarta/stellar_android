import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stellar_android.components.Snap
import com.example.stellar_android.components.BottomNavBar
import com.example.stellar_android.components.Typography
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import java.io.File

@Composable
fun snapsPage(
    navController: NavController,
    snaps: MutableState<List<Snap>>
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
                contentAlignment = Alignment.TopCenter
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .padding(16.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Snaps.",
                                color = Color.White,
                                style = Typography.bodyLarge
                            )
                            Text(
                                text = "Photos that represent your day",
                                color = Color.White,
                                style = Typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    // Grid Section
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3), // Tiga kolom untuk grid
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        if (snaps.value.isEmpty()) {
                            // Tampilkan loading jika snaps kosong
                            item {
                                Text(
                                    text = "Loading...",
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            // Tampilkan gambar dalam grid
                            items(snaps.value) { snap ->
                                val file = File(snap.filePath)
                                val bitmap = if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null

                                bitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Snap Image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.Gray, RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
