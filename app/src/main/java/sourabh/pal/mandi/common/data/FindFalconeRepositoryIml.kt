package sourabh.pal.mandi.common.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import sourabh.pal.mandi.common.data.api.ApiConstants
import sourabh.pal.mandi.common.data.api.FindFalconeApi
import sourabh.pal.mandi.common.data.api.model.ApiFindFalconeRequest
import sourabh.pal.mandi.common.data.api.model.ApiSearchSellerResponse
import sourabh.pal.mandi.common.data.api.model.ApiSeller
import sourabh.pal.mandi.common.data.api.model.ApiVillage
import sourabh.pal.mandi.common.data.api.model.mappers.*
import sourabh.pal.mandi.common.data.preferences.Preferences
import sourabh.pal.mandi.common.domain.FalconeNotFound
import sourabh.pal.mandi.common.domain.NetworkException
import sourabh.pal.mandi.common.domain.NoTokenToFindFalcone
import sourabh.pal.mandi.common.domain.model.VehiclesAndPlanets
import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import sourabh.pal.mandi.common.utils.DispatchersProvider
import javax.inject.Inject

val sellers = listOf(
    ApiSeller("Rohan", id = "A1241"),
    ApiSeller("Sourabh", id = "A1221"),
    ApiSeller("Ankit", id = "A1261"),
    ApiSeller("Priya", id = "B1241"),
    ApiSeller("Nityam", id = "N1241"),
    ApiSeller("Nityammm", id = "N1241"),
    ApiSeller("Aman", id = "A1234"),
    ApiSeller("Rahul", id = "A1121"),
    ApiSeller("Vikas", id = null),
    ApiSeller("Sanjeet", id = null),
    ApiSeller("Shreya", id = "A2241"),
    ApiSeller("Gautam", id = "C1241"),
)

val villages = listOf(
    ApiVillage("Johari", price = 100.12),
    ApiVillage("Sinola", price = 90.34),
    ApiVillage("Malsi", price = 81.22),
    ApiVillage("Guniyal", price = 78.42),
    ApiVillage("Purkul", price = 93.32),
    ApiVillage("Anarwala", price = 87.32),
    ApiVillage("Jakhan", price = 78.23),
    ApiVillage("Salan", price = 120.00),
    ApiVillage("Galjwadi", price = 150.33),
    ApiVillage("Kolukeht", price = 200.77),
    ApiVillage("Kimadi", price = 150.77),
)

class FindFalconeRepositoryIml @Inject constructor(
    private val api: FindFalconeApi,
    private val apiVehicleMapper: ApiVehicleMapper,
    private val apiPlanetMapper: ApiPlanetMapper,
    private val apiSellerMapper: ApiSellerMapper,
    private val apiVillageMapper: ApiVillageMapper,
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
                val request =
                    ApiFindFalconeRequest.fromDomain(vehicleForPlanet, preferences.getToken())
                val apiPlanets = api.findFalcone(request)
                if (apiPlanets.status == ApiConstants.DID_NOT_FIND_FALCONE) throw FalconeNotFound("Sorry you were not successful!!")
                if (!apiPlanets.error.isNullOrEmpty()) throw NoTokenToFindFalcone(apiPlanets.error.orEmpty())
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

    override suspend fun searchSellersByName(query: String): List<Seller> {
        return try {
            withContext(ioDispatcher.io()) {
                delay(500)
                val filteredSellers = sellers.filter { it.name.orEmpty().startsWith(query, true) }
                filteredSellers.map { apiSellerMapper.mapToDomain(it) }
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }

    override suspend fun getVillages(): List<Village> {
        return try {
            withContext(ioDispatcher.io()) {
                delay(500)
                villages.map { apiVillageMapper.mapToDomain(it) }
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }
}