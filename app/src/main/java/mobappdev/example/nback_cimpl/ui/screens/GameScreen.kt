package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel


@Composable
fun GameScreen(
    vm:GameViewModel)
{
    val score = '0'
    val currentEvent = '1'
    val totalEvents = '3'
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold (
        snackbarHost = { SnackbarHost (snackBarHostState) }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier.padding(32.dp),
                text = "Score = $score",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(modifier = Modifier.padding(20.dp),
                text = "current event = $currentEvent/$totalEvents",
                style = MaterialTheme.typography.headlineMedium)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GridContainer(listOf(1))
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary))
                    {
                        Text(text = "Match",
                            style = TextStyle(fontSize = 60.sp)
                        )
                    }
            }
        }
    }
}
@Composable
fun GridContainer(stimuliIndices: List<Int>) {
    var cells by remember { mutableStateOf(List(9) { index ->
                val cellColor = if (index in stimuliIndices) Color.Green else Color.LightGray
                CellData(index, cellColor)
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            items(cells.size) { index ->
                Cell(cellData = cells[index])
            }
        }
    }
}

@Composable
fun Cell(cellData: CellData) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
            .background(cellData.color)
            .padding(8.dp)
            .shadow(1.dp)
    ) {
        // You can add content inside the cell if needed
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${cellData.index}")
        }
    }
}

data class CellData(val index: Int, val color: Color)

@Preview
@Composable
fun GameScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface() { GameScreen(FakeVM())}
}

