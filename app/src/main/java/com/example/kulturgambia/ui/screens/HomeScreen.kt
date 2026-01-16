package com.example.kulturgambia.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.kulturgambia.data.model.CultureItem
import com.example.kulturgambia.ui.theme.UiTokens

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    items: List<CultureItem>,
    searchQuery: String,
    onItemClick: (CultureItem) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Music", "Dance", "Food", "Clothing", "Festivals")
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSubmitClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Share Story") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        text = "Discover Gambia",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold)
                    )
                    Text(
                        text = "Stories, traditions, and culture",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Featured Section (Carousel)
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items.take(5)) { item ->
                        FeaturedItemCard(item, onItemClick)
                    }
                }
            }

            // Category Filter (Sticky)
            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                    tonalElevation = 2.dp
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category) }
                            )
                        }
                    }
                }
            }

            // Explore Grid
            item {
                val filtered = if (selectedCategory == "All") items
                else items.filter { it.category == selectedCategory }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .heightIn(max = 2000.dp), // Height required for nested scrolling
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                    userScrollEnabled = false
                ) {
                    items(filtered) { item ->
                        CultureDiscoveryCard(item, onItemClick)
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedItemCard(item: CultureItem, onClick: (CultureItem) -> Unit) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick(item) }
    ) {
        AsyncImage(
            model = item.imageRes, // Using imageRes as per your model
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Text(
            text = item.title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CultureDiscoveryCard(item: CultureItem, onClick: (CultureItem) -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column {
            AsyncImage(
                model = item.imageRes,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(item.title, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}