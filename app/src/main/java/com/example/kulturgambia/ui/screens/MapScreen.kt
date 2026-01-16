package com.example.kulturgambia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kulturgambia.R
import com.example.kulturgambia.data.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(repo: PlacesRepository) {
    val places = repo.getAll()
    val context = LocalContext.current

    // Center roughly around The Gambia
    val gambiaCenter = LatLng(13.4432, -15.3101)
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(gambiaCenter, 7f)
    }

    var selectedPlaceId by remember { mutableStateOf<String?>(null) }
    val selectedPlace = remember(selectedPlaceId, places) {
        places.firstOrNull { it.id == selectedPlaceId }
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = true
            )
        ) {
            places.forEach { p ->
                Marker(
                    state = MarkerState(position = LatLng(p.lat, p.lng)),
                    title = p.name,
                    snippet = p.type,
                    onClick = {
                        selectedPlaceId = p.id
                        // return true: we handle the click (and open our card)
                        true
                    }
                )
            }
        }

        // Warm floating info card (bottom overlay)
        if (selectedPlace != null) {
            val imageResId = remember(selectedPlace.imageUrl) {
                if (selectedPlace.imageUrl.isBlank()) 0
                else context.resources.getIdentifier(
                    selectedPlace.imageUrl,
                    "drawable",
                    context.packageName
                )
            }
            val finalResId = if (imageResId != 0) imageResId else R.drawable.women // fallback

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = finalResId),
                        contentDescription = selectedPlace.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(Modifier.padding(14.dp)) {
                        Text(
                            text = selectedPlace.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = selectedPlace.address,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(Modifier.height(6.dp))

                        AssistChip(
                            onClick = {},
                            label = { Text(selectedPlace.type) }
                        )

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { selectedPlaceId = null }) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}
