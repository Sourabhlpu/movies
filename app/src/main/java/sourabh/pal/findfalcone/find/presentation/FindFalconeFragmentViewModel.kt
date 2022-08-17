package sourabh.pal.findfalcone.find.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIPlanetMapper
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIVehicleMapper
import sourabh.pal.findfalcone.find.domain.usecases.GetPlanets
import sourabh.pal.findfalcone.find.domain.usecases.GetVehicles
import javax.inject.Inject


private const val MAX_NO_OF_PLANETS_TO_BE_SELECTED = 4

@HiltViewModel
class FindFalconeFragmentViewModel @Inject constructor(
    private val getPlanets: GetPlanets,
    private val getVehicles: GetVehicles,
    private val uiPlanetMapper: UIPlanetMapper,
    private val uiVehicleMapper: UIVehicleMapper
) : ViewModel() {

    val state: LiveData<FindFalconeViewState> get() = _state
    private val _state = MutableLiveData<FindFalconeViewState>()

    var currentSelectedPlanetsPage = 0

    init {
        _state.value = FindFalconeViewState()
    }

    fun onEvent(event: FindFalconeEvent) {
        when (event) {
            is FindFalconeEvent.PlanetSelected -> onPlanetSelected(
                event.isSelected,
                event.selectedIndex
            )
            is FindFalconeEvent.OnPageSelected -> updateSelectedPageIndex(event.position)
            FindFalconeEvent.GetPlanets -> loadAllPlanets()
            FindFalconeEvent.GetVehicles -> loadAllVehicles()
            FindFalconeEvent.Submit -> TODO()
            is FindFalconeEvent.OnVehicleClicked -> updateVehicheSelection(event.vehicle)
        }
    }

    private fun loadAllVehicles() {
        _state.value = state.value?.copy(loading = true)
        viewModelScope.launch {
            val vehicles = getVehicles()
            val uiVehicles = vehicles.map { uiVehicleMapper.mapToView(it) }
            _state.value = state.value?.updateToVehiclesListSuccess(uiVehicles)
        }
    }

    private fun loadAllPlanets() {
        _state.value = state.value?.copy(loading = true)
        viewModelScope.launch {
            val planets = getPlanets()
            val uiPlanets = planets.map { uiPlanetMapper.mapToView(it) }
            _state.value = state.value?.updateToPlanetsListSuccess(uiPlanets)
        }
    }

    private fun updateVehicheSelection(vehicle: UIVehicle) {
        _state.value = state.value!!.updateToVehicleSelected(currentSelectedPlanetsPage, vehicle)
    }

    private fun updateSelectedPageIndex(position: Int) {
        currentSelectedPlanetsPage = position
        _state.value = state.value?.updateWhenPlanetsPageChanged(position)
    }

    private fun onPlanetSelected(isSelected: Boolean, selectedIndex: Int) {
        val currentState = state.value
        if (isSelected && areAllPlanetsSelected(currentState)) {
            _state.value = currentState?.updateToAllPlanetsSelected()
        } else {
            _state.value = state.value!!.updateToPlanetSelected(isSelected, selectedIndex)
        }
    }

    private fun areAllPlanetsSelected(currentState: FindFalconeViewState?) =
        currentState!!.numberOfSelectedPlanets >= MAX_NO_OF_PLANETS_TO_BE_SELECTED
}
