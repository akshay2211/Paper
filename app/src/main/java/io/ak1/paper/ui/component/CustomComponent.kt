package io.ak1.paper.ui.component

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.screens.home.DEFAULT
import io.ak1.paper.ui.screens.home.HomeUiState
import io.ak1.paper.ui.theme.PaperTheme
import io.ak1.paper.ui.utils.getUriList
import io.ak1.paper.ui.utils.gridTrim
import io.ak1.paper.ui.utils.timeAgo

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@Composable
fun NotesListComponent(
    headerColor: Color,
    homeUiState: HomeUiState,
    scrollState: LazyListState = rememberLazyListState(),
    padding: PaddingValues,
    callback: (NoteWithDoodleAndImage) -> Unit
) {
    val modifier = Modifier.padding(padding)
    Column {
        LazyColumn(modifier = modifier, state = scrollState) {
            itemsIndexed(homeUiState.notes) { index, element ->
                NoteView(element) {
                    callback(it)
                }
            }
        }
        if (homeUiState.isEmpty) {
            PlaceHolderBox(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(60.dp),
                colorFilter = ColorFilter.tint(headerColor)
            )

        }
    }
}

@Composable
fun PlaceHolderBox(modifier: Modifier, colorFilter: ColorFilter) {
    Box(modifier = modifier) {
        val fillMaxSizeModifier = Modifier.fillMaxSize()
        val placeholderDescription = stringResource(id = R.string.image_desc)
        Image(
            painter = painterResource(id = R.drawable.ic_not_found),
            contentDescription = placeholderDescription,
            alignment = Alignment.Center,
            modifier = fillMaxSizeModifier,
        )
        Image(
            painter = painterResource(id = R.drawable.ic_not_found_tint),
            contentDescription = placeholderDescription,
            alignment = Alignment.Center,
            modifier = fillMaxSizeModifier,
            colorFilter = colorFilter
        )
        Image(
            painter = painterResource(id = R.drawable.ic_not_found_three),
            contentDescription = placeholderDescription,
            alignment = Alignment.Center,
            modifier = fillMaxSizeModifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteView(element: NoteWithDoodleAndImage, callback: (NoteWithDoodleAndImage) -> Unit) {
    val hasDoodle = element.doodleList.isNotEmpty()
    val hasImages = element.imageList.isNotEmpty()
    val hasDescription = element.note.description.trim().isNotBlank()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        shape = RoundedCornerShape(10),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
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
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    VerticalSpacer(7.dp)
                    Text(
                        text = element.note.updatedOn.timeAgo(),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )

                }
            }
        }
    }
}

@Composable
fun ImageGridView(element: NoteWithDoodleAndImage) {
    val defaultHeight = 120.dp
    Box {
        val list = element.getUriList()
        Row {
            list.forEachIndexed { index, clickableUri ->
                if (index == 3) return@forEachIndexed
                Image(
                    painter = rememberAsyncImagePainter(model = clickableUri.uri),
                    contentDescription = null, // decorative
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(defaultHeight)
                        .fillMaxWidth()
                        .weight(1f, true)
                )

            }
        }
        if (element.note.description.trim().isEmpty()) Text(
            text = element.note.updatedOn.timeAgo(),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall,
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
fun Preview() {
    PaperTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                NoteView(
                    element = NoteWithDoodleAndImage(
                        Note(DEFAULT, "Hello this is sample text"), listOf(), listOf()
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
    border: Boolean = false,
    tint: Color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            painterResource(id = id),
            contentDescription = stringResource(id = R.string.image_desc),
            tint = tint,
            modifier = if (border) Modifier.border(
                0.5.dp, MaterialTheme.colorScheme.primary, CircleShape
            ) else Modifier
        )
    }
}

