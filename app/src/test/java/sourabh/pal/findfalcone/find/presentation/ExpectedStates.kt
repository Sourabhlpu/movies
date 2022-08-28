package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails

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
            speed = 2
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

fun expectedStateWhenPageIsVisible(index: Int): FindFalconeViewState {
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 150, speed = 2),
        UIVehicle("space rocket", quantity = 1, range = 200, speed = 4)
    )

    val expectedVehiclesForPlanet = listOf(
        UIVehicleWitDetails(vehicle = expectedVehicles[0], enable = true, remainingQuantity = 2),
        UIVehicleWitDetails(vehicle = expectedVehicles[1], enable = true, remainingQuantity = 1)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[index],
        vehiclesForCurrentPlanet = expectedVehiclesForPlanet
    )
}

fun expectedStateWhenPlanetIsSelected(): FindFalconeViewState {
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100, isSelected = true),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 150, speed = 2),
        UIVehicle("space rocket", quantity = 1, range = 200, speed = 4)
    )

    val expectedVehiclesForPlanet = listOf(
        UIVehicleWitDetails(vehicle = expectedVehicles[0], enable = true),
        UIVehicleWitDetails(vehicle = expectedVehicles[1], enable = true)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[0],
        vehiclesForCurrentPlanet = expectedVehiclesForPlanet,
        showVehicles = true
    )
}

fun expectedStateWhenVehicleIsSelected(): FindFalconeViewState {
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100, isSelected = true),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 150, speed = 2),
        UIVehicle("space rocket", quantity = 1, range = 200, speed = 4)
    )

    val expectedVehiclesForPlanet = listOf(
        UIVehicleWitDetails(
            vehicle = expectedVehicles[0],
            enable = true,
            isSelected = true,
            remainingQuantity = 1
        ),
        UIVehicleWitDetails(vehicle = expectedVehicles[1], enable = true, isSelected = false, remainingQuantity = 1)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[0],
        vehiclesForCurrentPlanet = expectedVehiclesForPlanet,
        totalTime = "50",
        selectedPairs = mapOf(Pair(expectedPlanets[0], expectedVehicles[0])),
        showVehicles = true
    )
}

fun expectedStateWhenSecondVehicleIsSelected(): FindFalconeViewState {
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100, isSelected = true),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 150, speed = 2),
        UIVehicle("space rocket", quantity = 1, range = 200, speed = 4)
    )

    val expectedVehiclesForPlanet = listOf(
        UIVehicleWitDetails(
            vehicle = expectedVehicles[0],
            enable = true,
            isSelected = false,
            remainingQuantity = 2
        ),
        UIVehicleWitDetails(vehicle = expectedVehicles[1], enable = true, isSelected = true, remainingQuantity = 0)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[0],
        vehiclesForCurrentPlanet = expectedVehiclesForPlanet,
        totalTime = "25",
        selectedPairs = mapOf(Pair(expectedPlanets[0], expectedVehicles[1])),
        showVehicles = true
    )
}

fun expectedStateWhenAllPlanetsAndVehiclesAreSelected(): FindFalconeViewState {
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100, isSelected = true),
        UIPlanet(name = "Enchai", distance = 200, isSelected = true),
        UIPlanet(name = "Jebing", distance = 300, isSelected = true),
        UIPlanet(name = "Sapir", distance = 400, isSelected = true),
        UIPlanet(name = "Lerbin", distance = 500),
        UIPlanet(name = "Pingasor", distance = 600)
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 150, speed = 2),
        UIVehicle("space rocket", quantity = 1, range = 200, speed = 4),
        UIVehicle("space shuttle", quantity = 1, range = 400, speed = 5),
        UIVehicle("space ship", quantity = 2, range = 600, speed = 10)
    )

    val expectedVehiclesForPlanet = listOf(
        UIVehicleWitDetails(vehicle = expectedVehicles[0], enable = false, isSelected = false, remainingQuantity = 1),
        UIVehicleWitDetails(vehicle = expectedVehicles[1], enable = false, isSelected = false, remainingQuantity = 0),
        UIVehicleWitDetails(vehicle = expectedVehicles[2], enable = false, isSelected = false, remainingQuantity = 0),
        UIVehicleWitDetails(vehicle = expectedVehicles[3], enable = true, isSelected = true, remainingQuantity = 1),

    )

    val selectedPairs = mapOf(
        Pair(expectedPlanets[0], expectedVehicles[0]),
        Pair(expectedPlanets[1], expectedVehicles[1]),
        Pair(expectedPlanets[2], expectedVehicles[2]),
        Pair(expectedPlanets[3], expectedVehicles[3]),
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[3],
        vehiclesForCurrentPlanet = expectedVehiclesForPlanet,
        selectedPairs = selectedPairs,
        totalTime = "200",
        showVehicles = true,
        enableButton = true,
        navigateToSuccess = Event(Pair("Donlon", "200"))
    )
}