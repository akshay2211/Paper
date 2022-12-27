package io.ak1.paper.ui.component

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R

/**
 * Created by akshay on 30/11/21
 * https://ak1.io
 */

@Composable
fun CustomAlertDialog(
    @StringRes titleId: Int = R.string.image_desc,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    callback: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) }, title = {
                Text(
                    text = stringResource(
                        id = titleId
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        setShowDialog(false)
                        callback()
                    },
                    modifier = Modifier.padding(8.dp, 0.dp)
                ) {
                    Text(
                        text = stringResource(
                            id = android.R.string.ok
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { setShowDialog(false) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = stringResource(
                            id = android.R.string.cancel
                        ),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        )
    }
}

@Composable
fun ColorRow(
    isVisible: Boolean,
    rowElementsCount: Int = 8,
    colors: List<Color>,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    clickedColor: (Color) -> Unit
) {
    val density = LocalDensity.current
    val defaultColor = remember {
        mutableStateOf(colors[0])
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {

        var columnsSize: Int = colors.size / rowElementsCount
        val remaining = colors.size % rowElementsCount
        if (remaining > 0) {
            columnsSize++
        }
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp, 8.dp, 16.dp, 16.dp)
        ) {
            repeat(columnsSize) { column ->
                println()
                Row {
                    repeat(rowElementsCount) { row ->
                        val pos = (column * rowElementsCount) + row
                        var size = 22.dp
                        if (pos < colors.size) {
                            val color = colors[pos]
                            if (defaultColor.value == color) {
                                size = 36.dp
                            }

                            IconButton(
                                onClick = {
                                    defaultColor.value = color
                                    clickedColor(color)

                                }, modifier = Modifier
                                    .weight(1f, true)

                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_color),
                                    contentDescription = stringResource(id = R.string.image_desc),
                                    tint = color,
                                    modifier = Modifier.size(size)
                                    //.animateContentSize(animationSpec = tween(1000,100,LinearOutSlowInEasing))
                                )
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f, true)
                            )
                        }

                    }
                }
            }
        }
    }
}
