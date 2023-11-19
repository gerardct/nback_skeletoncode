package mobappdev.example.nback_cimpl.ui.screens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

@Composable
fun AudioScreen(vm: GameViewModel, navController: NavController){
    val snackBarHostState = remember { SnackbarHostState() }
    val nback = vm.nBack
    val gameState by vm.gameState.collectAsState()
    val totalEvents = gameState.size.value// Access size from the ViewModel
    val nBackEvent = gameState.index.value
    val scoreState by vm.score.collectAsState()

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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Button with left arrow icon
                Button(
                    onClick = {
                        navController.navigate("HomeScreen")},
                    modifier = Modifier.padding(end = 1.dp)
                ) { Icon(
                    painter = painterResource(id = R.drawable.pngtree_vector_left_arrow_icon_png_image_927204), // Replace with your arrow icon
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
                text = "current event = ${nBackEvent+1}/${totalEvents-1}",
                style = MaterialTheme.typography.headlineMedium)
            Text(modifier = Modifier.padding(2.dp),
                text = "N = $nback",
                style = MaterialTheme.typography.headlineSmall)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sound_on),
                    contentDescription = "Visual Icon",
                    modifier = Modifier.size(250.dp))}
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
                        vm.updateButtonClickValue(1)// Modify buttonClick value to 0 when the button is clicked
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
}

@Preview
@Composable
fun AudioScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface { AudioScreen(FakeVM(), navController = rememberNavController()) }
}