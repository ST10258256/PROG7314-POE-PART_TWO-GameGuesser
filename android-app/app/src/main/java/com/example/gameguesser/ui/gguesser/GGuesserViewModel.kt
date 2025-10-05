package com.example.gameguesser.ui.gguesser

import android.app.Application
import androidx.lifecycle.*
import com.example.gameguesser.Database.AppDatabase
import com.example.gameguesser.data.Game
import com.example.gameguesser.models.RandomGameResponse
import com.example.gameguesser.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class MatchState { RED, YELLOW, GREEN }

data class FieldMatch(val fieldName: String, val expected: String, val actual: String, val state: MatchState)

data class GuessResult(
    val guessedGame: Game,
    val comparisons: List<FieldMatch>,
    val correct: Boolean
)

class GGuesserViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).gameDao()
    private val repo = GameRepository(dao)

    private val _target = MutableLiveData<Game?>()
    val target: LiveData<Game?> = _target

    private val _searchResults = MutableLiveData<List<Game>>(emptyList())
    val searchResults: LiveData<List<Game>> = _searchResults

    private val _guessHistory = MutableLiveData<List<GuessResult>>(emptyList())
    val guessHistory: LiveData<List<GuessResult>> = _guessHistory

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchRandomTarget() {
        _loading.value = true
        repo.fetchRandomGame { result ->
            viewModelScope.launch(Dispatchers.Main) {
                _loading.value = false
                result.fold(onSuccess = { rnd ->
                    if (rnd != null) {
                        // attempt to fetch the full game by id
                        fetchGameDetailsForTarget(rnd.id, rnd.name)
                    } else {
                        _error.value = "Received empty random game"
                    }
                }, onFailure = { t ->
                    _error.value = "Random API failed: ${t.localizedMessage}"
                })
            }
        }
    }

    private fun fetchGameDetailsForTarget(id: String, fallbackName: String) {
        _loading.value = true
        repo.fetchGameById(id) { result ->
            viewModelScope.launch(Dispatchers.Main) {
                _loading.value = false
                result.fold(onSuccess = { game ->
                    if (game != null) {
                        _target.value = game
                    } else {
                        // fallback: search by name via API full list, then local DB
                        fetchByNameFallback(fallbackName)
                    }
                }, onFailure = {
                    // fallback
                    fetchByNameFallback(fallbackName)
                })
            }
        }
    }

    private fun fetchByNameFallback(name: String) {
        _loading.value = true
        repo.fetchAllGamesFull { result ->
            viewModelScope.launch(Dispatchers.Main) {
                _loading.value = false
                result.fold(onSuccess = { list ->
                    val found = list.firstOrNull { it.name.equals(name, ignoreCase = true) }
                    if (found != null) {
                        _target.value = found
                    } else {
                        // try local DB
                        viewModelScope.launch(Dispatchers.IO) {
                            val localFound = repo.findGameByNameLocal(name)
                            viewModelScope.launch(Dispatchers.Main) {
                                if (localFound != null) _target.value = localFound
                                else _error.value = "Could not find full details for $name"
                            }
                        }
                    }
                }, onFailure = { t ->
                    _error.value = "Fallback search failed: ${t.localizedMessage}"
                })
            }
        }
    }

    fun searchGames(query: String) {
        _loading.value = true
        repo.fetchAllGamesFull { result ->
            viewModelScope.launch(Dispatchers.Main) {
                _loading.value = false
                result.fold(onSuccess = { list ->
                    val filtered = list.filter { it.name.contains(query, ignoreCase = true) }
                    _searchResults.value = filtered
                }, onFailure = { t ->
                    // try local DB instead
                    viewModelScope.launch(Dispatchers.IO) {
                        val local = dao.getAllGames().filter {
                            it.name.contains(query, ignoreCase = true)
                        }
                        viewModelScope.launch(Dispatchers.Main) {
                            _searchResults.value = local
                        }
                    }
                })
            }
        }
    }

    fun submitGuess(guess: Game) {
        // Compare guess to target
        val targetGame = _target.value
        if (targetGame == null) {
            _error.value = "No target game set. Press 'Fetch Random'."
            return
        }

        val comparisons = compareGames(targetGame, guess)
        val correct = comparisons.all { it.state == MatchState.GREEN }

        val newResult = GuessResult(guessedGame = guess, comparisons = comparisons, correct = correct)
        _guessHistory.value = _guessHistory.value!!.plus(newResult)

        // Optionally: call remote submitGuess endpoint to get server hint
        // your ApiService has submitGuess(...) — we can call it but not used for main comparison here
        // repository could send it if you want; omitted for brevity
    }

    private fun compareGames(target: Game, guess: Game): List<FieldMatch> {
        val list = mutableListOf<FieldMatch>()

        // Helper lambdas
        fun exactOr(guessVal: String?, targetVal: String?, fieldName: String) {
            val g = guessVal ?: ""
            val t = targetVal ?: ""
            val state = if (g.equals(t, ignoreCase = true)) MatchState.GREEN else MatchState.RED
            list.add(FieldMatch(fieldName, t, g, state))
        }

        // Name: exact -> GREEN, substring -> YELLOW
        val nameState = when {
            guess.name.equals(target.name, ignoreCase = true) -> MatchState.GREEN
            target.name.contains(guess.name, ignoreCase = true) || guess.name.contains(target.name, ignoreCase = true) -> MatchState.YELLOW
            else -> MatchState.RED
        }
        list.add(FieldMatch("Name", target.name, guess.name, nameState))

        // Genre exact -> GREEN, partial match (contains) -> YELLOW
        val genreState = when {
            guess.genre.equals(target.genre, ignoreCase = true) -> MatchState.GREEN
            target.genre.contains(guess.genre, ignoreCase = true) || guess.genre.contains(target.genre, ignoreCase = true) -> MatchState.YELLOW
            else -> MatchState.RED
        }
        list.add(FieldMatch("Genre", target.genre, guess.genre, genreState))

        // Platforms: intersection -> GREEN if identical sets, YELLOW if overlapping
        val targetPlatforms = target.platforms.map { it.lowercase() }.toSet()
        val guessPlatforms = guess.platforms.map { it.lowercase() }.toSet()
        val intersection = targetPlatforms.intersect(guessPlatforms)
        val platformState = when {
            targetPlatforms == guessPlatforms -> MatchState.GREEN
            intersection.isNotEmpty() -> MatchState.YELLOW
            else -> MatchState.RED
        }
        list.add(FieldMatch("Platforms", target.platforms.joinToString(), guess.platforms.joinToString(), platformState))

        // Release year: exact -> GREEN; same decade -> YELLOW
        val yearState = when {
            guess.releaseYear == target.releaseYear -> MatchState.GREEN
            (guess.releaseYear / 10) == (target.releaseYear / 10) -> MatchState.YELLOW
            else -> MatchState.RED
        }
        list.add(FieldMatch("Release Year", target.releaseYear.toString(), guess.releaseYear.toString(), yearState))

        // Developer & Publisher: GREEN exact, YELLOW if contains
        val devState = if (guess.developer.equals(target.developer, ignoreCase = true)) MatchState.GREEN
        else if (target.developer.contains(guess.developer, ignoreCase = true) || guess.developer.contains(target.developer, ignoreCase = true)) MatchState.YELLOW
        else MatchState.RED
        list.add(FieldMatch("Developer", target.developer, guess.developer, devState))

        val pubState = if (guess.publisher.equals(target.publisher, ignoreCase = true)) MatchState.GREEN
        else if (target.publisher.contains(guess.publisher, ignoreCase = true) || guess.publisher.contains(target.publisher, ignoreCase = true)) MatchState.YELLOW
        else MatchState.RED
        list.add(FieldMatch("Publisher", target.publisher, guess.publisher, pubState))

        // Budget: numeric tolerance -> GREEN if within 2.5%? (we'll use 10% tolerance), YELLOW if within 30%
        val budgetToleranceGreen = 0.10
        val budgetToleranceYellow = 0.30
        val bState = try {
            val g = guess.budget
            val t = target.budget
            val pctDiff = abs(g - t) / (if (t == 0.0) 1.0 else t)
            when {
                pctDiff <= budgetToleranceGreen -> MatchState.GREEN
                pctDiff <= budgetToleranceYellow -> MatchState.YELLOW
                else -> MatchState.RED
            }
        } catch (e: Exception) {
            MatchState.RED
        }
        list.add(FieldMatch("Budget", target.budget.toString(), guess.budget.toString(), bState))

        // Saga / POV: simple exact/contains
        list.add(FieldMatch("Saga", target.saga, guess.saga, if (guess.saga.equals(target.saga, true)) MatchState.GREEN else if (target.saga.contains(guess.saga, true) || guess.saga.contains(target.saga, true)) MatchState.YELLOW else MatchState.RED))
        list.add(FieldMatch("POV", target.pov, guess.pov, if (guess.pov.equals(target.pov, true)) MatchState.GREEN else if (target.pov.contains(guess.pov, true) || guess.pov.contains(target.pov, true)) MatchState.YELLOW else MatchState.RED))

        return list
    }
}
