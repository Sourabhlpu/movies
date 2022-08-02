package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.model.UIPlanet

sealed class FindFalconeEvent {
    object GetPlanetsAndVehicles: FindFalconeEvent()
    data class PlanetSelected(val planet: String, val selectedIndex: Int): FindFalconeEvent()
    object Submit: FindFalconeEvent()
}