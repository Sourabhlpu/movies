package sourabh.pal.findfalcone.find.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sourabh.pal.findfalcone.common.domain.MaxPlanetSelected
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

private const val MAX_NO_OF_PLANETS_TO_BE_SELECTED = 4

class FindFalconeFragmentViewModel : ViewModel() {

    val state: LiveData<FindFalconeViewState> get() = _state
    private val _state = MutableLiveData<FindFalconeViewState>()


    init {
        _state.value = FindFalconeViewState()
    }

    fun onEvent(event: FindFalconeEvent) {
        when (event) {
            is FindFalconeEvent.PlanetSelected -> onPlanetSelected(
                event.isSelected,
                event.selectedIndex
            )
        }
    }

    private fun onPlanetSelected(isSelected: Boolean, selectedIndex: Int) {
        val currentState = state.value
        if (isSelected && currentState!!.numberOfSelectedPlanets >= MAX_NO_OF_PLANETS_TO_BE_SELECTED  ) {
            _state.value = currentState.copy(
                failure = Event(MaxPlanetSelected())
            )
        } else {
            val planetsUpdated = getPlanetsListAfterSelection(isSelected, selectedIndex)
            val vehicles = state.value!!.vehicles
            _state.value = currentState?.copy(
                planets = planetsUpdated,
                vehiclesForSelectedPlanet = VehiclesForPlanet(planetsUpdated[selectedIndex], vehicles)
            )
        }
    }


    private fun getPlanetsListAfterSelection(
        isSelected: Boolean,
        selectedIndex: Int
    ): List<UIPlanet> {
        return state.value!!.planets.mapIndexed { index, uiPlanet ->
            if (index == selectedIndex)
                uiPlanet.copy(isSelected = isSelected)
            else
                uiPlanet
        }
    }
}
