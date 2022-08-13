package sourabh.pal.findfalcone.find.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import javax.inject.Inject


private const val MAX_NO_OF_PLANETS_TO_BE_SELECTED = 4

@HiltViewModel
class FindFalconeFragmentViewModel @Inject constructor() : ViewModel() {

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
            FindFalconeEvent.GetPlanetsAndVehicles -> TODO()
            FindFalconeEvent.Submit -> TODO()
            is FindFalconeEvent.OnPlanetClicked -> updateVehicheSelection(event.vehicle)
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
