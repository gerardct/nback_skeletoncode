package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R


@Composable
fun GameScreen(
    vm:GameViewModel, navController: NavController)
{
    val snackBarHostState = remember { SnackbarHostState() }
    val nback = vm.nBack
    val gameState by vm.gameState.collectAsState()
    val highscore by vm.highscore.collectAsState()
    val totalEvents = gameState.size.value // Access size from the ViewModel
    val nBackEvent = gameState.index.value
    val scoreState by vm.score.collectAsState()    // Call the runVisualGame function when the GameScreen is created or based on some trigger

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
                    onClick = {
                        navController.navigate("HomeScreen")},
                    modifier = Modifier.padding(end = 1.dp)
                ) { Icon(
                        painter = painterResource(id = mobappdev.example.nback_cimpl.R.drawable.pngtree_vector_left_arrow_icon_png_image_927204), // Replace with your arrow icon
                        contentDescription = "Arrow icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                modifier = Modifier.padding(22.dp),
                text = "Score = $scoreState",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(modifier = Modifier.padding(2.dp),
                text = "current event = ${nBackEvent+1}/${totalEvents}",
                style = MaterialTheme.typography.headlineMedium)
            Text(modifier = Modifier.padding(2.dp),
                text = "N = $nback",
                style = MaterialTheme.typography.headlineSmall)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GridContainer(gameState.eventValue)
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ){
                val buttonColor = gameState.buttonColor.value

                Button(
                    onClick = {
                        // Check if there's a match based on the game state
                        vm.checkMatch()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                        .fillMaxWidth()
                        .padding(end = 1.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(8.dp) // Add content padding to see the background color
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.visual),
                            contentDescription = "Visual Icon",
                            modifier = Modifier.size(40.dp),
                            tint = when (buttonColor) {
                                Color.Green -> Color.Green // Set button background color to green when button color is green
                                Color.Red -> Color.Red // Set button background color to red when button color is red
                                else -> Color.White // Default button background color
                            }// Adjust the icon size as needed
                        )
                        Spacer(modifier = Modifier.width(14.dp)) // Add space between icon and text
                        Text(
                            text = "Position",
                            style = TextStyle(fontSize = 60.sp),
                            color = when (buttonColor) {
                                Color.Green -> Color.Green // Set button background color to green when button color is green
                                Color.Red -> Color.Red // Set button background color to red when button color is red
                                else -> Color.White // Default button background color
                            }
                        )}
                }

            }
        }
    }
}
@Composable
fun GridContainer(stimuliIndices: Int) {
    val totalCells = 9

    var cells by remember { mutableStateOf(List(totalCells) { index ->
        CellData(index, Color.LightGray)
    }) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(stimuliIndices) {
        val highlightedCells = mutableSetOf<Int>()
        highlightedCells += stimuliIndices

        cells = cells.mapIndexed { index, cellData ->
            if (index in highlightedCells) {
                scope.launch {
                    cells = cells.toMutableList().also { updatedCells ->
                        updatedCells[index] = CellData(index, Color.Yellow)
                    }
                    delay(2000) // Adjust delay time as needed
                    cells = cells.toMutableList().also { updatedCells ->
                        updatedCells[index] = CellData(index, Color.LightGray)
                    }
                }
                CellData(index, Color.Yellow)
            } else {
                cellData
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(4.dp), // Add space between cells
            content = {
                items(cells.size) { index ->
                    Cell(cellData = cells[index])
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
            .padding(4.dp) // Add padding around the cell
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cellData.color)
                .padding(8.dp), // Add padding inside the cell
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
    Surface { GameScreen(FakeVM(), navController = rememberNavController())}
}

