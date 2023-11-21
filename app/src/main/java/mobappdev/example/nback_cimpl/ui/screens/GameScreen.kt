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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import mobappdev.example.nback_cimpl.R


@Composable
fun GameScreen(
    vm:GameViewModel, navController: NavController) {

    val snackBarHostState = remember { SnackbarHostState() }
    val nback = vm.nBack
    val gameState by vm.gameState.collectAsState()
    val totalEvents = vm.size.collectAsState().value  // Access size from the ViewModel
    val nBackEvent = gameState.index.value
    val scoreState by vm.score.collectAsState()    // Call the runVisualGame function when the GameScreen is created or based on some trigger
    val gamefinished = gameState.gamefinished

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Button with left arrow icon
                Button(
                    onClick = {
                        navController.navigate("HomeScreen")
                        vm.resetGame()
                    },
                    modifier = Modifier.padding(end = 1.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home), // Replace with your arrow icon
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
            Text(
                modifier = Modifier.padding(2.dp),
                text = "current event = ${nBackEvent + 1}/${totalEvents}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                modifier = Modifier.padding(2.dp),
                text = "N = $nback",
                style = MaterialTheme.typography.headlineSmall
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Grid(
                        highlightedIndex = gameState.eventValue,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val buttonColor = gameState.buttonColor.value

                Button(
                    onClick = {
                        vm.checkMatch()// Modify buttonClick value to 0 when the button is clicked
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
                        )
                    }
                }

            }
        }
    }
    if (gamefinished == 1) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Display a square with the obtained score
            Box(
                modifier = Modifier
                    .size(600.dp)
                    .background(Color.Blue),
                contentAlignment = Alignment.Center // Center content in the blue box
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Obtained Score: $scoreState",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center // Center align the text
                        )
                    )
                    Text(
                        text = "Press home button to play again",
                        color = Color.White,
                        textAlign = TextAlign.Center // Center align the text
                    )
                }
            }
        }
    } else {
        // Other UI elements and game-related components in your GameScreen when the game is ongoing
    }}
@Composable
fun Grid(
    highlightedIndex: Int
) {
    Column (modifier = Modifier.fillMaxSize()
    ){
        for (i in 0 until 3) {
            Row (modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){
                for (j in 0 until 3) {
                    val index = i * 3 + j
                    val color = if (index + 1 == highlightedIndex) Color.Yellow else Color.LightGray

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                            .background(color)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface { GameScreen(FakeVM(), navController = rememberNavController())}
}

