package com.tpp.theperiodpurse.ui.symptomlog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tpp.theperiodpurse.data.model.LogPrompt
import com.tpp.theperiodpurse.data.model.LogSquare
import com.tpp.theperiodpurse.ui.viewmodel.AppViewModel
import com.tpp.theperiodpurse.ui.viewmodel.LogViewModel

@Composable
fun OvulationPrompt(logViewModel: LogViewModel, appViewModel: AppViewModel) {
    var selected by remember {
        mutableStateOf(logViewModel.getSquareSelected(logPrompt = LogPrompt.Ovulation))
    }
    Column() {
        val flowSquares = listOf(
            LogSquare.Ovulating,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 16.dp,
            ),
            modifier = Modifier
                .height(135.dp),
            content = {
                items(flowSquares) { flowSquare ->
                    LogSelectableSquare(
                        logSquare = flowSquare,
                        selected = selected,
                        appViewModel = appViewModel
                    ) { logSquare ->
                        if (selected == logSquare.description) {
                            selected = null
                            logViewModel.resetSquareSelected(logSquare)
                        } else {
                            selected = logSquare.description
                            logViewModel.setSquareSelected(logSquare)
                        }
                    }
                }
            },
        )
    }
}