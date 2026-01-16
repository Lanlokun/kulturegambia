package com.example.kulturgambia.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kulturgambia.data.model.Place
import com.example.kulturgambia.data.repository.PlacesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteDetailScreen(
    placeId: String,
    placesRepo: PlacesRepository,
    onBack: () -> Unit,
    onOpenMap: (lat: Double, lng: Double, name: String) -> Unit
) {
    val place = placesRepo.getById(placeId)

    if (place == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Site not found")
        }
        return
    }

    Scaffold(
        // Floating action button for primary action (Map)
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onOpenMap(place.lat, place.lng, place.name) },
                icon = { Icon(Icons.Default.Map, null) },
                text = { Text("View on Map") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { inner ->
        Box(modifier = Modifier.fillMaxSize().padding(inner)) {
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. HERO IMAGE SECTION
                Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    val context = LocalContext.current
                    val imageResId = remember(place.imageUrl) {
                        resolvePlaceDrawableId(context, place.imageUrl)
                    }

                    if (imageResId != 0 || place.imageUrl.startsWith("http")) {
                        AsyncImage(
                            model = if (imageResId != 0) imageResId else place.imageUrl,
                            contentDescription = place.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Place, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                        }
                    }


                }

                // 2. INFORMATION CONTENT
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Title and Type
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = place.type.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Quick Info Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoItem(Icons.Default.LocationOn, "Location", place.address.take(15) + "...")
                        VerticalDivider(modifier = Modifier.height(32.dp))
                        InfoItem(Icons.Default.Explore, "Coordinates", "${place.lat.toInt()}, ${place.lng.toInt()}")
                    }

                    // About Section
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("About the Site", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = place.description,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Location Details Card
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Full Address", style = MaterialTheme.typography.labelLarge)
                            Text(place.address, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            Text("GPS Details", style = MaterialTheme.typography.labelLarge)
                            Text("Lat: ${place.lat}, Lng: ${place.lng}", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(Modifier.height(80.dp)) // Extra space for FAB
                }
            }
        }
    }
}

@Composable
private fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

private fun resolvePlaceDrawableId(context: Context, key: String): Int {
    if (key.isBlank()) return 0
    return context.resources.getIdentifier(key, "drawable", context.packageName)
}