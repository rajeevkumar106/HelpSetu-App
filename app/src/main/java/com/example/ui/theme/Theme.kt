package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = HelpSetuGreen,
    onPrimary = Color.White,
    primaryContainer = HelpSetuGreenDark,
    onPrimaryContainer = HelpSetuGreenLight,
    secondary = HelpSetuAmber,
    onSecondary = Color.Black,
    secondaryContainer = HelpSetuAmberDark,
    onSecondaryContainer = HelpSetuAmberLight,
    background = Color(0xFF111827),
    surface = Color(0xFF1F2937),
    onBackground = Color(0xFFF9FAFB),
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = Color(0xFF374151),
    onSurfaceVariant = Color(0xFFD1D5DB)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = HelpSetuGreen,
    onPrimary = Color.White,
    primaryContainer = HelpSetuGreenLight,
    onPrimaryContainer = HelpSetuGreenDark,
    secondary = HelpSetuAmber,
    onSecondary = Color.White,
    secondaryContainer = HelpSetuAmberLight,
    onSecondaryContainer = HelpSetuAmberDark,
    background = HelpSetuBackground,
    surface = HelpSetuSurface,
    onBackground = HelpSetuTextPrimary,
    onSurface = HelpSetuTextPrimary,
    surfaceVariant = HelpSetuSurfaceVariant,
    onSurfaceVariant = HelpSetuTextSecondary
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun HelpSetuTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  MyApplicationTheme(darkTheme = darkTheme, dynamicColor = false, content = content)
}
