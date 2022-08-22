package sourabh.pal.findfalcone.find.presentation

import androidx.annotation.VisibleForTesting
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
        val updatedVehicles =
            vehicles.map {
                it.copy(isSelected = it.isSelectedForPlanet(currentPlanet))
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
        val currentPlanet = planets[selectedIndex]
        val updatedVehicles = getVehiclesWhenPlanetIsSelected(isSelected, currentPlanet)
        val updatedPlanets = getPlanetsListAfterSelection(isSelected, selectedIndex)
        return copy(
            planets = updatedPlanets,
            vehicles = updatedVehicles,
            showVehicles = isSelected,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[selectedIndex], updatedVehicles)
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


    /*
      ********************** UTILITY METHODS ************************
     */

    private fun getUpdatedVehiclesList(
        currentPlanet: UIPlanet,
        selectedVehicle: UIVehicle
    ): List<UIVehicle> {
        val updatedVehicle = getUpdatedVehicleAfterSelection(selectedVehicle, currentPlanet)
        val resetVehicles = resetVehiclesList(currentPlanet)
        return resetVehicles.map { if (it == selectedVehicle) updatedVehicle else it }
    }

    private fun getUpdatedVehicleAfterSelection(
        selectedVehicle: UIVehicle,
        currentPlanet: UIPlanet
    ): UIVehicle {
        val isSelected = !selectedVehicle.isSelectedForPlanet(currentPlanet)
        val updatedSelectedFor: List<UIPlanet> = if (isSelected)
            selectedVehicle.selectedFor.toMutableList().apply { add(currentPlanet) }
        else
            selectedVehicle.selectedFor.toMutableList().apply { remove(currentPlanet) }
        val remainingQuantity =
            if (isSelected) selectedVehicle.remainingQuantity - 1 else selectedVehicle.quantity + 1

        return selectedVehicle.copy(
            isSelected = !selectedVehicle.isSelected,
            selectedFor = updatedSelectedFor,
            remainingQuantity = remainingQuantity.coerceIn(0, selectedVehicle.quantity)
        )
    }

    private fun resetVehiclesList(currentPlanet: UIPlanet): List<UIVehicle> {
        val resetVehicles = vehicles.map {
            val isSelectedPreviously = it.isSelectedForPlanet(currentPlanet)
            val remainingQuantityUpdated =
                if (isSelectedPreviously) it.remainingQuantity + 1 else it.remainingQuantity
            it.copy(
                selectedFor = it.selectedFor.toMutableList().apply { remove(currentPlanet) },
                isSelected = false,
                remainingQuantity = remainingQuantityUpdated.coerceIn(0, it.quantity)
            )
        }
        return resetVehicles
    }

    private fun getVehiclesWhenPlanetIsSelected(
        isSelected: Boolean,
        currentPlanet: UIPlanet
    ): List<UIVehicle> {
        val updatedVehicles =
            if (isSelected) vehicles.map {
                it.copy(isSelected = it.isSelectedForPlanet(currentPlanet))
            }
            else vehicles.map {
                val updatedSelectedFor =
                    it.selectedFor.toMutableList().apply { remove(currentPlanet) }
                it.copy(
                    isSelected = false,
                    selectedFor = updatedSelectedFor,
                    remainingQuantity = it.quantity - updatedSelectedFor.size
                )
            }
        return updatedVehicles
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