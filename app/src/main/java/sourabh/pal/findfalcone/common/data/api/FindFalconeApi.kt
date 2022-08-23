package sourabh.pal.findfalcone.common.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import sourabh.pal.findfalcone.common.data.api.model.*

interface FindFalconeApi {

    @GET(ApiConstants.VEHICLES_ENDPOINT)
    suspend fun getAllVehicles(): List<ApiVehicle>

    @GET(ApiConstants.PLANETS_ENDPOINT)
    suspend fun getAllPlanets(): List<ApiPlanet>

    @POST(ApiConstants.AUTH_ENDPOINT)
    suspend fun getToken(): ApiToken

    @POST(ApiConstants.FIND_FALCONE)
    suspend fun findFalcone(@Body data: ApiFindFalconeRequest): ApiFindFalconeRespone
}