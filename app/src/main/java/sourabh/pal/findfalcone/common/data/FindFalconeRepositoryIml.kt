package sourabh.pal.findfalcone.common.data

import kotlinx.coroutines.withContext
import retrofit2.HttpException
import sourabh.pal.findfalcone.common.data.api.FindFalconeApi
import sourabh.pal.findfalcone.common.data.api.model.ApiFindFalconeRequest
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiFindFalconeResponseMapper
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiPlanetMapper
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiVehicleMapper
import sourabh.pal.findfalcone.common.data.preferences.FindFalconePreferences
import sourabh.pal.findfalcone.common.data.preferences.Preferences
import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.domain.NetworkUnavailableException
import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import sourabh.pal.findfalcone.common.utils.DispatchersProvider
import javax.inject.Inject

class FindFalconeRepositoryIml @Inject constructor(
    private val api: FindFalconeApi,
    private val apiVehicleMapper: ApiVehicleMapper,
    private val apiPlanetMapper: ApiPlanetMapper,
    private val apiFindFalconeResponseMapper: ApiFindFalconeResponseMapper,
    private val preferences: Preferences,
    private val ioDispatcher: DispatchersProvider
) : FindFalconeRepository {

    override suspend fun getAllVehicles(): List<Vehicle> {
        return try {
            withContext(ioDispatcher.io()) {
                val apiVehicles = api.getAllVehicles()
                apiVehicles.map { apiVehicleMapper.mapToDomain(it) }
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }

    override suspend fun getAllPlanets(): List<Planet> {
        return try {
            withContext(ioDispatcher.io()) {
                val apiPlanets = api.getAllPlanets()
                apiPlanets.map { apiPlanetMapper.mapToDomain(it) }
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }

    override suspend fun findFalcone(vehicleForPlanet: VehiclesAndPlanets): Planet {
        return try {
            withContext(ioDispatcher.io()) {
                val apiPlanets = api.findFalcone(
                    ApiFindFalconeRequest.fromDomain(
                        vehicleForPlanet,
                        preferences.getToken()
                    )
                )
                apiFindFalconeResponseMapper.mapToDomain(apiPlanets)
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }

    override suspend fun getToken() {
        try {
            withContext(ioDispatcher.io()) {
                val apiToken = api.getToken()
                preferences.putToken(apiToken.token)
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }

    override fun getLocalToken(): String {
        return preferences.getToken()
    }
}