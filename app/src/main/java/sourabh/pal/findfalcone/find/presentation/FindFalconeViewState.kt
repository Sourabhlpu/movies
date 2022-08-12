package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

data class FindFalconeViewState(
    val loading: Boolean = true,
    val planets: List<UIPlanet> = listOf(
        UIPlanet("Donlon", 100),
        UIPlanet("Enchai", 234),
        UIPlanet("Jebing", 234),
        UIPlanet("Sapir", 234),
        UIPlanet("Lerbin", 234),
        UIPlanet("Pingasor",234)
    ),
    val vehicles: List<UIVehicle> = listOf(
        UIVehicle("Space pod", 2, 200,2),
        UIVehicle("Space rocket", 1, 300,4),
        UIVehicle("Space shuttle", 1, 400,5),
        UIVehicle("Space ship", 2, 600,10)
    ),
    val vehiclesForSelectedPlanet: VehiclesForPlanet = VehiclesForPlanet(),
    val failure: Event<Throwable>? = null
){
    val numberOfSelectedPlanets get() = planets.filter { it.isSelected }.size
}