package sourabh.pal.mandi.common.domain.repositories

import sourabh.pal.mandi.common.domain.model.VehiclesAndPlanets
import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle

interface FindFalconeRepository {
    suspend fun getAllVehicles(): List<Vehicle>
    suspend fun getAllPlanets(): List<Planet>
    suspend fun findFalcone(vehicleForPlanet: VehiclesAndPlanets): Planet
    suspend fun getToken()
    suspend fun searchSellersByName(query: String): List<Seller>
    suspend fun getVillages(): List<Village>
    fun getLocalToken(): String
}