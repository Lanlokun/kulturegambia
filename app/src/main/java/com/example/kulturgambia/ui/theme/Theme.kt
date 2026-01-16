package com.example.kulturgambia.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme


// Gambia flag inspired colors
private val GambiaRed = Color(0xFFCE1126)
private val GambiaBlue = Color(0xFF0C1C8C)
private val GambiaGreen = Color(0xFF3A7728)
private val GambiaWhite = Color(0xFFFFFFFF)
private val DarkSurface = Color(0xFF0F1115)

private val LightColorScheme = lightColorScheme(
    primary = GambiaBlue,
    onPrimary = GambiaWhite,

    secondary = GambiaRed,
    onSecondary = GambiaWhite,

    tertiary = GambiaGreen,
    onTertiary = GambiaWhite,

    background = Color(0xFFF7F7FB),
    onBackground = Color(0xFF121212),

    surface = GambiaWhite,
    onSurface = Color(0xFF121212),

    surfaceVariant = Color(0xFFEDEDF5),
    onSurfaceVariant = Color(0xFF2A2A2A),

    outline = Color(0xFFB9B9C8)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8AA0FF),      // lighter blue for dark mode
    onPrimary = Color(0xFF0B1020),

    secondary = Color(0xFFFF7A86),    // lighter red
    onSecondary = Color(0xFF2A0B10),

    tertiary = Color(0xFF7FD06A),     // lighter green
    onTertiary = Color(0xFF0A1A0A),

    background = Color(0xFF0B0D12),
    onBackground = Color(0xFFECECF2),

    surface = DarkSurface,
    onSurface = Color(0xFFECECF2),

    surfaceVariant = Color(0xFF1A1D25),
    onSurfaceVariant = Color(0xFFC9CAD6),

    outline = Color(0xFF3A3F4D)
)

@Composable
fun KulturGambiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
