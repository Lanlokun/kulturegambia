package com.example.kulturgambia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kulturgambia.R
import com.example.kulturgambia.data.model.Event
import com.example.kulturgambia.data.repository.EventsRepository
import com.example.kulturgambia.data.repository.PlacesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    eventRepo: EventsRepository,
    placeRepo: PlacesRepository,
    onOpenEvent: (String) -> Unit
) {

    val events = eventRepo.getUpcoming()

    // Simple in-memory RSVP state (per app run)
    val rsvpState = remember { mutableStateMapOf<String, Boolean>() }

    // Warm palette (local to this screen, no theme changes needed)
    val topBarColor = MaterialTheme.colorScheme.tertiaryContainer
    val screenBg = MaterialTheme.colorScheme.surface
    val cardColor = MaterialTheme.colorScheme.surfaceVariant

    Scaffold(
        containerColor = screenBg,

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(events) { event ->
                val place = placeRepo.getById(event.placeId)
                val locationName = place?.name ?: "Unknown Location"

                val isRsvped = rsvpState[event.id] ?: false

                EventCard(
                    event = event,
                    locationName = locationName,
                    cardContainerColor = cardColor,
                    isRsvped = isRsvped,
                    onToggleRsvp = { rsvpState[event.id] = !isRsvped },
                    onOpen = { onOpenEvent(event.id) }

                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    locationName: String,
    cardContainerColor: androidx.compose.ui.graphics.Color,
    isRsvped: Boolean,
    onToggleRsvp: () -> Unit,
    onOpen: () -> Unit

) {
    val context = LocalContext.current

    // imageUrl is a drawable NAME like "roots_festival"
    val imageResId = remember(event.imageUrl) {
        if (event.imageUrl.isBlank()) 0
        else context.resources.getIdentifier(event.imageUrl, "drawable", context.packageName)
    }
    val finalResId = if (imageResId != 0) imageResId else R.drawable.women

    // Dialog for RSVP confirmation (nice UX)
    var showDialog by remember { mutableStateOf(false) }

    val accent = MaterialTheme.colorScheme.tertiary
    val accentOn = MaterialTheme.colorScheme.onTertiary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() },
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = finalResId),
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${event.startDate} - ${event.endDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(14.dp))

                // RSVP row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isRsvped) Icons.Default.EventAvailable else Icons.Default.People,
                            contentDescription = null,
                            tint = if (isRsvped) accent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isRsvped) "You are going" else "RSVP to attend",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isRsvped) accent else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (isRsvped) {
                        OutlinedButton(
                            onClick = { showDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel")
                        }
                    } else {
                        Button(
                            onClick = { showDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent,
                                contentColor = accentOn
                            )
                        ) {
                            Text("RSVP")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(if (isRsvped) "Cancel RSVP?" else "Confirm RSVP")
            },
            text = {
                Text(
                    if (isRsvped) "You will be removed from the attendee list for this event."
                    else "You will be marked as attending this event."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onToggleRsvp()
                        showDialog = false
                    }
                ) {
                    Text(if (isRsvped) "Yes, cancel" else "Yes, RSVP")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Not now")
                }
            }
        )
    }
}
