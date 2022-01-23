package io.ak1.paper.ui.screens.doodle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import io.ak1.paper.R
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations


/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
@Composable
fun DoodleScreen(navController: NavHostController) {

    //val history = remember{ mutableStateOf<>()}
    val drawController = rememberDrawController()

    Column {
        TopAppBar(
            title = { Text(text = "Doodle", color = MaterialTheme.colors.primary) },
            navigationIcon = {
                PaperIconButton(id = R.drawable.ic_back) {
                    navController.navigateUp()
                }
            },
            actions = {
                PaperIconButton(
                    id = R.drawable.ic_check,
                ) {
                    navController.navigateUp()
                }
                PaperIconButton(
                    id = R.drawable.ic_trash,
                ) {
                    //drawController.getDrawBoxBitmap()?.getEncodedString()
                    //var drawBoxDataText = drawController.exportPath()
                    //navController.popBackStack(Destinations.NOTE_ROUTE, true, false)

                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        )
        DrawBox(
            drawController = drawController,
            Modifier
                .fillMaxSize()
                .weight(1f, true)
        ) { one, two ->

        }
    }

}
