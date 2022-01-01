package io.ak1.paper.ui.screens.doodle

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.ak1.drawbox.DrawBox
import io.ak1.paper.R

/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
@Composable
fun DoodleScreen(navController: NavHostController) {
    Column {
        TopAppBar(
            title = {
                Text(text = "Doodle", color = MaterialTheme.colors.primary)
            },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.image_desc),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier
                        .clickable {
                            navController.navigateUp()
                        }
                        .padding(12.dp)
                )
            },
            actions = {
                Image(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = stringResource(id = R.string.image_desc),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier
                        .clickable {
                            navController.navigateUp()
                        }
                        .padding(12.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ic_trash),
                    contentDescription = stringResource(id = R.string.image_desc),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    modifier = Modifier
                        .clickable {
                            navController.navigateUp()
                        }
                        .padding(12.dp)
                )
            },
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.background
        )
        DrawBox(
            Modifier
                .fillMaxSize()
                .weight(1f, true)
        )
    }

}