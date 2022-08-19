package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

data class FindFalconeViewState1(
    val loading: Boolean = false,
    val planets: List<UIPlanet> = emptyList(),
    val vehicles: List<UIVehicle> = emptyList(),
    val currentPlanet: UIPlanet = UIPlanet(),
    val vehiclesForCurrentPlanet: List<UIVehicle> = emptyList(),
    val showVehicles: Boolean = false,
    val failure: Event<Throwable>? = null
) {

    fun updateToVehiclesListSuccess(uiVehicles: List<UIVehicle>): FindFalconeViewState1 {
        return copy(
            loading = false,
            vehicles = uiVehicles,
            vehiclesForCurrentPlanet = uiVehicles.filter { it.range >= currentPlanet.distance }
        )
    }

    fun updateToPlanetsListSuccess(uiPlanets: List<UIPlanet>): FindFalconeViewState1 {
        return copy(loading = false, planets = uiPlanets)
    }
}