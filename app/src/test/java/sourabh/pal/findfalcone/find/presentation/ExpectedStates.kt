package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.domain.MaxPlanetSelected
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet

private val planetsInitial by lazy {
    listOf(
        UIPlanet(
            name = "Donlon",
            distance = 100
        ),
        UIPlanet(
            name = "Enchai",
            distance = 200
        ),
        UIPlanet(
            name = "Jebing",
            distance = 300
        ),
        UIPlanet(
            name = "Sapir",
            distance = 400
        ),
        UIPlanet(
            name = "Lerbin",
            distance = 500
        ),
        UIPlanet(
            name = "Pingasor",
            distance = 600
        )
    )
}

private val vehiclesInitial by lazy {
    listOf(
        UIVehicle(
            "space pod",
            quantity = 2,
            range = 200,
            speed = 2,
/*                remainingQuantity = 1,
                enable = true,
                isSelected = true,
                selectedFor = listOf(planetsInitial[0])*/
        ),
        UIVehicle(
            "space rocket",
            quantity = 1,
            range = 300,
            speed = 4
        ),
        UIVehicle(
            "space shuttle",
            quantity = 1,
            range = 400,
            speed = 5
        ),
        UIVehicle(
            "space ship",
            quantity = 2,
            range = 600,
            speed = 10
        )
    )
}

fun expectedStateWhenFirstAndSecondPlanetSelectFirstPlanet(): FindFalconeViewState {
    val updatedPlanets = planetsInitial.mapIndexed { index, uiPlanet ->
        if (index in 0..1) uiPlanet.copy(
            isSelected = true
        ) else uiPlanet
    }
    val updatedVehicles = vehiclesInitial.mapIndexed { index, uiVehicle ->
        if (index == 0)
            uiVehicle.copy(
                isSelected = true,
                selectedFor = listOf(updatedPlanets[0], updatedPlanets[1]),
                remainingQuantity = uiVehicle.quantity - 2
            )
        else
            uiVehicle
    }
    return FindFalconeViewState(
        planets = updatedPlanets,
        vehicles = updatedVehicles,
        vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[1], updatedVehicles),
        showVehicles = true
    )
}

fun expectedStateWhenVehicleIsSelectedForFirstPlanet(): FindFalconeViewState {
    val updatedPlanets =
        planetsInitial.mapIndexed { index, uiPlanet -> if (index == 0) uiPlanet.copy(isSelected = true) else uiPlanet }
    val updatedVehicles = vehiclesInitial.mapIndexed { index, uiVehicle ->
        if (index == 0) uiVehicle.copy(
            remainingQuantity = uiVehicle.remainingQuantity - 1,
            isSelected = true,
            selectedFor = listOf(updatedPlanets[0])
        ) else uiVehicle
    }
    return FindFalconeViewState(
        planets = updatedPlanets,
        vehicles = updatedVehicles,
        vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[0], updatedVehicles),
        showVehicles = true
    )
}

fun expectedStateWhenVehicleIsSelectedForFirstPlanetAndThenSecondPlanetIsVisible(): FindFalconeViewState {
    val updatedPlanets =
        planetsInitial.mapIndexed { index, uiPlanet ->
            if (index == 1 || index == 0) uiPlanet.copy(
                isSelected = true
            ) else uiPlanet
        }
    val updatedVehicles = vehiclesInitial.mapIndexed { index, uiVehicle ->
        if (index == 0) uiVehicle.copy(
            remainingQuantity = uiVehicle.remainingQuantity - 1,
            isSelected = false,
            selectedFor = listOf(updatedPlanets[0])
        ) else uiVehicle
    }
    return FindFalconeViewState(
        planets = updatedPlanets,
        vehicles = updatedVehicles,
        vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[1], updatedVehicles),
        showVehicles = true
    )
}

fun expectedStateWhenSameVehicleIsSelectedForFirstTwoPlanets(): FindFalconeViewState {
    val updatedPlanets =
        planetsInitial.mapIndexed { index, uiPlanet ->
            if (index == 1 || index == 0) uiPlanet.copy(
                isSelected = true
            ) else uiPlanet
        }
    val updatedVehicles = vehiclesInitial.mapIndexed { index, uiVehicle ->
        if (index == 0) uiVehicle.copy(
            remainingQuantity = uiVehicle.remainingQuantity - 2,
            isSelected = true,
            selectedFor = listOf(updatedPlanets[0], updatedPlanets[1])
        ) else uiVehicle
    }
    return FindFalconeViewState(
        planets = updatedPlanets,
        vehicles = updatedVehicles,
        vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[1], updatedVehicles),
        showVehicles = true
    )
}

fun expectedStateWhenAllFourPlanetsAreSelectedAndFifthIsClicked(): FindFalconeViewState {
    val updatedPlanets =
        planetsInitial.mapIndexed { index, uiPlanet -> if (index in 0..3) uiPlanet.copy(isSelected = true) else uiPlanet }
    return FindFalconeViewState(
        planets = updatedPlanets,
        vehicles = vehiclesInitial,
        vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[4], vehiclesInitial),
        failure = Event(MaxPlanetSelected())
    )
}

fun expectedStateForSingleVehicleSelectionForPlanet(): FindFalconeViewState {
    val updatedPlanets =
        planetsInitial.mapIndexed { index, uiPlanet -> if (index == 0) uiPlanet.copy(isSelected = true) else uiPlanet }
    val updatedVehicles = vehiclesInitial.mapIndexed { index, uiVehicle ->
        if (index == 1) uiVehicle.copy(
            isSelected = true,
            selectedFor = listOf(updatedPlanets[0]),
            remainingQuantity = uiVehicle.remainingQuantity - 1
        )
        else
            uiVehicle
    }
    return FindFalconeViewState(
        planets = updatedPlanets,
        vehicles = updatedVehicles,
        vehiclesForSelectedPlanet = VehiclesForPlanet(updatedPlanets[0], updatedVehicles)
    )
}