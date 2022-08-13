package sourabh.pal.findfalcone.common.domain.repositories

import sourabh.pal.findfalcone.common.domain.model.planets.Planet

import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle

interface FindFalconeRepository {
    suspend fun getAllVehicles(): List<Vehicle>
    suspend fun getAllPlanets(): List<Planet>
}