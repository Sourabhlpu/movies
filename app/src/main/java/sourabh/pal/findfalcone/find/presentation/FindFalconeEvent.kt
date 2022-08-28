package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails

sealed class FindFalconeEvent {
    object GetPlanetsAndVehicles: FindFalconeEvent()
    data class PlanetSelected(val selectedIndex: Int): FindFalconeEvent()
    data class PlanetUnSelected(val selectedIndex: Int): FindFalconeEvent()
    data class OnPageSelected(val position: Int) : FindFalconeEvent()
    data class OnVehicleClicked(val vehicle: UIVehicleWitDetails) : FindFalconeEvent()
    object Submit: FindFalconeEvent()
}