package com.brunodegan.pokedex.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primaryContainer = Color(0xFFA09898),
    secondaryContainer = Color.Black,
    primary = Color.Red,
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Color.LightGray,
    tertiary = Color.White,
    onTertiary = Color.White,
    background = Color.Transparent,
    onSurface = Color(0xFFFF9800),
    surface = Color(0xFFF44336),
    onError = Color.Transparent
)

private val LightColorScheme = lightColorScheme(
    primaryContainer = Color(0xFFFF9800),
    secondaryContainer = Color.Black,
    primary = Color.Red,
    onPrimary = Color.White,
    secondary = Color.LightGray,
    onSecondary = Color.LightGray,
    tertiary = Color.Black,
    onTertiary = Color.White,
    background = Color.Transparent,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFFFF9800),
    surface = Color(0xFFF44336),
    onError = Color.Transparent
)


@Composable
fun PokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    val shapes = Shapes(
        extraSmall = MaterialTheme.shapes.extraSmall,
        small = MaterialTheme.shapes.small,
        medium = MaterialTheme.shapes.medium,
        large = MaterialTheme.shapes.large,
        extraLarge = MaterialTheme.shapes.extraLarge
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content
    )
}