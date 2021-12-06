package io.ak1.paper.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.paper.R

/**
 * Created by akshay on 06/12/21
 * https://ak1.io
 */

@Composable
fun DefaultAppBar(
    @DrawableRes iconId: Int = R.drawable.ic_back,
    @StringRes titleId: Int,
    navController: NavController
) {
    Row(modifier = Modifier.padding(4.dp)) {
        Image(
            painter = painterResource(iconId),
            contentDescription = stringResource(id = R.string.image_desc),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            modifier = Modifier
                .clickable {
                    navController.navigateUp()
                }
                .padding(12.dp)
        )
        Text(
            text = stringResource(id = titleId),
            style = MaterialTheme.typography.h6, modifier = Modifier.padding(0.dp, 9.dp)
        )
    }
}