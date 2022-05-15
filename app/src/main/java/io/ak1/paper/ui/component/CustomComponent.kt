package io.ak1.paper.ui.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.models.getBitmapList
import io.ak1.paper.ui.screens.home.DEFAULT
import io.ak1.paper.ui.theme.PaperTheme
import io.ak1.paper.ui.utils.gridTrim
import io.ak1.paper.ui.utils.limitWidthInWideScreen
import io.ak1.paper.ui.utils.timeAgo

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@Composable
fun NotesListComponent(
    includeHeader: Boolean = true,
    resultList: List<NoteWithDoodleAndImage>,
    scrollState: LazyListState = rememberLazyListState(),
    padding: PaddingValues,
    callback: (NoteWithDoodleAndImage) -> Unit,
) {
    val modifier = Modifier.padding(padding).padding(12.dp, 0.dp)
    LazyColumn(modifier = modifier.limitWidthInWideScreen(), state = scrollState) {
        itemsIndexed(resultList) { index, element ->
            NoteView(element) {
                callback(it)
            }
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteView(element: NoteWithDoodleAndImage, callback: (NoteWithDoodleAndImage) -> Unit) {
    val hasDoodle = element.doodleList.isNotEmpty()
    val hasImages = element.imageList.isNotEmpty()
    val hasDescription = element.note.description.trim().isNotBlank()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp),
        onClick = { callback(element) },
    ) {
        Column {
            if (hasDoodle || hasImages) {
                ImageGridView(element)
            }
            if (hasDescription) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = element.note.description.trim().gridTrim(),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.h6,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    VerticalSpacer(7.dp)
                    Text(
                        text = element.note.updatedOn.timeAgo(),
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.caption
                    )

                }
            }
        }
    }
}

@Composable
fun ImageGridView(element: NoteWithDoodleAndImage) {
    Box {
        val bitmapList = element.getBitmapList()
        val totalSize = bitmapList.size
        if (totalSize == 1) {
            bitmapList[0]?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                )
            }
        } else if (totalSize == 2 || totalSize == 3) {
            Row {
                bitmapList.forEach {
                    it?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null, // decorative
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                                .weight(1f, true)
                        )
                    }
                }
            }
        } else if (totalSize >= 4) {
            Row {
                Column(Modifier.weight(1f, true)) {
                    bitmapList[0]?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null, // decorative
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                        )
                    }
                    bitmapList[2]?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null, // decorative
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                Column(Modifier.weight(1f, true)) {
                    bitmapList[1]?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null, // decorative
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                        )
                    }
                    bitmapList[3]?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null, // decorative
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
        if (element.note.description.trim().isEmpty())
            Text(
                text = element.note.updatedOn.timeAgo(),
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            )

    }
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
