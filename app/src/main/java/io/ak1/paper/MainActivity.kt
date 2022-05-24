package io.ak1.paper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import io.ak1.paper.ui.component.RootComponent
import io.ak1.paper.ui.theme.randomInt
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        randomInt = Random(System.currentTimeMillis()).nextInt(0..17)
        setContent {
            RootComponent()
        }
    }
}