package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle

sealed class FindFalconeEvent {
    object GetPlanets: FindFalconeEvent()
    object GetVehicles: FindFalconeEvent()
    data class PlanetSelected(val isSelected: Boolean = false, val selectedIndex: Int): FindFalconeEvent()
    data class OnPageSelected(val position: Int) : FindFalconeEvent()
    data class OnPlanetClicked(val vehicle: UIVehicle) : FindFalconeEvent()
    object Submit: FindFalconeEvent()
}