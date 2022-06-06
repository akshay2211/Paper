package io.ak1.paper.ui.screens.setting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import io.ak1.paper.R
import io.ak1.paper.data.local.dataStore
import io.ak1.paper.data.local.isDarkThemeOn
import io.ak1.paper.data.local.themePreferenceKey
import io.ak1.paper.ui.component.PaperIconButton
import kotlinx.coroutines.launch

/**
 * Created by akshay on 06/12/21
 * https://ak1.io
 */


@Composable
fun SettingsScreen(navigateUp: () -> Unit,openWith:(String)->Unit) {
    Scaffold(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_title),
                        style = MaterialTheme.typography.h6, modifier = Modifier.padding(0.dp, 9.dp)
                    )
                },
                navigationIcon = {
                    PaperIconButton(id = R.drawable.ic_back) {
                        navigateUp.invoke()
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            )
        }
    ) { padding ->
        val context = LocalContext.current
        val theme = context.isDarkThemeOn().collectAsState(initial = 0)

        val coroutineScope = rememberCoroutineScope()
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            val elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            )
            Column(
                modifier = Modifier
                    .width(600.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp, 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {

                    Column(modifier = Modifier.weight(1f, true)) {
                        Text(
                            "Theme",
                            style = MaterialTheme.typography.h6,
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            stringResource(
                                id =
                                when (theme.value) {
                                    0 -> R.string.default_theme
                                    else -> R.string.custom_theme
                                }
                            ),
                            style = MaterialTheme.typography.overline,
                            textAlign = TextAlign.Start,
                        )
                    }
                    Switch(
                        colors = SwitchDefaults.colors(uncheckedThumbColor = MaterialTheme.colors.secondaryVariant),
                        onCheckedChange = {
                            coroutineScope.launch {
                                context.dataStore.edit { settings ->
                                    settings[themePreferenceKey] = if (it) 1 else 0
                                }
                            }
                        }, checked = theme.value != 0
                    )
                }
                if (theme.value != 0) {

                    Row(
                        modifier = Modifier
                            .padding(16.dp, 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,

                        ) {

                        Column(modifier = Modifier.weight(1f, true)) {
                            Text(
                                "Dark Mode",
                                style = MaterialTheme.typography.h6,
                                maxLines = 2,
                                textAlign = TextAlign.Start,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                stringResource(
                                    id =
                                    when (theme.value) {
                                        1 -> R.string.light_mode_selected
                                        2 -> R.string.dark_mode_selected
                                        else -> R.string.default_theme
                                    }
                                ),
                                style = MaterialTheme.typography.caption,
                                textAlign = TextAlign.Start,
                            )
                        }
                        Switch(
                            colors = SwitchDefaults.colors(uncheckedThumbColor = MaterialTheme.colors.secondaryVariant),
                            onCheckedChange = {
                                coroutineScope.launch {
                                    context.dataStore.edit { settings ->
                                        Log.e("checked ", "boolean $it")
                                        settings[themePreferenceKey] = if (it) 2 else 1
                                    }
                                }
                            }, checked = theme.value == 2
                        )
                    }
                }


                Column(
                    Modifier
                        .weight(1f, true), verticalArrangement = Arrangement.Bottom
                ) {


                    Row {
                        Button(
                            onClick = {openWith("https://github.com/akshay2211/Paper") },
                            elevation = elevation,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.weight(1f, true)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painterResource(id = R.drawable.ic_github),
                                    contentDescription = stringResource(id = R.string.app_name),
                                    tint = MaterialTheme.colors.surface,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .background(MaterialTheme.colors.onSurface, CircleShape)
                                        .padding(10.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                )
                                Text(
                                    text = "Get Source",
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Button(
                            onClick = {openWith("https://github.com/akshay2211/Paper") },
                            elevation = elevation,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.weight(1f, true)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painterResource(id = R.drawable.ic_star),
                                    contentDescription = stringResource(id = R.string.app_name),
                                    tint = Color(0xFFF44336),
                                    modifier = Modifier
                                        .background(MaterialTheme.colors.onSurface, CircleShape)
                                        .align(Alignment.CenterHorizontally)
                                        .padding(10.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                )
                                Text(
                                    text = "Spare a Star",
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Button(
                            onClick = {openWith("https://github.com/akshay2211/Paper/issues") },
                            elevation = elevation,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                            modifier = Modifier.weight(1f, true)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painterResource(id = R.drawable.ic_issue),
                                    contentDescription = stringResource(id = R.string.app_name),
                                    tint = Color(0xFFFBC02D),
                                    modifier = Modifier
                                        .background(MaterialTheme.colors.onSurface, CircleShape)
                                        .align(Alignment.CenterHorizontally)
                                        .padding(10.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                )
                                Text(
                                    text = "File a Bug",
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .navigationBarsPadding()
                    )

                }
            }
        }
    }
}



@Preview
@Composable
fun Preview() {
    SettingsScreen({}) {}
}