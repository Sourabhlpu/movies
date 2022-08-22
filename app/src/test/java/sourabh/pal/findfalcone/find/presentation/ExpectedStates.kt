package sourabh.pal.findfalcone.find.presentation

import org.mockito.internal.matchers.Find
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

object ExpectedStates {


    fun whenPlanetIsSelected(): FindFalconeViewState {
        val updatedPlanets = listOf(
            UIPlanet("Donlon", distance = 100, isSelected = true),
            UIPlanet("Enchai", distance = 200, isSelected = false)
        )
        val updatedVehicles = listOf(
            UIVehicle(
                name = "space pod",
                quantity = 2,
                range = 150,
                speed = 2,
                remainingQuantity = 2,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),
            UIVehicle(
                name = "space rocket",
                quantity = 1,
                range = 200,
                speed = 4,
                remainingQuantity = 1,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),

            )
        return FindFalconeViewState(
            planets = updatedPlanets,
            vehicles = updatedVehicles,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[0], updatedVehicles),
            showVehicles = true
        )
    }

    fun whenPlanetIsUnSelected(): FindFalconeViewState {
        val updatedPlanets = listOf(
            UIPlanet("Donlon", distance = 100, isSelected = false),
            UIPlanet("Enchai", distance = 200, isSelected = false)
        )
        val updatedVehicles = listOf(
            UIVehicle(
                name = "space pod",
                quantity = 2,
                range = 150,
                speed = 2,
                remainingQuantity = 2,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),
            UIVehicle(
                name = "space rocket",
                quantity = 1,
                range = 200,
                speed = 4,
                remainingQuantity = 1,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),

            )
        return FindFalconeViewState(
            planets = updatedPlanets,
            vehicles = updatedVehicles,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[0], updatedVehicles),
            showVehicles = false
        )
    }

    fun whenVehicleIsSelectedForFirstPlanet(): FindFalconeViewState {
        val updatedPlanets = listOf(
            UIPlanet("Donlon", distance = 100, isSelected = true),
            UIPlanet("Enchai", distance = 200, isSelected = false)
        )
        val updatedVehicles = listOf(
            UIVehicle(
                name = "space pod",
                quantity = 2,
                range = 150,
                speed = 2,
                remainingQuantity = 1,
                enable = true,
                isSelected = true,
                selectedFor = listOf(updatedPlanets[0])
            ),
            UIVehicle(
                name = "space rocket",
                quantity = 1,
                range = 200,
                speed = 4,
                remainingQuantity = 1,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),

            )
        return FindFalconeViewState(
            planets = updatedPlanets,
            vehicles = updatedVehicles,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[0], updatedVehicles),
            showVehicles = true
        )
    }

    fun whenPlanetUnselectedAndMovedToSecondPlanetAndSelectedIt(): FindFalconeViewState{
        val updatedPlanets = listOf(
            UIPlanet("Donlon", distance = 100, isSelected = false),
            UIPlanet("Enchai", distance = 200, isSelected = true)
        )
        val updatedVehicles = listOf(
            UIVehicle(
                name = "space pod",
                quantity = 2,
                range = 150,
                speed = 2,
                remainingQuantity = 2,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),
            UIVehicle(
                name = "space rocket",
                quantity = 1,
                range = 200,
                speed = 4,
                remainingQuantity = 1,
                enable = true,
                isSelected = false,
                selectedFor = emptyList()
            ),

            )
        return FindFalconeViewState(
            planets = updatedPlanets,
            vehicles = updatedVehicles,
            vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[1], updatedVehicles),
            showVehicles = true
        )
    }
}