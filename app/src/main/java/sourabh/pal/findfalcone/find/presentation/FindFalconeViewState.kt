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
    val showVehicles: Boolean = false,
    val failure: Event<Throwable>? = null
) {
    val numberOfSelectedPlanets get() = planets.filter { it.isSelected }.size

    fun updateWhenPlanetsPageChanged(currentPage: Int): FindFalconeViewState {
        if (planets.isEmpty())
            return this
        val currentPlanet = planets[currentPage]
        return copy(
            showVehicles = currentPlanet.isSelected,
            vehiclesForSelectedPlanet = VehiclesForPlanet(currentPlanet, vehicles),
            vehicles = vehicles
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
        val currentPlanet = planets[currentSelectedPlanetsPage]
        val updatedVehicles = getUpdatedVehiclesList(currentPlanet, vehicle)
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
        currentPlanet: UIPlanet,
        selectedVehicle: UIVehicle
    ): List<UIVehicle> {

        val isSelected = !selectedVehicle.isSelected(currentPlanet)
        val updatedSelectedFor: List<UIPlanet> = if (isSelected)
            selectedVehicle.selectedFor.toMutableList().apply { add(currentPlanet) }
        else
            selectedVehicle.selectedFor.toMutableList().apply { remove(currentPlanet) }
        val remainingQuantity =
            if (isSelected) selectedVehicle.remainingQuantity - 1 else selectedVehicle.quantity + 1

        val updatedVehicle = selectedVehicle.copy(
            isSelected = !selectedVehicle.isSelected,
            selectedFor = updatedSelectedFor,
            remainingQuantity = remainingQuantity.coerceIn(0, selectedVehicle.quantity)
        )

        val resetVehicles = vehicles.map {
            val isSelectedPreviously = it.isSelected(currentPlanet)
            val remainingQuantityUpdated =
                if (isSelectedPreviously) it.remainingQuantity + 1 else it.quantity
            it.copy(
                selectedFor = it.selectedFor.toMutableList().apply { remove(currentPlanet) },
                isSelected = false,
                remainingQuantity = remainingQuantityUpdated.coerceIn(0, it.quantity)
            )
        }

        return resetVehicles.map { if (it == selectedVehicle) updatedVehicle else it }


/*        val (currentSelectedPlanet, isVehicleSelectedForPlanet, updatedVehicle) = getUpdatedPlanetAfterSelection(
            currentSelectedPlanetsPage,
            vehicle
        )
        val resetVehicles = if (isVehicleSelectedForPlanet)
            removeSelectedFor(vehicles, currentSelectedPlanet)
        else
            vehicles

        return resetVehicles.map { vehicle ->
            if (vehicle.name == updatedVehicle.name)
                updatedVehicle
            else
                vehicle.copy(
                    selectedFor = vehicle.selectedFor.filter { it == currentSelectedPlanet },
                    remainingQuantity = if(vehicle.isSelected(currentSelectedPlanet)) vehicle.remainingQuantity + 1 else vehicle.remainingQuantity
                )
        }*/
    }

    private fun getUpdatedPlanetAfterSelection(
        currentSelectedPlanetsPage: Int,
        vehicle: UIVehicle
    ): Triple<UIPlanet, Boolean, UIVehicle> {
        val currentSelectedPlanet = planets[currentSelectedPlanetsPage]
        val isVehicleSelectedForPlanet = !vehicle.isSelected(currentSelectedPlanet)
        val remainingQuantity = getRemainingQuantityForVehicle(
            currentSelectedPlanetsPage,
            vehicle
        ).coerceAtLeast(0)

        val updatedVehicle = vehicle.copy(
            remainingQuantity = remainingQuantity,
            selectedFor = getPlanetsSelectedForVehicle(
                vehicle,
                isVehicleSelectedForPlanet,
                currentSelectedPlanet
            )
        )
        return Triple(currentSelectedPlanet, isVehicleSelectedForPlanet, updatedVehicle)
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

    private fun removeSelectedFor(vehicles: List<UIVehicle>, toRemove: UIPlanet): List<UIVehicle> {
        return vehicles.map { vehicle ->
            vehicle.copy(
                selectedFor = vehicle.selectedFor.dropWhile { it == toRemove }
            )
        }
    }
}