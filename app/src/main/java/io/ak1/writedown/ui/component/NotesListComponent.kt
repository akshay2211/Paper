package io.ak1.writedown.ui.component

import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.ak1.writedown.R
import io.ak1.writedown.models.Note
import java.util.*

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
                        style = MaterialTheme.typography.h6
                    )

                    Spacer(modifier = Modifier.height(5.dp))

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

fun Long.timeAgo() = DateUtils.getRelativeTimeSpanString(
    this,
    Calendar.getInstance().timeInMillis,
    DateUtils.MINUTE_IN_MILLIS
).toString()


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeHeader(modifier: Modifier) {
    Column(modifier = modifier) {
        Row {
            Text(text = "Note", style = MaterialTheme.typography.h2)
            Image(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = stringResource(
                    id = R.string.image_desc
                ),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )
        }
        SearchBar {}
    }

}

@Composable
fun SearchBar(function: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }
    val description = remember {
        mutableStateOf(TextFieldValue())
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(0.dp),
        shape = RoundedCornerShape(6.dp)
    ) {
        TextField(
            value = description.value,
            onValueChange = {
                description.value = it
            },singleLine=true,
            textStyle = MaterialTheme.typography.h6,
            modifier = Modifier.padding(0.dp)
                .fillMaxWidth().background(Color.Red)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focus.value != focusState.isFocused) {
                        focus.value = focusState.isFocused
                        if (!focusState.isFocused) {
                            inputService?.hideSoftwareKeyboard()
                        }
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            /*
            *     label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    * */
            placeholder = {
                Text(text = "Search Label",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }/*
            placeholder = {
                Text(text = "Search Placeholder")
            },*/
            /* leadingIcon = {
                 Image(
                     painter = painterResource(id = R.drawable.ic_settings),
                     contentDescription = stringResource(
                         id = R.string.image_desc
                     ),
                     colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                 )
             },*/
            /* trailingIcon = {
                 Image(
                     painter = painterResource(id = R.drawable.ic_settings),
                     contentDescription = stringResource(
                         id = R.string.image_desc
                     ),
                     colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                 )
             }*/


        )
    }
}

fun String.gridTrim(maxDigits: Int = 100) =
    if (this.length > 100) "${this.substring(maxDigits)}..." else this