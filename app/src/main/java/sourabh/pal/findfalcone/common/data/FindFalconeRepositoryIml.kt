package sourabh.pal.findfalcone.common.data

import kotlinx.coroutines.withContext
import sourabh.pal.findfalcone.common.data.api.FindFalconeApi
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiPlanetMapper
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiVehicleMapper
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import sourabh.pal.findfalcone.common.utils.DispatchersProvider
import javax.inject.Inject

class FindFalconeRepositoryIml @Inject constructor(
    private val api: FindFalconeApi,
    private val apiVehicleMapper: ApiVehicleMapper,
    private val apiPlanetMapper: ApiPlanetMapper,
    private val ioDispatcher: DispatchersProvider
) : FindFalconeRepository {

    override suspend fun getAllVehicles(): List<Vehicle> {
        return withContext(ioDispatcher.io()) {
            val apiVehicles = api.getAllVehicles()
            apiVehicles.map { apiVehicleMapper.mapToDomain(it) }
        }
    }

    override suspend fun getAllPlanets(): List<Planet> {
        return withContext(ioDispatcher.io()) {
            val apiPlanets = api.getAllPlanets()
            apiPlanets.map { apiPlanetMapper.mapToDomain(it) }
        }
    }
}