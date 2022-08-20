package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

object ExpectedStates {


    fun whenPlanetIsSelected(
        currentVehicles: List<UIVehicle>,
        currentPlanets: List<UIPlanet>,
        selectedPlanetIndex: Int,
        isChecked: Boolean = true
    ): FindFalconeViewState {
        val updatedPlanets = currentPlanets.mapIndexed { index, uiPlanet ->
            if (index == selectedPlanetIndex) {
                uiPlanet.copy(isSelected = isChecked)
            } else
                uiPlanet
        }

        return FindFalconeViewState(
            vehicles = currentVehicles,
            planets = updatedPlanets,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[selectedPlanetIndex], currentVehicles),
            showVehicles = updatedPlanets[selectedPlanetIndex].isSelected
        )
    }
}