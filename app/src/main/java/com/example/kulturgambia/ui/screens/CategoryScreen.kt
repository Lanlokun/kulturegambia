package com.example.kulturgambia.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kulturgambia.data.repository.CultureRepository
import com.example.kulturgambia.ui.components.KGSectionHeader
import com.example.kulturgambia.ui.components.KGCard
import com.example.kulturgambia.ui.components.KGCategoryChip
import com.example.kulturgambia.ui.components.KGImageHeader
import com.example.kulturgambia.ui.theme.UiTokens

@Composable
fun CategoryScreen(
    category: String,
    repo: CultureRepository,
    onOpenItem: (String) -> Unit
) {
    val items = repo.getByCategory(category)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            KGSectionHeader(
                title = category,
                subtitle = "Explore Gambian $category",
                modifier = Modifier.padding(horizontal = UiTokens.ScreenPadding, vertical = 14.dp)
            )
        }

        items(items) { item ->
            KGCard(
                modifier = Modifier
                    .padding(horizontal = UiTokens.ScreenPadding, vertical = 8.dp)
                    .fillMaxSize(),
                onClick = { onOpenItem(item.id) }
            ) {
                KGImageHeader(
                    resId = item.imageRes,
                    contentDescription = item.title
                )
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        item.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 8.dp))
                    KGCategoryChip(text = item.category)
                }
            }
        }
    }
}
