package sourabh.pal.findfalcone.find.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails
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


    init {
        _state.value = FindFalconeViewState()
    }

    fun onEvent(event: FindFalconeEvent) {
        when (event) {
            is FindFalconeEvent.PlanetSelected -> onPlanetSelected(event.selectedIndex)
            is FindFalconeEvent.PlanetUnSelected -> onPlanetUnSelected(event.selectedIndex)
            is FindFalconeEvent.OnPageSelected -> updateSelectedPageIndex(event.position)
            FindFalconeEvent.GetPlanets -> loadAllPlanets()
            FindFalconeEvent.GetVehicles -> loadAllVehicles()
            FindFalconeEvent.Submit -> TODO()
            is FindFalconeEvent.OnVehicleClicked -> updateVehicleSelection(event.vehicle)
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

    private fun updateVehicleSelection(vehicle: UIVehicleWitDetails) {
        if(vehicle.isSelected)
        _state.value = state.value!!.updateToVehicleUnSelected(vehicle)
        else
            _state.value = state.value!!.updateToVehicleSelected(vehicle)
    }

    private fun updateSelectedPageIndex(position: Int) {
        _state.value = state.value!!.updateToWhenPageIsChanged(position)
    }

    private fun onPlanetSelected(selectedIndex: Int) {
        _state.value = state.value!!.updateToPlanetSelected(selectedIndex)
    }

    private fun onPlanetUnSelected(selectedIndex: Int) {
        _state.value = state.value!!.updateToPlanetUnSelected(selectedIndex)
    }

    private fun areAllPlanetsSelected() {}

}
