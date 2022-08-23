package sourabh.pal.findfalcone.common.data

import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import javax.inject.Inject

class FakeRepository @Inject constructor() : FindFalconeRepository {

    var isHappyPath = true
    var sendFullList = false

    //private val vehicles: List<Vehicle> get() = listOf(Vehicle("space pod", quantity = 2, range = 400, speed = 2))

    //private val planets: List<Planet> get() = listOf(Planet("Donlon", 200))

    val planets by lazy {
        listOf(
            Planet(
                name = "Donlon",
                distance = 100
            ),
            Planet(
                name = "Enchai",
                distance = 200
            ),
            Planet(
                name = "Jebing",
                distance = 300
            ),
            Planet(
                name = "Sapir",
                distance = 400
            ),
            Planet(
                name = "Lerbin",
                distance = 500
            ),
            Planet(
                name = "Pingasor",
                distance = 600
            )
        )
    }

    val vehicles by lazy {
        listOf(
            Vehicle(
                "space pod",
                quantity = 2,
                range = 200,
                speed = 2
            ),
            Vehicle(
                "space rocket",
                quantity = 1,
                range = 300,
                speed = 4
            ),
            Vehicle(
                "space shuttle",
                quantity = 1,
                range = 400,
                speed = 5
            ),
            Vehicle(
                "space ship",
                quantity = 2,
                range = 600,
                speed = 10
            )
        )
    }

    override suspend fun getAllVehicles(): List<Vehicle> {
        return when {
            isHappyPath && !sendFullList -> vehicles.subList(0, 2)
            isHappyPath && sendFullList -> vehicles
            else -> throw NetworkException("Network Exception")
        }
    }

    override suspend fun getAllPlanets(): List<Planet> {
        return when {
            isHappyPath && !sendFullList -> planets.subList(0, 2)
            isHappyPath && sendFullList -> planets
            else -> throw NetworkException("Network Exception")
        }
    }

    override suspend fun findFalcone(vehicleForPlanet: VehiclesAndPlanets): Planet {
        return Planet("Donlon")
    }

    override suspend fun getToken() {

    }

    override fun getLocalToken(): String {
        return ""
    }

}