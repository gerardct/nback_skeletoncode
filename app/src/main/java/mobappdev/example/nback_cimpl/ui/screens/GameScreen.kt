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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import kotlinx.coroutines.delay



@Composable
fun GameScreen(
    vm:GameViewModel, navController: NavController)
{
    val score = '0'
    val currentEvent = '1'
    val totalEvents = '3'
    val snackBarHostState = remember { SnackbarHostState() }
    val nback = vm.nBack

    val gameState by vm.gameState.collectAsState()
    val events by vm.gameState.value.eventValue.

    // Call the runVisualGame function when the GameScreen is created or based on some trigger
    LaunchedEffect(key1 = Unit) {
        vm.runVisualGame(eventsArray, nBack)
    }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                // Button with left arrow icon
                Button(
                    onClick = { navController.navigate("HomeScreen")},
                    modifier = Modifier.padding(end = 1.dp)
                ) {
                    Icon(
                        painter = painterResource(id = mobappdev.example.nback_cimpl.R.drawable.pngtree_vector_left_arrow_icon_png_image_927204), // Replace with your arrow icon
                        contentDescription = "Arrow icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                modifier = Modifier.padding(22.dp),
                text = "Score = $score",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(modifier = Modifier.padding(2.dp),
                text = "current event = $currentEvent/$totalEvents",
                style = MaterialTheme.typography.headlineMedium)
            Text(modifier = Modifier.padding(2.dp),
                text = "N = $nback",
                style = MaterialTheme.typography.headlineSmall)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GridContainer(events)
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
    val totalCells = 9

    var cells by remember { mutableStateOf(List(totalCells) { index ->
        CellData(index, if (index in stimuliIndices) Color.Green else Color.LightGray)
    }) }

    LaunchedEffect(stimuliIndices) {
        // Change specified cells to green and revert to original color after a delay
        stimuliIndices.forEach { index ->
            cells = cells.toMutableList().also { updatedCells ->
                updatedCells[index] = CellData(index, Color.Green)
            }
            delay(1000) // Adjust delay time as needed
            cells = cells.toMutableList().also { updatedCells ->
                updatedCells[index] = CellData(index, Color.LightGray)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(GridCells.Fixed(3),
            content = {
                items(cells) { cellData ->
                    Cell(cellData = cellData)
                }
            }
        )
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
    Surface() { GameScreen(FakeVM(), navController = rememberNavController())}
}

