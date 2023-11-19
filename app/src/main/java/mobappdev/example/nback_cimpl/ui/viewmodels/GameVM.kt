package mobappdev.example.nback_cimpl.ui.viewmodels

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import java.util.Locale

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int
    val size: StateFlow<Int>

    fun setGameType(gameType: GameType)
    fun startGame()

    fun updateButtonClickValue(value: Int)

    fun checkMatch()
    fun resetGame()
}


class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    private val _size = MutableStateFlow(0)
    override val size: StateFlow<Int>
        get() = _size

    // nBack is currently hardcoded
    override val nBack: Int
        get() = 2

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events


    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }
    override fun resetGame() {
        // Reset score, game state, or any other necessary values
        _score.value = 0
        _gameState.value = GameState()}

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop
        _score.value = 0
        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList()
            .toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}");
        gameState.value.size.value  = events.size

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(events, nBack)
                GameType.AudioVisual -> runAudioVisualGame()
                GameType.Visual -> runVisualGame(events, nBack)
            }
            // Todo: update the highscore
            val obtainedScore = _score.value
            val previousHighScore = _highscore.value

            if (obtainedScore > previousHighScore) {
                // Update the high score if the obtained score is higher
                userPreferencesRepository.saveHighScore(obtainedScore)
                _highscore.value = obtainedScore
            }
        }
    }
    // Other properties and methods

    private val debounceTimeMillis = 2000L // Adjust this debounce time as needed
    private var lastClickTimeMillis = 0L
    var buttonClick: Int = 0 // Initially set to 0

    override fun updateButtonClickValue(value: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTimeMillis >= debounceTimeMillis) {
            lastClickTimeMillis = currentTime
            buttonClick = value
            checkMatch()
        }
    }

    override fun checkMatch() {
        val matchIndex = _gameState.value.index.value
        val match = _gameState.value.isMatch

        if (buttonClick == 1) {
            if (matchIndex > nBack && match) {
                _score.value++
                _gameState.value.buttonColor.value = Color.Green
                // Add the current matchIndex to the set
            } else {
                _gameState.value.buttonColor.value = Color.Red
            }

            viewModelScope.launch {
                delay(1000) // Revert button color after a short time
                _gameState.value.buttonColor.value = Color.White
            }
        } else {
            // Handle other cases when buttonClick is not 1
        }
    }


    private val numberToLetter = hashMapOf(
        1 to "A",
        2 to "B",
        3 to "C",
        4 to "D",
        5 to "E",
        6 to "F",
        7 to "G",
        8 to "H",
        9 to "I"
    )

    private suspend fun runAudioGame(events: Array<Int>, nBack: Int) {
        delay(2000)

        val numberToLetter = hashMapOf(
            1 to "A",
            2 to "B",
            3 to "C",
            4 to "D",
            5 to "E",
            6 to "F",
            7 to "G",
            8 to "H",
            9 to "I"
        )

        for (index in events.indices) {
            _gameState.value = _gameState.value.copy(eventValue = events[index])
            gameState.value.index.value = index

            // Speak the letter associated with the number
            val letter = numberToLetter[events[index]]
            if (letter != null) {
                //textToSpeech.speak(letter, TextToSpeech.QUEUE_ADD, null, null)
                delay(eventInterval) // Add delay between letters
            }

            if (index >= nBack) {
                if (events[index] == events[index - nBack]) {
                    _gameState.value = _gameState.value.copy(isMatch = true)
                    Log.d("Check:match", "True")
                } else {
                    _gameState.value = _gameState.value.copy(isMatch = false)
                    Log.d("Check_match", "False")
                }
            }
            // Add a delay between events if needed
            delay(eventInterval)
        }
    }


    private suspend fun runVisualGame(events: Array<Int>, nBack: Int) {
            delay(2000)

            // Todo: Replace this code for actual game code
            for (index in events.indices) {
                _gameState.value = _gameState.value.copy(eventValue = events[index])
                gameState.value.index.value = index
                if (index >= nBack){
                    if (events[index] == events[index-nBack]){
                        _gameState.value = _gameState.value.copy(isMatch = true)
                        Log.d("Check:match","True")
                    }
                    else{_gameState.value = _gameState.value.copy(isMatch = false)
                        Log.d("Check_match","False")
                    }
                }
                // Add a delay between events if needed
                delay(eventInterval)
            }
        }


    private fun runAudioVisualGame(){
        // Todo: Make work for Higher grade
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository)
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1,  // The value of the array string
    val isMatch: Boolean = false,
    val button: Int = 0,
    val index: MutableState<Int> = mutableStateOf(0),
    val size: MutableState<Int> = mutableStateOf(0),
    val buttonColor: MutableState<Color> = mutableStateOf(Color.Gray)
)

class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2
    override val size: StateFlow<Int>
        get() = MutableStateFlow(0)

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun updateButtonClickValue(value: Int) {
    }
    override fun checkMatch() {
    }

    override fun resetGame() {
    }
}