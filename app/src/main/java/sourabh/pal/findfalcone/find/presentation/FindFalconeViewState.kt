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
        UIPlanet("Jebing", 334),
        UIPlanet("Sapir", 434),
        UIPlanet("Lerbin", 534),
        UIPlanet("Pingasor",580)
    ),
    val vehicles: List<UIVehicle> = listOf(
        UIVehicle("Space pod", 2, 200,2),
        UIVehicle("Space rocket", 1, 300,4),
        UIVehicle("Space shuttle", 1, 400,5),
        UIVehicle("Space ship", 2, 600,10)
    ),
    val vehiclesForSelectedPlanet: VehiclesForPlanet = VehiclesForPlanet(vehicles = vehicles),
    val failure: Event<Throwable>? = null
){
    val numberOfSelectedPlanets get() = planets.filter { it.isSelected }.size
}