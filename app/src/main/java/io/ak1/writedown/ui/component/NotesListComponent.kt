package io.ak1.writedown.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.writedown.R
import io.ak1.writedown.models.Note
import io.ak1.writedown.ui.utils.gridTrim
import io.ak1.writedown.ui.utils.timeAgo

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun NotesListComponent(
    resultList: State<List<Note>>,
    listState: LazyListState,
    callback: (Note) -> Unit
) {
    val modifier = Modifier.padding(7.dp)
    LazyColumn(modifier = modifier, state = listState) {
        item { HomeHeader(modifier) }
        itemsIndexed(resultList.value) { _, element ->
            Card(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                onClick = { callback(element) }
            ) {
                Column(modifier = Modifier.padding(15.dp)) {

                    Text(
                        text = element.description.gridTrim(),
                        modifier = Modifier.fillMaxSize(),
                        style = MaterialTheme.typography.subtitle1
                    )

                    verticalSpacer(7.dp)

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
}


@Composable
fun HomeHeader(modifier: Modifier) {
    Box(modifier = modifier) {
        Text(text = "Notes", style = MaterialTheme.typography.h3)
        Row(modifier = Modifier.padding(0.dp, 120.dp, 0.dp, 0.dp)) {
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
                modifier = modifier,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = stringResource(
                    id = R.string.image_desc
                ),
                modifier = modifier,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
        }
        verticalSpacer(16.dp)
    }
}