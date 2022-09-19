package sourabh.pal.mandi.common.data.api

import kotlinx.coroutines.delay
import retrofit2.http.*
import sourabh.pal.mandi.common.data.api.model.*
import sourabh.pal.mandi.common.data.api.model.mappers.ApiSellerMapper
import sourabh.pal.mandi.common.domain.model.seller.Seller


interface MandiApi {

    @GET(ApiConstants.VEHICLES_ENDPOINT)
    suspend fun getAllVehicles(): List<ApiVehicle>

    @GET(ApiConstants.PLANETS_ENDPOINT)
    suspend fun getAllPlanets(): List<ApiPlanet>

    @Headers("Accept: application/json")
    @POST(ApiConstants.AUTH_ENDPOINT)
    suspend fun getToken(): ApiToken

    @Headers("Accept: application/json")
    @POST(ApiConstants.FIND_FALCONE)
    suspend fun findFalcone(@Body data: ApiFindFalconeRequest): ApiFindFalconeRespone

    @GET(ApiConstants.SELLERS_ENDPOINT)
    suspend fun searchSellers(@Query(ApiParameters.NAME) name: String): ApiSearchSellerResponse
}