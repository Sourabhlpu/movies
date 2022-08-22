package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.domain.MaxPlanetSelected
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet
import kotlin.math.exp

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

fun expectedStateWhenPageIsVisible(index: Int): FindFalconeViewState{
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 200, speed = 2, enable = true),
        UIVehicle("space rocket", quantity = 1, range = 300, speed = 4, enable = true)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[index],
        vehiclesForCurrentPlanet = expectedVehicles
    )
}

fun expectedStateWhenPlanetIsSelected(): FindFalconeViewState{
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100, isSelected = true),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 200, speed = 2, enable = true),
        UIVehicle("space rocket", quantity = 1, range = 300, speed = 4, enable = true)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[0],
        vehiclesForCurrentPlanet = expectedVehicles,
        showVehicles = true
    )
}

fun expectedStateWhenVehicleIsSelected(): FindFalconeViewState{
    val expectedPlanets = listOf(
        UIPlanet(name = "Donlon", distance = 100, isSelected = true),
        UIPlanet(name = "Enchai", distance = 200),
    )
    val expectedVehicles = listOf(
        UIVehicle("space pod", quantity = 2, range = 200, speed = 2, enable = true, isSelected = true),
        UIVehicle("space rocket", quantity = 1, range = 300, speed = 4, enable = true)
    )

    return FindFalconeViewState(
        planets = expectedPlanets,
        vehicles = expectedVehicles,
        currentPlanet = expectedPlanets[0],
        vehiclesForCurrentPlanet = expectedVehicles,
        selectedPairs = listOf(Pair(expectedPlanets[0], expectedVehicles[0])),
        showVehicles = true
    )
}