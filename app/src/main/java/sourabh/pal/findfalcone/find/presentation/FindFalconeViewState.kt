package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.domain.MaxPlanetSelected
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

data class FindFalconeViewState(
    val loading: Boolean = false,
    val planets: List<UIPlanet> = emptyList(),
    val vehicles: List<UIVehicle> = emptyList(),
    val vehiclesForSelectedPlanet: VehiclesForPlanet = VehiclesForPlanet(vehicles = vehicles),
    val currentPlanet: UIPlanet = UIPlanet(),
    val vehiclesForCurrentPlanet: List<UIVehicle> = emptyList(),
    val showVehicles: Boolean = false,
    val failure: Event<Throwable>? = null
) {
    val numberOfSelectedPlanets get() = planets.filter { it.isSelected }.size

    fun updateWhenPlanetsPageChanged(currentPage: Int): FindFalconeViewState {
        if (planets.isEmpty())
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

    fun updateToVehiclesListSuccess(uiVehicles: List<UIVehicle>): FindFalconeViewState {
        return copy(
            loading = false,
            vehicles = uiVehicles,
            vehiclesForSelectedPlanet = VehiclesForPlanet(vehicles = uiVehicles)
        )
    }

    fun updateToPlanetsListSuccess(uiPlanets: List<UIPlanet>): FindFalconeViewState {
        return copy(loading = false, planets = uiPlanets)
    }

    private fun getUpdatedVehiclesList(
        currentSelectedPlanetsPage: Int,
        vehicle: UIVehicle
    ): List<UIVehicle> {
        val currentSelectedPlanet = planets[currentSelectedPlanetsPage]
        //val resetVehicles = resetVehicleSelectionForPlanet(currentSelectedPlanet)
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
        return vehicles
            .map { if (it.name == updatedVehicle.name) updatedVehicle else it }
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

    private fun resetVehicleSelectionForPlanet(
        uiPlanet: UIPlanet,
    ): List<UIVehicle> {
        return vehicles.map {
            val updatedSelectedFor = it.selectedFor.toMutableList().apply {
                remove(uiPlanet)
            }.toList()
            it.copy(
                selectedFor = updatedSelectedFor,
                remainingQuantity = it.quantity - updatedSelectedFor.size,
                isSelected = false,
                enable = it.selectedFor.isNotEmpty() && it.remainingQuantity > 0
            )
        }
    }
}