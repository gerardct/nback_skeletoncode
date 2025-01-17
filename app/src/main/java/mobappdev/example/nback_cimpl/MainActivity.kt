package mobappdev.example.nback_cimpl

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.screens.AudioScreen
import mobappdev.example.nback_cimpl.ui.screens.GameScreen
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.theme.NBack_CImplTheme
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import java.util.Locale

/**
 * This is the MainActivity of the application
 *
 * Your navigation between the two (or more) screens should be handled here
 * For this application you need at least a homescreen (a start is already made for you)
 * and a gamescreen (you will have to make yourself, but you can use the same viewmodel)
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


class MainActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.US)

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Language not supported")
                } else {
                    // TextToSpeech initialized successfully
                    // Now, you can use 'textToSpeech' in your application
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }

        setContent {
            NBack_CImplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create NavController
                    val navController = rememberNavController()
                    val gameViewModel: GameVM = viewModel(
                        factory = GameVM.Factory
                    )
                    gameViewModel.textToSpeech = textToSpeech

                    // Pass NavController to HomeScreen
                    NavHost(
                        navController = navController,
                        startDestination = "HomeScreen"
                    ) {
                        composable("HomeScreen") {
                            gameViewModel.resetGame()

                            HomeScreen(vm = gameViewModel, navController = navController)
                        }
                        composable("GameScreen") {
                            // Instantiate the viewmodel
                            GameScreen(vm = gameViewModel, navController = navController)
                        }
                        composable("AudioScreen") {
                            // Instantiate the viewmodel
                            AudioScreen(vm = gameViewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
