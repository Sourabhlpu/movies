package sourabh.pal.mandi.common.data.api

import kotlinx.coroutines.delay
import retrofit2.http.*
import sourabh.pal.mandi.common.data.api.model.*
import sourabh.pal.mandi.common.data.api.model.mappers.ApiSellerMapper
import sourabh.pal.mandi.common.domain.model.seller.Seller


interface MandiApi {
    @GET(ApiConstants.SELLERS_ENDPOINT)
    suspend fun searchSellers(@Query(ApiParameters.NAME) name: String): ApiSearchSellerResponse

    @GET(ApiConstants.GET_VILLAGES)
    suspend fun getVillages(): List<ApiVillage>
}