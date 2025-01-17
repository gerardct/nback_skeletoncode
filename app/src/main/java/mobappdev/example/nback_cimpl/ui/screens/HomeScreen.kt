package mobappdev.example.nback_cimpl.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

/**
 * This is the Home screen composable
 *
 * Currently this screen shows the saved highscore
 * It also contains a button which can be used to show that the C-integration works
 * Furthermore it contains two buttons that you can use to start a game
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun HomeScreen(
    vm: GameViewModel, navController: NavController
) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val gameType by vm.gameState.map { it.gameType }.collectAsState("")
    val nback = vm.nBack
    val scoreState by vm.score.collectAsState()    // Call the runVisualGame function when the GameScreen is created or based on some trigger

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    // Use the isPortrait variable in an if-else statement
    if (isPortrait) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "N-Back game",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(32.dp),

                    )
                Text(
                    modifier = Modifier.padding(50.dp),
                    text = "High-Score = $highscore",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "Previous score = $scoreState",
                    style = MaterialTheme.typography.headlineMedium
                )
                Box(
                    modifier = Modifier.padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Settings",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "Game type = $gameType"
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = "N-back = $nback"
                        )
                    }
                }
                // Todo: You'll probably want to change this "BOX" part of the composable

                Button(
                    onClick = {
                        // Show a snackbar first
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "STARTING GAME"
                            )
                        }
                        // Start the game after a slight delay (for demonstration purposes)
                        scope.launch {
                            delay(500) // Adjust the delay time as need
                            when (vm.gameState.value.gameType) {
                                GameType.Audio -> navController.navigate("AudioScreen")
                                else -> navController.navigate("GameScreen")
                            }
                            vm.startGame()
                        }
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Start Game".uppercase(),
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        vm.setGameType(GameType.Audio)
                        // Todo: change this button behaviour
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Hey! you clicked the audio button"
                            )
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sound_on),
                            contentDescription = "Sound",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                    Button(
                        onClick = {
                            vm.setGameType(GameType.Visual)
                            // Todo: change this button behaviour
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Hey! you clicked the visual button",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.visual),
                            contentDescription = "Visual",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                }
            }
        }
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(it), // Adjust padding as needed
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "N-Back game",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(20.dp)
                    )
                    Text(
                        modifier = Modifier.padding(20.dp),
                        text = "High-Score = $highscore",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Previous score = $scoreState",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Content for the third column
                    Button(onClick = {
                        vm.setGameType(GameType.Audio)
                        // Todo: change this button behaviour
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Hey! you clicked the audio button"
                            )
                        }
                    }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sound_on),
                            contentDescription = "Sound",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Button(
                        onClick = {
                            vm.setGameType(GameType.Visual)
                            // Todo: change this button behaviour
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Hey! you clicked the visual button",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.visual),
                            contentDescription = "Visual",
                            modifier = Modifier
                                .height(48.dp)
                                .aspectRatio(3f / 2f)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Content for the second column
                    Button(
                        onClick = {
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "STARTING GAME"
                                )
                            }
                            scope.launch {
                                delay(500)
                                when (vm.gameState.value.gameType) {
                                    GameType.Audio -> navController.navigate("AudioScreen")
                                    else -> navController.navigate("GameScreen")
                                }
                                vm.startGame()
                            }
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Start Game".uppercase(),
                            style = MaterialTheme.typography.displaySmall,
                            textAlign = TextAlign.Center // Center the text horizontally
                        )
                    }
                }
                // Add more columns as needed
            }
        }
    }
}



@Preview
@Composable
fun HomeScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface {
        HomeScreen(FakeVM(), navController = rememberNavController())
    }
}