package sourabh.pal.findfalcone.find.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails

data class FindFalconeViewState(
    val loading: Boolean = false,
    val planets: List<UIPlanet> = emptyList(),
    val vehicles: List<UIVehicle> = emptyList(),
    val selectedPairs: List<Pair<UIPlanet, UIVehicle>> = emptyList(),
    val currentPlanet: UIPlanet = UIPlanet(),
    val vehiclesForCurrentPlanet: List<UIVehicleWitDetails> = emptyList(),
    val showVehicles: Boolean = false,
    val failure: Event<Throwable>? = null
) {

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
            currentPlanet = planets[currentPage],
            vehiclesForCurrentPlanet = vehiclesForCurrentPlanet.map {
                it.copy(
                    isSelected = selectedPairs.contains(Pair(currentPlanet, it.vehicle))
                )
            }
        )
    }

    fun updateToPlanetSelected(selectedIndex: Int): FindFalconeViewState {
        val currentPlanet = planets[selectedIndex].copy(isSelected = true)
        val vehiclesForPlanet = vehiclesForCurrentPlanet.filter { it.vehicle.range >= currentPlanet.distance }
        val planetsUpdated =
            planets.map { if (it.name == currentPlanet.name) currentPlanet else it }
        return copy(
            planets = planetsUpdated,
            currentPlanet = currentPlanet,
            vehiclesForCurrentPlanet = vehiclesForPlanet,
            showVehicles = true
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateToPlanetUnSelected(selectedIndex: Int): FindFalconeViewState {
        val currentPlanet = planets[selectedIndex].copy(isSelected = false)
        val vehiclesForPlanet = vehiclesForCurrentPlanet.filter { it.vehicle.range >= currentPlanet.distance }
        val planetsUpdated =
            planets.map { if (it.name == currentPlanet.name) currentPlanet else it }
        return copy(
            planets = planetsUpdated,
            currentPlanet = currentPlanet,
            vehiclesForCurrentPlanet = vehiclesForPlanet.map { it.copy(isSelected = false, remainingQuantity = (it.remainingQuantity + 1).coerceAtMost(it.vehicle.quantity)) },
            selectedPairs = selectedPairs.toMutableList().apply {
                removeIf { it.first == currentPlanet }
            },
            showVehicles = false
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateToVehicleSelected(selectedVehicle: UIVehicleWitDetails): FindFalconeViewState {
        var selectedVehicleUpdated = selectedVehicle
        val updatedVehicles = vehiclesForCurrentPlanet.map {
            if (it.vehicle.name == selectedVehicle.vehicle.name) {
                selectedVehicleUpdated =
                    selectedVehicle.copy(remainingQuantity = (it.remainingQuantity - 1).coerceAtLeast(0), isSelected = true)
                selectedVehicleUpdated
            } else it.copy(isSelected = false, remainingQuantity = (it.remainingQuantity + 1).coerceAtMost(it.vehicle.quantity))
        }
        return copy(
            selectedPairs = selectedPairs.toMutableList()
                .apply {
                    removeIf { it.first.name == currentPlanet.name }
                    add(Pair(currentPlanet, selectedVehicleUpdated.vehicle))
                       },
            vehiclesForCurrentPlanet = updatedVehicles.filter { it.vehicle.range >= currentPlanet.distance }
        )
    }

    fun updateToVehicleUnSelected(selectedVehicle: UIVehicleWitDetails): FindFalconeViewState {
        var selectedVehicleUpdated = selectedVehicle
        val updatedVehicles = vehiclesForCurrentPlanet.map {
            if (it.vehicle.name == selectedVehicle.vehicle.name) {
                selectedVehicleUpdated =
                    selectedVehicle.copy(remainingQuantity = (it.remainingQuantity + 1).coerceAtMost(it.vehicle.quantity), isSelected = false)
                selectedVehicleUpdated
            } else it
        }
        return copy(
            selectedPairs = selectedPairs.toMutableList()
                .apply { remove(Pair(currentPlanet, selectedVehicleUpdated.vehicle)) },
            vehiclesForCurrentPlanet = updatedVehicles.filter { it.vehicle.range >= currentPlanet.distance }
        )
    }


}