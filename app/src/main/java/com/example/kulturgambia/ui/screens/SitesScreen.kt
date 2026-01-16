    // app/src/main/java/com/example/kulturgambia/ui/screens/SitesScreen.kt
    package com.example.kulturgambia.ui.screens

    import android.content.Context
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.LocationOn
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
    import androidx.compose.ui.unit.sp
    import coil.compose.AsyncImage
    import com.example.kulturgambia.data.model.Place
    import com.example.kulturgambia.data.repository.PlacesRepository
    import androidx.compose.runtime.saveable.rememberSaveable
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.setValue
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material.icons.filled.SearchOff
    import androidx.compose.material.icons.filled.Close


    private fun resolveDrawableId(context: Context, key: String): Int {
        if (key.isBlank()) return 0
        // Matches res/drawable/<key>.(png|jpg|webp|xml)
        return context.resources.getIdentifier(key, "drawable", context.packageName)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SitesScreen(
        placesRepo: PlacesRepository,
        onOpenPlace: (String) -> Unit
    ) {
        val allPlaces = remember { placesRepo.getAll() }
        var searchQuery by rememberSaveable { mutableStateOf("") }
        var active by rememberSaveable { mutableStateOf(false) }

        // Filter logic
        val filteredPlaces = remember(searchQuery) {
            if (searchQuery.isBlank()) {
                allPlaces
            } else {
                allPlaces.filter {
                    it.name.contains(searchQuery, ignoreCase = true) ||
                            it.type.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        val cardShape = RoundedCornerShape(20.dp)

        Scaffold(
            topBar = {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {


                    // --- INTEGRATED SEARCH BAR ---
                    DockedSearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { active = false },
                        active = active,
                        onActiveChange = { active = it },
                        placeholder = { Text("Search landmarks or cities...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                }
                            }
                        }
                    ) {
                        // Quick suggestions could go here
                    }
                }
            }
        ) { padding ->
            if (filteredPlaces.isEmpty()) {
                // Reusing your empty state but tailored for search
                SearchEmptyState(query = searchQuery)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredPlaces, key = { it.id }) { place ->
                        SiteListItem(
                            place = place,
                            shape = cardShape,
                            onClick = { onOpenPlace(place.id) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchEmptyState(query: String) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.SearchOff, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                Text("No results for \"$query\"", style = MaterialTheme.typography.titleMedium)
                Text("Try a different keyword", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    @Composable
    private fun SiteListItem(
        place: Place,
        shape: RoundedCornerShape,
        onClick: () -> Unit
    ) {
        val context = LocalContext.current
        val imageResId = remember(place.imageUrl) { resolveDrawableId(context, place.imageUrl) }

        // Using OutlinedCard for a cleaner, flatter Material 3 look
        OutlinedCard(
            onClick = onClick,
            shape = shape,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            )
        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min) // Aligns image height with text height
                    .fillMaxWidth()
            ) {
                // 1. IMAGE BOX
                Box(modifier = Modifier.weight(0.4f).fillMaxHeight()) {
                    AsyncImage(
                        model = if (imageResId != 0) imageResId else place.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Floating category tag on top of image
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(bottomEnd = 12.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = place.type,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // 2. CONTENT COLUMN
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = place.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = place.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
    @Composable
    private fun SiteCard(
        place: Place,
        shape: RoundedCornerShape,
        onClick: () -> Unit
    ) {
        val context = LocalContext.current
        val imageResId = remember(place.imageUrl) { resolveDrawableId(context, place.imageUrl) }

        ElevatedCard(
            shape = shape,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Column {

                when {
                    // If it's a drawable key like "museum", "albert", etc.
                    imageResId != 0 -> {
                        AsyncImage(
                            model = imageResId,
                            contentDescription = place.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // If later you switch to real URLs, this will still work
                    place.imageUrl.startsWith("http", ignoreCase = true) ||
                            place.imageUrl.startsWith("content://", ignoreCase = true) ||
                            place.imageUrl.startsWith("file://", ignoreCase = true) -> {
                        AsyncImage(
                            model = place.imageUrl,
                            contentDescription = place.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = place.address,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = place.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onClick) { Text("Open") }
                    }
                }
            }
        }
    }

    @Composable
    private fun EmptySitesState() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "No sites available",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Places will appear here once added.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
