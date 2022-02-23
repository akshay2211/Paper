package io.ak1.paper.ui.screens.doodle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.GsonBuilder
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController
import io.ak1.paper.R
import io.ak1.paper.models.Doodle
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.utils.getEncodedString
import org.koin.androidx.compose.getViewModel


/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
@Composable
fun DoodleScreen(navController: NavHostController, isNewDoodle: Boolean, id: String) {


    val drawController = rememberDrawController()
    val homeViewModel = getViewModel<HomeViewModel>()

    val doodleData = remember {
        mutableStateOf<Doodle>(Doodle(id,"",""))
    }
  /*LaunchedEffect(Unit) {
      if(isNewDoodle){

      }
        // inputService?.showSoftwareKeyboard()
        //focusRequester.requestFocus()
    }*/
    fun saveAndExit() {
        if(isNewDoodle){
            homeViewModel.saveDoodle(doodleData.value)
        }

    }


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
                    val base64 = drawController.getDrawBoxBitmap()?.getEncodedString() ?: ""
                    val list = drawController.exportPath()
                    val json = GsonBuilder().create().toJson(list)

                    doodleData.value.apply {
                        this.rawText = json
                        this.base64Text = base64
                    }
                    //  val doodle = Doodle(json, base64)
                    // doodleData.value = doodle
                    saveAndExit()

                }
                PaperIconButton(
                    id = R.drawable.ic_trash,
                ) {
                    navController.navigateUp()
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
        )
    }
}