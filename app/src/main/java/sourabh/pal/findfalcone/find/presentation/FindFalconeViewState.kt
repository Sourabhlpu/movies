package sourabh.pal.findfalcone.find.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import sourabh.pal.findfalcone.common.domain.MaxPlanetSelected
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails

data class FindFalconeViewState(
    val loading: Boolean = false,
    val planets: List<UIPlanet> = emptyList(),
    val vehicles: List<UIVehicle> = emptyList(),
    val selectedPairs: Map<UIPlanet, UIVehicle> = emptyMap(),
    val currentPlanet: UIPlanet = UIPlanet(),
    val vehiclesForCurrentPlanet: List<UIVehicleWitDetails> = emptyList(),
    val showVehicles: Boolean = false,
    val enableButton: Boolean = false,
    val totalTime: String = "0",
    val failure: Event<Throwable>? = null
) {

    val numberOfSelectedPlanets get() = selectedPairs.size

    fun updateToVehiclesListSuccess(uiVehicles: List<UIVehicle>): FindFalconeViewState {
        return copy(
            loading = false,
            vehicles = uiVehicles,
            vehiclesForCurrentPlanet = uiVehicles.map { UIVehicleWitDetails(vehicle = it) }
        )
    }

    fun updateToPlanetsListSuccess(uiPlanets: List<UIPlanet>): FindFalconeViewState {
        return copy(loading = false, planets = uiPlanets)
    }

    fun updateToWhenPageIsChanged(currentPage: Int): FindFalconeViewState {
        if (planets.isEmpty())
            return this
        val currentPlanet = planets[currentPage]
        return copy(
            currentPlanet = currentPlanet,
            vehiclesForCurrentPlanet = getVehiclesWhenPageIsChanged(currentPlanet),
            showVehicles = currentPlanet.isSelected
        )
    }

    fun updateToPlanetSelected(selectedIndex: Int): FindFalconeViewState {
        val currentPlanet = planets[selectedIndex].copy(isSelected = true)
        val vehiclesForPlanet =
            vehiclesForCurrentPlanet.map { it.copy(enable = it.vehicle.range >= currentPlanet.distance && it.remainingQuantity > 0) }
        val planetsUpdated = planets.map { if (it.name == currentPlanet.name) currentPlanet else it }
        return copy(
            planets = planetsUpdated,
            currentPlanet = currentPlanet,
            vehiclesForCurrentPlanet = vehiclesForPlanet,
            showVehicles = true,
            enableButton = false
        )
    }


    fun updateToPlanetUnSelected(selectedIndex: Int): FindFalconeViewState {
        val currentPlanet = planets[selectedIndex].copy(isSelected = false)
        val planetsUpdated = planets.map { if (it.name == currentPlanet.name) currentPlanet else it }
        val updatedSelectedPair = selectedPairs.toMutableMap().apply { remove(this@FindFalconeViewState.currentPlanet) }
        val totalTime = getTotalTime(updatedSelectedPair.toMap())
        return copy(
            planets = planetsUpdated,
            currentPlanet = currentPlanet,
            selectedPairs = updatedSelectedPair,
            vehiclesForCurrentPlanet = getVehiclesWhenPlanetIsUnselected(),
            showVehicles = false,
            totalTime = totalTime.toString(),
            enableButton = updatedSelectedPair.size == MAX_NO_OF_PLANETS_TO_BE_SELECTED
        )
    }



    fun updateToVehicleSelected(vehicle: UIVehicleWitDetails): FindFalconeViewState {
        val updatedVehicles = getVehiclesWhenVehicleIsSelected(vehicle)
        val updatedSelectedPair = selectedPairs.toMutableMap().apply { put(currentPlanet, vehicle.vehicle) }
        val totalTime = getTotalTime(updatedSelectedPair.toMap())
        return copy(
            selectedPairs = updatedSelectedPair,
            vehiclesForCurrentPlanet = updatedVehicles,
            totalTime = totalTime.toString(),
            enableButton = updatedSelectedPair.size == MAX_NO_OF_PLANETS_TO_BE_SELECTED
        )
    }

    fun updateToVehicleUnSelected(vehicle: UIVehicleWitDetails): FindFalconeViewState {
        val updatedVehicles = getVehiclesWhenVehicleIsUnselected(vehicle)
        val updatedSelectedPair = selectedPairs.toMutableMap().apply { remove(currentPlanet) }
        val totalTime = getTotalTime(updatedSelectedPair.toMap())
        return copy(
            vehiclesForCurrentPlanet = updatedVehicles,
            selectedPairs = selectedPairs.toMutableMap().apply { remove(currentPlanet) },
            totalTime = totalTime.toString(),
            enableButton = updatedSelectedPair.size == MAX_NO_OF_PLANETS_TO_BE_SELECTED
        )
    }

    fun updateToAllPlanetsSelected(): FindFalconeViewState {
        return copy(
            failure = Event(MaxPlanetSelected())
        )
    }

    fun updateToFailure(throwable: Throwable): FindFalconeViewState{
        return copy(
            failure = Event(throwable)
        )
    }


    /*
      **************************** Utility Methods ****************************************
     */
    private fun getVehiclesWhenVehicleIsUnselected(vehicle: UIVehicleWitDetails): List<UIVehicleWitDetails> {
        val vehicleUpdated = vehicle.copy(
            isSelected = false,
            remainingQuantity = (vehicle.remainingQuantity + 1).coerceAtMost(vehicle.vehicle.quantity)
        )
        val updatedVehicles = vehiclesForCurrentPlanet.map {
            if (it.vehicle.name == vehicleUpdated.vehicle.name) {
                vehicleUpdated
            } else it
        }
        return updatedVehicles
    }

    private fun getVehiclesWhenPlanetIsUnselected() = vehiclesForCurrentPlanet.map {
        if (it.isSelected)
            it.copy(
                isSelected = false,
                remainingQuantity = (it.remainingQuantity + 1).coerceAtMost(it.vehicle.quantity)
            )
        else it
    }

    private fun getVehiclesWhenVehicleIsSelected(vehicle: UIVehicleWitDetails): List<UIVehicleWitDetails> {
        val selectedVehicle = vehicle.copy(isSelected = true, remainingQuantity = (vehicle.remainingQuantity - 1).coerceAtLeast(0))
        return vehiclesForCurrentPlanet.map {
            if (it.vehicle.name == selectedVehicle.vehicle.name) {
                selectedVehicle
            } else it.copy(
                isSelected = false,
                remainingQuantity = if (it.isSelected) (it.remainingQuantity + 1).coerceAtMost(it.vehicle.quantity) else it.remainingQuantity
            )
        }
    }

    private fun getVehiclesWhenPageIsChanged(currentPlanet: UIPlanet) =
        vehiclesForCurrentPlanet.map {
            val updatedIsSelected = checkIfVehicleAndPlanetIsPaired(it.vehicle, currentPlanet)
            it.copy(
                isSelected = updatedIsSelected,
                enable = updatedIsSelected || it.remainingQuantity > 0
            )
        }

    private fun checkIfVehicleAndPlanetIsPaired(vehicle: UIVehicle, planet: UIPlanet): Boolean {
        if (selectedPairs.containsKey(planet)) {
            return selectedPairs[planet]?.name == vehicle.name
        }
        return false
    }

    private fun getTotalTime(selectedItems: Map<UIPlanet, UIVehicle>): Int{
        var totalTime = 0
        selectedItems.forEach { (uiPlanet, uiVehicle) ->
            check(uiVehicle.speed > 0) {
                "Vehicle speed cannot be zero or less"
            }
            totalTime += uiPlanet.distance / uiVehicle.speed
        }
        return totalTime
    }
}