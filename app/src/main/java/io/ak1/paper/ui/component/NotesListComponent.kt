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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.Note
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
    resultList: State<List<Note>>,
    listState: LazyListState,
    searchCallback: () -> Unit, moreCallback: () -> Unit,
    callback: (Note) -> Unit,
) {
    val modifier = Modifier.padding(7.dp)
    LazyColumn(modifier = modifier, state = listState) {
        if (includeHeader) {
            item { HomeHeader(modifier, searchCallback, moreCallback) }
        }
        itemsIndexed(resultList.value) { index, element ->
            /*val backgroundColor = when (index) {
                    2 -> MaterialTheme.colors.secondary
                    0 -> Accent2
                    else -> MaterialTheme.colors.surface
                }*/

            Card(
                modifier = modifier.fillMaxWidth(),
                //backgroundColor = backgroundColor,
                shape = RoundedCornerShape(6.dp),
                onClick = { callback(element) }
            ) {
                Column(modifier = Modifier.padding(15.dp)) {

                    Text(
                        text = element.description.gridTrim(),
                        modifier = Modifier.fillMaxSize(),
                        style = MaterialTheme.typography.subtitle1
                    )

                    VerticalSpacer(7.dp)

                    Text(
                        text = element.updatedOn.timeAgo(),
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.caption
                    )

                }
            }

        }
    }

    if ((listState.firstVisibleItemScrollOffset > 338 && listState.firstVisibleItemIndex == 0) || listState.firstVisibleItemIndex > 0) {
        Card(
            modifier = modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
            shape = RectangleShape,
            elevation = 0.dp
        ) {
            Row(modifier, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h5
                )
                Iconsbar(modifier, searchCallback, moreCallback)
            }
        }
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
    Box(modifier = modifier) {
        Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h3)
        Row(modifier = Modifier.padding(0.dp, 120.dp, 0.dp, 0.dp)) {
            Iconsbar(modifier, searchCallback, moreCallback)
        }
        VerticalSpacer(16.dp)
    }
}

@Composable
fun PaperIconButton(@DrawableRes id: Int, tint: Color = MaterialTheme.colors.primary, onClick:()->Unit){
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