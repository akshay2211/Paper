package io.ak1.writedown.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ak1.writedown.models.Note

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
    Column {
        HomeHeader {

        }

        LazyVerticalGrid(
            modifier = Modifier
                .padding(7.dp)
                .fillMaxSize(),
            cells = GridCells.Fixed(2), state = listState
        ) {

            itemsIndexed(resultList.value) { _, element ->
                Card(
                    modifier = Modifier
                        .padding(7.dp)
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(6.dp),
                    onClick = {
                        callback(element)
                    }
                ) {
                    Text(
                        text = element.description.gridTrim(), modifier = Modifier
                            .padding(15.dp)
                            .fillMaxSize()
                    )
                }

            }
        }
    }
}

@Composable
fun HomeHeader(function: () -> Unit) {
    Text(text = "Note", style = MaterialTheme.typography.h1)
}

fun String.gridTrim(maxDigits: Int = 100) =
    if (this.length > 100) "${this.substring(maxDigits)}..." else this