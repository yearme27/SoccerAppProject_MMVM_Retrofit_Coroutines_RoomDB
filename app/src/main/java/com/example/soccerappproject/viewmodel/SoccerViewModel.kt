package com.example.soccerappproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soccerappproject.api.SoccerRepository
import com.example.soccerappproject.model.UIState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

private const val TAG = "SoccerViewModel"

class SoccerViewModel(
    private val repository: SoccerRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _allLeagueListData = MutableLiveData<UIState>()
    val allLeagueListData: LiveData<UIState> get() = _allLeagueListData

    private val _allSeasonListData = MutableLiveData<UIState>()
    val allSeasonListData: LiveData<UIState> get() = _allSeasonListData

    private val _allStandingListData = MutableLiveData<UIState>()
    val allStandingListData: LiveData<UIState> get() = _allStandingListData

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG, "Context: $coroutineContext\nMessage: ${throwable.localizedMessage}", throwable)
    }

    private val viewModelSafeScope = viewModelScope + coroutineExceptionHandler

    private var leagueJob: Job? = null
    private var seasonJob: Job? = null
    private var standingJob: Job? = null

    private var loadedSeasonsFor: String? = null
    private var loadedStandingsFor: Pair<String, Int>? = null

    // Each load function is safe to call every time a screen appears: it skips the request
    // when the same data is already loaded or on its way, and retries after an error.

    fun loadLeagues() {
        if (_allLeagueListData.value is UIState.Success<*> || leagueJob?.isActive == true) return

        _allLeagueListData.value = UIState.Loading
        leagueJob = viewModelSafeScope.launch(dispatcher) {
            repository.getAllSoccerLeagues("leagues").collect {
                _allLeagueListData.postValue(it)
            }
        }
    }

    fun loadSeasons(id: String) {
        val sameRequest = id == loadedSeasonsFor
        if (sameRequest && (_allSeasonListData.value is UIState.Success<*> || seasonJob?.isActive == true)) return

        seasonJob?.cancel()
        loadedSeasonsFor = id
        _allSeasonListData.value = UIState.Loading
        seasonJob = viewModelSafeScope.launch(dispatcher) {
            repository.getSoccerSeason(id).collect {
                // Ignore a late result for a league the user has already left.
                if (loadedSeasonsFor == id) _allSeasonListData.postValue(it)
            }
        }
    }

    fun loadStandings(season: Int, id: String) {
        val request = id to season
        val sameRequest = request == loadedStandingsFor
        if (sameRequest && (_allStandingListData.value is UIState.Success<*> || standingJob?.isActive == true)) return

        standingJob?.cancel()
        loadedStandingsFor = request
        _allStandingListData.value = UIState.Loading
        standingJob = viewModelSafeScope.launch(dispatcher) {
            repository.getSoccerStanding(season, id, "asc").collect {
                if (loadedStandingsFor == request) _allStandingListData.postValue(it)
            }
        }
    }
}
