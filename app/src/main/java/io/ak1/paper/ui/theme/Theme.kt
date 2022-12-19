package io.ak1.paper.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import io.ak1.paper.data.local.dataStore
import io.ak1.paper.data.local.isDarkThemeOn
import io.ak1.paper.data.local.themePreferenceKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


private val DarkColorScheme = darkColorScheme(
    primary = WhiteDark,
    secondary = Accent,
    tertiary = Grey,
    background = BlackDark,
    surface = BlackDark,
    onPrimary = BlackDark,
    onBackground = WhiteDark
)

private val LightColorScheme = lightColorScheme(
    primary = BlackDark,
    secondary = Accent,
    tertiary = Grey,
    background = WhiteDark,
    surface = WhiteDark,
    onPrimary = WhiteDark,
    onBackground = WhiteDark

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
@Composable
fun PaperTheme(darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
    ) {
        val colorScheme =
            when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
                }
                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }
        val view = LocalView.current
        if (!view.isInEditMode) {
            val currentWindow = (view.context as? Activity)?.window
                ?: throw Exception("Not in an activity - unable to get Window reference")

            SideEffect {
                currentWindow.statusBarColor = Color.Transparent.value.toInt()
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
                    !darkTheme
            }
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

@Composable
fun isSystemInDarkThemeCustom(): Boolean {
    val context = LocalContext.current
    val exampleData = runBlocking { context.dataStore.data.first() }
    val theme =
        context.isDarkThemeOn().collectAsState(initial = exampleData[themePreferenceKey] ?: 0)
    return when (theme.value) {
        2 -> true
        1 -> false
        else -> context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}
