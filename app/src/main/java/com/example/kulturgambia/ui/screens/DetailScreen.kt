package com.example.kulturgambia.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kulturgambia.data.repository.CultureRepository
import com.example.kulturgambia.data.repository.FavoritesRepository
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    id: String,
    cultureRepo: CultureRepository,
    favoritesRepo: FavoritesRepository,
    onBack: () -> Unit,
    onEdit: (String) -> Unit, // New: Navigate to an edit screen
    onDeleteSuccess: () -> Unit // New: Go back after successful deletion
) {
    val item = cultureRepo.getById(id) ?: return
    val scope = rememberCoroutineScope()
    var isSaved by remember { mutableStateOf(false) }

    // UI State for Menu and Dialogs
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        isSaved = favoritesRepo.isFavorite(id)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Story?") },
            text = { Text("This action cannot be undone. Are you sure you want to remove this cultural item?") },
            confirmButton = {
                TextButton(onClick = {
                    // Logic: cultureRepo.delete(id)
                    showDeleteDialog = false
                    onDeleteSuccess()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    scope.launch {
                        if (isSaved) favoritesRepo.remove(id) else favoritesRepo.add(id)
                        isSaved = !isSaved
                    }
                },
                containerColor = if (isSaved) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.primary,
                icon = { Icon(if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null) },
                text = { Text(if (isSaved) "Saved" else "Save") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // Header Image Box
            Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
                item.imageRes?.let { resId ->
                    Image(
                        painter = painterResource(resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Top Actions Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(Color.Black.copy(0.5f), Color.Transparent)))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row {
                        IconButton(onClick = { /* Implement Share Logic */ }) {
                            Icon(Icons.Default.Share, "Share", tint = Color.White)
                        }
                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, "More", tint = Color.White)
                            }
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                DropdownMenuItem(
                                    text = { Text("Edit Story") },
                                    leadingIcon = { Icon(Icons.Default.Edit, null) },
                                    onClick = {
                                        showMenu = false
                                        onEdit(id)
                                    }
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                DropdownMenuItem(
                                    text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                    leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
                                    onClick = {
                                        showMenu = false
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Content Panel
            Surface(
                modifier = Modifier.fillMaxWidth().offset(y = (-24).dp),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // Category & Date
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SuggestionChip(
                            onClick = { },
                            label = { Text(item.category.uppercase()) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "• 12 min read", // Dynamic if you calculate content length
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    // Summary Box
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Text(
                            text = item.summary,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp),
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Tags Section (Missing item usually found in detail pages)
                    if (item.tags.isNotEmpty()) {
                        Text("Tags", style = MaterialTheme.typography.titleSmall)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item.tags.forEach { tag ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text("#$tag") }
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    Text(
                        text = item.content,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 26.sp
                    )

                    Spacer(Modifier.height(120.dp))
                }
            }
        }
    }
}