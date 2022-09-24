package sourabh.pal.movies.common.data.api

import retrofit2.http.*
import sourabh.pal.movies.common.data.api.model.*
import sourabh.pal.movies.common.domain.model.sell.Sell


interface MandiApi {
    @GET(ApiConstants.SELLERS_ENDPOINT)
    suspend fun searchSellers(@Query(ApiParameters.NAME) name: String): ApiSearchSellerResponse

    @GET(ApiConstants.GET_VILLAGES)
    suspend fun getVillages(): List<ApiVillage>

    @POST(ApiConstants.SELL_PRODUCE)
    suspend fun sellProduce(produce: Sell): String

    @GET(ApiConstants.BASE_ENDPOINT)
    suspend fun getMovies(
        @Query(ApiParameters.SEARCH_QUERY) query: String,
        @Query(ApiParameters.MOVIE_TYPE) type: String,
        @Query(ApiParameters.PAGE) page: Int,
        @Query(ApiParameters.API_KEY) apiKey: String
    ) : ApiPaginatedMovies
}