package com.app.pesawise.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PesaWiseColorScheme = darkColorScheme(

    primary = BinanceYellow,
    onPrimary = AppBackground,

    secondary = SecondaryText,
    onSecondary = AppBackground,

    tertiary = SuccessGreen,
    onTertiary = AppBackground,

    background = AppBackground,
    onBackground = PrimaryText,

    surface = AppSurface,
    onSurface = PrimaryText,

    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = SecondaryText,

    error = ErrorRed,
    onError = White,

    outline = BorderColor
)

@Composable
fun PesaWiseTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PesaWiseColorScheme,
        typography = Typography,
        content = content
    )
}