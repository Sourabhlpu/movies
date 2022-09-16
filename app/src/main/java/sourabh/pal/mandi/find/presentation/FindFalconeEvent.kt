package sourabh.pal.mandi.find.presentation

import sourabh.pal.mandi.common.presentation.model.UIPlanet
import sourabh.pal.mandi.common.presentation.model.UIVehicle
import sourabh.pal.mandi.common.presentation.model.UIVehicleWitDetails

sealed class FindFalconeEvent {
    object GetPlanetsAndVehicles: FindFalconeEvent()
    data class PlanetSelected(val selectedIndex: Int): FindFalconeEvent()
    data class PlanetUnSelected(val selectedIndex: Int): FindFalconeEvent()
    data class OnPageSelected(val position: Int) : FindFalconeEvent()
    data class OnVehicleClicked(val vehicle: UIVehicleWitDetails) : FindFalconeEvent()
    object Submit: FindFalconeEvent()
}