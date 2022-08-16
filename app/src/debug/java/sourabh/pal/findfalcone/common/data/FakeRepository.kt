package sourabh.pal.findfalcone.common.data

import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class FakeRepository @Inject constructor() : FindFalconeRepository {

    val vehicles: List<Vehicle> get() = listOf(Vehicle("space pod", quantity = 2, range = 400, speed = 2))

    val planets: List<Planet> get() = listOf(Planet("Donlon", 200))

    override suspend fun getAllVehicles(): List<Vehicle> {
        return vehicles
    }

    override suspend fun getAllPlanets(): List<Planet> {
        return planets
    }

}