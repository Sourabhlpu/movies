package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.domain.MaxPlanetSelected
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

data class FindFalconeViewState(
    val loading: Boolean = false,
    val planets: List<UIPlanet> = emptyList() /*listOf(
        UIPlanet("Donlon", 100),
        UIPlanet("Enchai", 234),
        UIPlanet("Jebing", 334),
        UIPlanet("Sapir", 434),
        UIPlanet("Lerbin", 534),
        UIPlanet("Pingasor", 580)
    )*/,
    val vehicles: List<UIVehicle> = emptyList() /*listOf(
        UIVehicle("Space pod", 2, 200, 2),
        UIVehicle("Space rocket", 1, 300, 4),
        UIVehicle("Space shuttle", 1, 400, 5),
        UIVehicle("Space ship", 2, 600, 10)
    )*/,
    val vehiclesForSelectedPlanet: VehiclesForPlanet = VehiclesForPlanet(vehicles = vehicles),
    val showVehicles: Boolean = false,
    val selectedVehicleForPlanet: List<Pair<UIPlanet, UIVehicle>> = emptyList(),
    val failure: Event<Throwable>? = null
) {
    val numberOfSelectedPlanets get() = planets.filter { it.isSelected }.size

    fun updateWhenPlanetsPageChanged(currentPage: Int): FindFalconeViewState {
        if(planets.isEmpty())
            return this
        val currentPlanet = planets[currentPage]
        val updatedVehicles =
            vehicles.map {
                if (it.selectedFor.contains(currentPlanet)) it.copy(isSelected = true) else it.copy(
                    isSelected = false
                )
            }
        return copy(
            showVehicles = currentPlanet.isSelected,
            vehiclesForSelectedPlanet = VehiclesForPlanet(currentPlanet, updatedVehicles),
            vehicles = updatedVehicles
        )
    }

    fun updateToAllPlanetsSelected(): FindFalconeViewState {
        return copy(
            failure = Event(MaxPlanetSelected())
        )
    }

    fun updateToPlanetSelected(isSelected: Boolean, selectedIndex: Int): FindFalconeViewState {
        val updatedPlanets = getPlanetsListAfterSelection(isSelected, selectedIndex)
        return copy(
            planets = updatedPlanets,
            showVehicles = isSelected,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[selectedIndex], vehicles)
        )
    }

    fun updateToVehicleSelected(
        currentSelectedPlanetsPage: Int,
        vehicle: UIVehicle
    ): FindFalconeViewState {

        val updatedVehicles = getUpdatedVehiclesList(currentSelectedPlanetsPage, vehicle)
        return copy(
            vehicles = updatedVehicles,
            vehiclesForSelectedPlanet = VehiclesForPlanet(
                planets[currentSelectedPlanetsPage],
                vehicles = updatedVehicles
            )
        )
    }

    private fun getUpdatedVehiclesList(
        currentSelectedPlanetsPage: Int,
        vehicle: UIVehicle
    ): List<UIVehicle> {

        val currentSelectedPlanet = planets[currentSelectedPlanetsPage]
        val isVehicleSelectedForPlanet = !vehicle.isSelected(currentSelectedPlanet)
        val updatedVehicle = vehicle.copy(
            remainingQuantity = getRemainingQuantityForVehicle(
                currentSelectedPlanetsPage,
                vehicle
            ).coerceAtLeast(0),
            isSelected = isVehicleSelectedForPlanet,
            selectedFor = getPlanetsSelectedForVehicle(
                vehicle,
                isVehicleSelectedForPlanet,
                currentSelectedPlanet
            )
        )
        return vehicles.map { if (it.name == updatedVehicle.name) updatedVehicle else it }
    }

    private fun getPlanetsSelectedForVehicle(
        vehicle: UIVehicle,
        isSelected: Boolean,
        currentSelectedPlanet: UIPlanet
    ): List<UIPlanet> {
        val selectedFor = vehicle.selectedFor
        val updatedSelectedFor =
            if (isSelected) {
                selectedFor.toMutableList().apply { add(currentSelectedPlanet) }.toList()
            } else {
                selectedFor.toMutableList().apply { remove(currentSelectedPlanet) }
            }
        return updatedSelectedFor
    }

    private fun getRemainingQuantityForVehicle(
        currentSelectedPlanetsPage: Int,
        vehicle: UIVehicle
    ): Int {
        val currentSelectedPlanet = planets[currentSelectedPlanetsPage]
        val isVehicleSelectedForPlanet = !vehicle.isSelected(currentSelectedPlanet)
        return if (isVehicleSelectedForPlanet) vehicle.remainingQuantity - 1 else vehicle.remainingQuantity + 1
    }

    private fun getPlanetsListAfterSelection(
        isSelected: Boolean,
        selectedIndex: Int
    ): List<UIPlanet> {
        return planets.mapIndexed { index, uiPlanet ->
            if (index == selectedIndex)
                uiPlanet.copy(isSelected = isSelected)
            else
                uiPlanet
        }
    }
}