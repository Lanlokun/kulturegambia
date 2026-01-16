// app/src/main/java/com/example/kulturgambia/ui/screens/EventDetailScreen.kt
package com.example.kulturgambia.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kulturgambia.data.model.Event
import com.example.kulturgambia.data.repository.EventsRepository
import com.example.kulturgambia.data.repository.PlacesRepository

private fun resolveDrawableId(context: Context, key: String): Int {
    if (key.isBlank()) return 0
    return context.resources.getIdentifier(key, "drawable", context.packageName)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    eventsRepo: EventsRepository,
    placesRepo: PlacesRepository,
    onBack: () -> Unit,
    onOpenMap: (lat: Double, lng: Double, name: String) -> Unit
) {
    val event = eventsRepo.getById(eventId)

    Scaffold(

    ) { inner ->
        if (event == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Event not found",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            return@Scaffold
        }

        val place = placesRepo.getById(event.placeId)

        EventDetailContent(
            event = event,
            placeName = place?.name.orEmpty(),
            placeAddress = place?.address.orEmpty(),
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            onOpenMap = {
                if (place != null) {
                    onOpenMap(place.lat, place.lng, place.name)
                }
            }
        )
    }
}

@Composable
private fun EventDetailContent(
    event: Event,
    placeName: String,
    placeAddress: String,
    modifier: Modifier,
    onOpenMap: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = remember(event.imageUrl) { resolveDrawableId(context, event.imageUrl) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        // --- HERO IMAGE ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                imageResId != 0 -> {
                    AsyncImage(
                        model = imageResId,
                        contentDescription = event.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                event.imageUrl.startsWith("http", true) ||
                        event.imageUrl.startsWith("content://", true) ||
                        event.imageUrl.startsWith("file://", true) -> {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = event.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                else -> {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }
        }

        // --- CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Dates
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (event.endDate.isBlank() || event.endDate == event.startDate)
                        event.startDate
                    else
                        "${event.startDate}  •  ${event.endDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Place (if available)
            if (placeName.isNotBlank() || placeAddress.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        if (placeName.isNotBlank()) {
                            Text(
                                text = placeName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (placeAddress.isNotBlank()) {
                            Text(
                                text = placeAddress,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Button(
                    onClick = onOpenMap,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Map, contentDescription = null)
                    Spacer(Modifier.width(10.dp))
                    Text("View venue on map")
                }
            } else {
                AssistChip(
                    onClick = {},
                    label = { Text("Venue not set") },
                    leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null) }
                )
            }

            Divider()

            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
