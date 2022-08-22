package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

data class FindFalconeViewState(
    val loading: Boolean = false,
    val planets: List<UIPlanet> = emptyList(),
    val vehicles: List<UIVehicle> = emptyList(),
    val selectedPairs: List<Pair<UIPlanet, UIVehicle>> = emptyList(),
    val currentPlanet: UIPlanet = UIPlanet(),
    val vehiclesForCurrentPlanet: List<UIVehicle> = emptyList(),
    val showVehicles: Boolean = false,
    val failure: Event<Throwable>? = null
) {

    fun updateToVehiclesListSuccess(uiVehicles: List<UIVehicle>): FindFalconeViewState {
        return copy(
            loading = false,
            vehicles = uiVehicles,
            vehiclesForCurrentPlanet = uiVehicles
        )
    }

    fun updateToPlanetsListSuccess(uiPlanets: List<UIPlanet>): FindFalconeViewState {
        return copy(loading = false, planets = uiPlanets)
    }

    fun updateToWhenPageIsChanged(currentPage: Int): FindFalconeViewState {
        if(planets.isEmpty())
            return this
        val currentPlanet = planets[currentPage]
        return copy(
            currentPlanet = planets[currentPage],
            vehiclesForCurrentPlanet = vehicles.map {
                it.copy(
                    isSelected = selectedPairs.contains(Pair(currentPlanet, it))
                )
            }
        )
    }

    fun updateToPlanetSelected(isSelected: Boolean, selectedIndex: Int): FindFalconeViewState{
        val currentPlanet = planets[selectedIndex].copy(isSelected = true)
        val vehiclesForPlanet = vehicles.filter { it.range >= currentPlanet.distance }
        val planetsUpdated = planets.map { if(it.name == currentPlanet.name) currentPlanet else it }
        return copy(
            planets = planetsUpdated,
            currentPlanet = currentPlanet,
            vehiclesForCurrentPlanet = vehiclesForPlanet,
            showVehicles = isSelected
        )
    }

    fun updateToVehicleSelected(selectedVehicle: UIVehicle): FindFalconeViewState{
        val updatedVehicles = vehicles.map {
            if ( it.name == selectedVehicle.name )
            selectedVehicle.copy(remainingQuantity = it.remainingQuantity - 1)
            else it
        }
        return copy(
            vehicles = updatedVehicles,
            selectedPairs = selectedPairs.toMutableList().apply { add( Pair(currentPlanet, selectedVehicle)) },
            vehiclesForCurrentPlanet = updatedVehicles.filter { it.range >= currentPlanet.distance }
        )
    }

}