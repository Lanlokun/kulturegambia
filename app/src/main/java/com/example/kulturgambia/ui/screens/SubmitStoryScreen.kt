package com.example.kulturgambia.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // Ensure you have the Coil library dependency
import kotlinx.coroutines.launch
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitStoryScreen(
    onPost: suspend (
        title: String,
        category: String,
        summary: String,
        content: String,
        imageUri: Uri
    ) -> Boolean
) {
    val categories = listOf("Music", "Dance", "Food", "Clothing", "Festivals", "History", "Language")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(categories.first()) }
    var summary by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var isSubmitting by rememberSaveable { mutableStateOf(false) }
    var triedSubmit by rememberSaveable { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

    val titleError = triedSubmit && title.isBlank()
    val summaryError = triedSubmit && summary.isBlank()
    val contentError = triedSubmit && content.isBlank()
    val imageError = triedSubmit && imageUri == null

    val canSubmit = title.isNotBlank() && summary.isNotBlank() && content.isNotBlank() && imageUri != null && !isSubmitting

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Share Culture", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (isSubmitting) return@ExtendedFloatingActionButton
                    triedSubmit = true
                    if (!canSubmit) {
                        scope.launch { snackbarHostState.showSnackbar("All fields are required.") }
                        return@ExtendedFloatingActionButton
                    }
                    scope.launch {
                        isSubmitting = true
                        val success = onPost(title.trim(), category, summary.trim(), content.trim(), imageUri!!)
                        isSubmitting = false
                        if (success) {
                            title = ""; summary = ""; content = ""; imageUri = null; triedSubmit = false
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = { Icon(Icons.Default.Send, null) },
                text = { Text(if (isSubmitting) "Sharing..." else "Share Story") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // --- SECTION: MEDIA ---
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Visual Content", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = if (imageError) 2.dp else 1.dp,
                                color = if (imageError) MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.2f)))
                            FilledTonalButton(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                                Icon(Icons.Default.Edit, "Edit")
                                Spacer(Modifier.width(8.dp))
                                Text("Change Photo")
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.AddPhotoAlternate, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                                Text("Upload a Cover Image", style = MaterialTheme.typography.bodyMedium)
                                if (imageError) Text("This field is mandatory", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                // --- SECTION: DETAILS ---
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Story Details", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

                    // Title
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        placeholder = { Text("Enter a descriptive title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = titleError,
                        singleLine = true
                    )

                    // Enhanced Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = showCategoryMenu,
                        onExpandedChange = { showCategoryMenu = it }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = showCategoryMenu,
                            onDismissRequest = { showCategoryMenu = false }
                        ) {
                            categories.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        category = item
                                        showCategoryMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Summary
                    OutlinedTextField(
                        value = summary,
                        onValueChange = { if (it.length <= 100) summary = it },
                        label = { Text("Summary") },
                        placeholder = { Text("A brief hook for your story...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = summaryError,
                        supportingText = {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                if (summaryError) Text("Required") else Spacer(Modifier.width(1.dp))
                                Text("${summary.length}/100")
                            }
                        }
                    )

                    // Full Story
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("The Story") },
                        placeholder = { Text("Share the traditions, the history, or the memories...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 8,
                        shape = RoundedCornerShape(12.dp),
                        isError = contentError
                    )
                }

                Spacer(Modifier.height(120.dp))
            }

            if (isSubmitting) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))
            }
        }
    }
}