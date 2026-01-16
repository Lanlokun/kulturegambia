package com.example.kulturgambia.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kulturgambia.ui.theme.UiTokens

@Composable
fun KGCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(UiTokens.CardRadius)
    val cardModifier = if (onClick != null) modifier.clickable { onClick() } else modifier

    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = UiTokens.CardBorderAlpha)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = UiTokens.CardElevation)
    ) {
        Column(content = content)
    }
}

@Composable
fun KGImageHeader(
    resId: Int?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    height: Dp = UiTokens.ImageHeightGrid
) {
    if (resId != null) {
        Image(
            painter = painterResource(resId),
            contentDescription = contentDescription,
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            contentScale = ContentScale.Crop
        )
    } else {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(height),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {}
    }
}

@Composable
fun KGCategoryChip(
    text: String,
    onClick: () -> Unit = {}
) {
    AssistChip(
        onClick = onClick,
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}

@Composable
fun KGFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        label = { Text(text) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
            selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = enabled,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
            selectedBorderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            disabledSelectedBorderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f)
        )
    )
}

@Composable
fun KGSectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun KGSubmitCtaCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(UiTokens.CardRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            modifier = Modifier.padding(UiTokens.ScreenPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun KGCarouselTitleOverlay(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.82f),
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(title, maxLines = 1, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
