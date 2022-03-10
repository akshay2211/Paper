package io.ak1.paper.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ak1.paper.R

/**
 * Created by akshay on 10/03/22
 * https://ak1.io
 */

/**
 * Enum to manage state in [CollapsibleTopBar]
 */
enum class CollapsibleTopBarState {
    Collapsed,
    Expanded
}

/**
 * Displays the Collapsible TopBar in HomeScreen.
 *
 * @param collapsibleTopBarState state to manage animation
 * @param actions all actions needed for TopBar
 */
@Composable
fun CollapsibleTopBar(
    collapsibleTopBarState: CollapsibleTopBarState,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val transition = updateTransition(collapsibleTopBarState, label = "CollapsibleTopBarTransition")

    val topBarHeight = transition.animateDp(label = "CollapsibleTopBarHeight") { state ->
        if (CollapsibleTopBarState.Expanded == state) 200.dp else 48.dp
    }
    val topBarElevation = transition.animateDp(label = "CollapsibleTopBarElevation") { state ->
        if (CollapsibleTopBarState.Expanded == state) 0.dp else AppBarDefaults.TopAppBarElevation
    }
    val headerTextSize = transition.animateInt(label = "CollapsibleTopBarTitleSize") { state ->
        if (CollapsibleTopBarState.Expanded == state) 48 else 20
    }
    val topBarColor = transition.animateColor(label = "CollapsibleTopBarColor") { state ->
        if (CollapsibleTopBarState.Expanded == state) MaterialTheme.colors.background else MaterialTheme.colors.surface
    }

    Surface(
        color = topBarColor.value,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = topBarElevation.value,
        shape = RectangleShape,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Box(modifier = Modifier.statusBarsPadding()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(AppBarDefaults.ContentPadding)
                        .height(topBarHeight.value),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom,
                    content = {
                        Spacer(Modifier.width(12.dp))

                        Row(
                            Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            verticalAlignment = Alignment.Top
                        ) {
                            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                                CompositionLocalProvider(
                                    LocalContentAlpha provides ContentAlpha.high,
                                    content = {
                                        Text(
                                            text = stringResource(id = R.string.app_name),
                                            style = MaterialTheme.typography.h3,
                                            fontSize = headerTextSize.value.sp,
                                            modifier = Modifier
                                                .padding(12.dp)

                                        )
                                    }
                                )
                            }
                        }

                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.Bottom,
                                content = actions
                            )
                        }
                    }
                )
            }
        }
    }
}