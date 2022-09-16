package sourabh.pal.mandi.common.domain.repositories

import sourabh.pal.mandi.common.domain.model.VehicleForPlanet
import sourabh.pal.mandi.common.domain.model.VehiclesAndPlanets
import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle

interface FindFalconeRepository {
    suspend fun getAllVehicles(): List<Vehicle>
    suspend fun getAllPlanets(): List<Planet>
    suspend fun findFalcone(vehicleForPlanet: VehiclesAndPlanets): Planet
    suspend fun getToken()
    fun getLocalToken(): String
}