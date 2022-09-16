package sourabh.pal.mandi.common.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import sourabh.pal.mandi.common.data.api.model.*

interface FindFalconeApi {

    @GET(ApiConstants.VEHICLES_ENDPOINT)
    suspend fun getAllVehicles(): List<ApiVehicle>

    @GET(ApiConstants.PLANETS_ENDPOINT)
    suspend fun getAllPlanets(): List<ApiPlanet>

    @Headers( "Accept: application/json" )
    @POST(ApiConstants.AUTH_ENDPOINT)
    suspend fun getToken(): ApiToken

    @Headers( "Accept: application/json" )
    @POST(ApiConstants.FIND_FALCONE)
    suspend fun findFalcone(@Body data: ApiFindFalconeRequest): ApiFindFalconeRespone
}