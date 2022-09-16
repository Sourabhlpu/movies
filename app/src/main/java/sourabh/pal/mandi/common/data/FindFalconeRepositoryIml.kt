package sourabh.pal.mandi.common.data

import kotlinx.coroutines.withContext
import retrofit2.HttpException
import sourabh.pal.mandi.common.data.api.ApiConstants
import sourabh.pal.mandi.common.data.api.FindFalconeApi
import sourabh.pal.mandi.common.data.api.model.ApiFindFalconeRequest
import sourabh.pal.mandi.common.data.api.model.mappers.ApiFindFalconeResponseMapper
import sourabh.pal.mandi.common.data.api.model.mappers.ApiPlanetMapper
import sourabh.pal.mandi.common.data.api.model.mappers.ApiVehicleMapper
import sourabh.pal.mandi.common.data.preferences.Preferences
import sourabh.pal.mandi.common.domain.FalconeNotFound
import sourabh.pal.mandi.common.domain.NetworkException
import sourabh.pal.mandi.common.domain.NoTokenToFindFalcone
import sourabh.pal.mandi.common.domain.model.VehiclesAndPlanets
import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import sourabh.pal.mandi.common.utils.DispatchersProvider
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
                val request = ApiFindFalconeRequest.fromDomain(vehicleForPlanet, preferences.getToken())
                val apiPlanets = api.findFalcone(request)
                if(apiPlanets.status == ApiConstants.DID_NOT_FIND_FALCONE) throw FalconeNotFound("Sorry you were not successful!!")
                if(!apiPlanets.error.isNullOrEmpty()) throw NoTokenToFindFalcone(apiPlanets.error.orEmpty())
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
                preferences.putToken(apiToken.token.orEmpty())
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }

    override fun getLocalToken(): String {
        return preferences.getToken()
    }
}