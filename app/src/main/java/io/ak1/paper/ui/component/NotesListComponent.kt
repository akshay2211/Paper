package io.ak1.paper.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.utils.gridTrim
import io.ak1.paper.ui.utils.timeAgo

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun NotesListComponent(
    includeHeader: Boolean = true,
    resultList: State<List<NoteWithDoodleAndImage>>,
    listState: LazyListState,
    searchCallback: () -> Unit, moreCallback: () -> Unit,
    callback: (Note) -> Unit,
) {
    val modifier = Modifier.padding(0.dp)
    LazyColumn(modifier = modifier, state = listState) {
        if (includeHeader) {
            item { HomeHeader(modifier, searchCallback, moreCallback) }
        }
        itemsIndexed(resultList.value) { index, element ->
            Card(
                modifier = modifier.fillMaxWidth().padding(14.dp,5.dp),
                shape = RoundedCornerShape(6.dp),
                onClick = { callback(element.note) }
            ) {
                Column(modifier = Modifier.padding(15.dp)) {

                    Text(
                        text = element.note.description.gridTrim(),
                        modifier = Modifier.fillMaxSize(),
                        style = MaterialTheme.typography.subtitle1
                    )

                    VerticalSpacer(7.dp)

                    Text(
                        text = element.note.updatedOn.timeAgo(),
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.caption
                    )

                }
            }

        }
    }
    if ((listState.firstVisibleItemScrollOffset > 338 && listState.firstVisibleItemIndex == 0) || listState.firstVisibleItemIndex > 0) {
        TopAppBar(
            title = {
                Text(text = "Paper")
            },
            actions = {
                PaperIconButton(id = R.drawable.ic_search, onClick = searchCallback)
                PaperIconButton(id = R.drawable.ic_more, onClick = moreCallback)
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = AppBarDefaults.TopAppBarElevation
        )
    }

}

@Composable
fun RowScope.Iconsbar(modifier: Modifier, searchCallback: () -> Unit, moreCallback: () -> Unit) {
    Spacer(
        modifier = Modifier
            .height(38.dp)
            .weight(1f, fill = true)
    )
    Image(
        painter = painterResource(id = R.drawable.ic_search),
        contentDescription = stringResource(
            id = R.string.image_desc
        ),
        modifier = modifier.clickable { searchCallback() },
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
    )
    Image(
        painter = painterResource(id = R.drawable.ic_more),
        contentDescription = stringResource(
            id = R.string.image_desc
        ),
        modifier = modifier.clickable { moreCallback() },
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeHeader(modifier: Modifier, searchCallback: () -> Unit, moreCallback: () -> Unit) {
    Box {
        Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h3,modifier = Modifier.padding(14.dp))
        Row(modifier = Modifier.padding(0.dp, 120.dp, 0.dp, 0.dp)) {
            TopAppBar(
                title = {

                },
                actions = {
                    PaperIconButton(id = R.drawable.ic_search, onClick = searchCallback)
                    PaperIconButton(id = R.drawable.ic_more, onClick = moreCallback)
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        }
        VerticalSpacer(16.dp)
    }
}

@Composable
fun PaperIconButton(
    @DrawableRes id: Int,
    tint: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painterResource(id = id),
            contentDescription = stringResource(id = R.string.image_desc),
            tint = tint
        )
    }
}