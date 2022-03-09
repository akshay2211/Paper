package io.ak1.paper.ui.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.screens.home.DEFAULT
import io.ak1.paper.ui.theme.PaperTheme
import io.ak1.paper.ui.utils.gridTrim
import io.ak1.paper.ui.utils.timeAgo

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@Composable
fun NotesListComponent(
    includeHeader: Boolean = true,
    resultList: State<List<NoteWithDoodleAndImage>>,
    scrollState: LazyListState = rememberLazyListState(),
    callback: (Note) -> Unit,
) {
    val modifier = Modifier
        .padding(0.dp)
    LazyColumn(modifier = modifier, state = scrollState) {
        if (includeHeader) {
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
            }
        }

        itemsIndexed(resultList.value) { index, element ->
            NoteView(element) {
                callback(it)
            }
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteView(element: NoteWithDoodleAndImage, callback: (Note) -> Unit) {
    /* val hasDoodle = element.doodleList.isNotEmpty()
     val hasImages = element.imageList.isNotEmpty()
     if (hasDoodle || hasImages) {
         val configuration = LocalConfiguration.current
         val totalSize = element.doodleList.size + element.imageList.size
         val screenHeight = configuration.screenHeightDp.dp
         val screenWidth = configuration.screenWidthDp.dp
         val shape = RoundedCornerShape(6.dp)
         val padding = 5.dp
         val height = 200.dp
         val modifier = Modifier
             .padding(padding)
             .height(height)
         Card(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(14.dp, 5.dp),
             shape = shape,
             onClick = { callback(element.note) },
             elevation = 0.dp
         ) {
             Column(
                 Modifier
                     .padding(0.dp)
             ) {
                 if (totalSize == 1) {
                     if (hasDoodle) {
                         val doodle = element.doodleList[0]
                         doodle.base64Text.convert()?.let {
                             Image(
                                 bitmap = it.asImageBitmap(),
                                 contentDescription = "hi",
                                 modifier = modifier
                                     .fillMaxWidth()
                                     .clip(shape),
                                 contentScale = ContentScale.Crop
                             )
                         }
                     } else {
                         val image = element.imageList[0]
                         image.imageText.convert()?.let {
                             Image(
                                 bitmap = it.asImageBitmap(),
                                 contentDescription = "hi",
                                 modifier = modifier.fillMaxWidth(),
                                 contentScale = ContentScale.Crop
                             )
                         }
                     }
                 } else if (totalSize == 2 || totalSize == 3) {
                     val width = (screenWidth / totalSize) - (padding * 2)
                     Log.e("size if ", "$screenWidth totalSize is $totalSize =>  $width")
                     Row {
                         for (image in element.imageList) {
                             image.imageText.convert()?.let {
                                 Image(
                                     bitmap = it.asImageBitmap(),
                                     contentDescription = "hi",
                                     modifier = modifier
                                         .width(width)
                                         .height(width),
                                     contentScale = ContentScale.Crop
                                 )
                             }
                         }
                         for (doodle in element.doodleList) {
                             doodle.base64Text.convert()?.let {
                                 Image(
                                     bitmap = it.asImageBitmap(),
                                     contentDescription = "hi",
                                     modifier = modifier
                                         .width(width)
                                         .height(width),
                                     contentScale = ContentScale.Crop
                                 )
                             }
                         }
                     }
                 } else {
                     LazyRow {
                         items(element.imageList) { image ->
                             image.imageText.convert()?.let {
                                 Image(
                                     bitmap = it.asImageBitmap(),
                                     contentDescription = "hi",
                                     modifier = modifier.width(100.dp)
                                 )
                             }
                         }
                         items(element.doodleList) { doodle ->
                             doodle.base64Text.convert()?.let {
                                 Image(
                                     bitmap = it.asImageBitmap(),
                                     contentDescription = "hi",
                                     modifier = modifier.width(100.dp)
                                 )
                             }
                         }
                     }
                 }
                 if (element.note.description.trim().isNotBlank()) {
                     Text(
                         text = element.note.description.trim().gridTrim(),
                         modifier = Modifier
                             .fillMaxWidth()
                             .padding(15.dp),
                         style = MaterialTheme.typography.h5,
                         maxLines = 2
                     )
                     VerticalSpacer(7.dp)
                 }
                 Text(
                     text = element.note.updatedOn.timeAgo(),
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(15.dp, 0.dp),
                     color = MaterialTheme.colors.primaryVariant,
                     style = MaterialTheme.typography.caption
                 )
             }
         }
     } else {*/
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp, 5.dp),
        onClick = { callback(element.note) },
    ) {

        Column(modifier = Modifier.padding(15.dp)) {

            Text(
                text = element.note.description.trim().gridTrim(),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
            VerticalSpacer(7.dp)
            Text(
                text = element.note.updatedOn.timeAgo(),
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.caption
            )

        }
    }
    // }
}

@Preview("Home screen")
@Preview("Home screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Home screen (big font)", fontScale = 1.5f)
@Preview("Home screen (large screen)", device = Devices.PIXEL_C)
@Composable
fun preview() {
    PaperTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column {
                NoteView(
                    element = NoteWithDoodleAndImage(
                        Note(DEFAULT, "Hello this is sample text"), listOf(),
                        listOf()
                    )
                ) {}
            }
        }
    }
}

@Composable
fun PaperIconButton(
    @DrawableRes id: Int,
    enabled: Boolean = true,
    tint: Color = if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            painterResource(id = id),
            contentDescription = stringResource(id = R.string.image_desc),
            tint = tint
        )
    }
}

@Composable
fun ColorRow(
    isVisible: Boolean,
    rowElementsCount: Int = 8,
    colors: List<Color>,
    backgroundColor: Color = MaterialTheme.colors.background,
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
