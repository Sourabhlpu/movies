package sourabh.pal.findfalcone.find.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

class FindFalconeFragmentViewModel : ViewModel() {

    val state: LiveData<FindFalconeViewState> get() = _state
    private val _state = MutableLiveData<FindFalconeViewState>()

    init {
        _state.value = FindFalconeViewState()
    }

    fun onEvent(event: FindFalconeEvent) {
        when (event) {
            is FindFalconeEvent.PlanetSelected -> onPlanetSelected(
                event.planet,
                event.selectedIndex
            )
        }
    }

    private fun onPlanetSelected(planet: String, selectedIndex: Int) {
        val planetsUpdated = getPlanetsListAfterSelection(planet, selectedIndex)
        _state.value = _state.value?.copy(
            planets = planetsUpdated,
            planetsName = planetsUpdated.filter { !it.isSelected }.map { it.name },
            showRadioGroup1 = planetsUpdated.find { it.selectedIndex == 0 } != null,
            showRadioGroup2 = planetsUpdated.find { it.selectedIndex == 1 } != null,
            showRadioGroup3 = planetsUpdated.find { it.selectedIndex == 2 } != null,
            showRadioGroup4 = planetsUpdated.find { it.selectedIndex == 3 } != null
        )
    }

    private fun getPlanetsListAfterSelection(
        planet: String,
        selectedIndex: Int
    ): List<UIPlanet> {
        var currentPlanetsMutableList = state.value?.planets.orEmpty()

        currentPlanetsMutableList = currentPlanetsMutableList.map {
            if (it.isSelected && it.selectedIndex == selectedIndex)
                it.copy(isSelected = false)
            else
                it
        }.toMutableList()
        return currentPlanetsMutableList.map {
            if (it.name == planet)
                it.copy(isSelected = true, selectedIndex = selectedIndex)
            else
                it
        }
    }
}
