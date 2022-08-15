package sourabh.pal.findfalcone.common.data

import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class FakeRepository @Inject constructor() : FindFalconeRepository {
    override suspend fun getAllVehicles(): List<Vehicle> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPlanets(): List<Planet> {
        TODO("Not yet implemented")
    }


}