package sourabh.pal.movies.common.domain.repositories

import kotlinx.coroutines.flow.Flow
import sourabh.pal.movies.common.domain.model.Movie
import sourabh.pal.movies.common.domain.model.Village
import sourabh.pal.movies.common.domain.model.sell.Sell
import sourabh.pal.movies.common.domain.model.seller.Seller
import sourabh.pal.movies.common.domain.pagination.PaginatedMovies


interface MandiRepository {
    suspend fun searchSellersByName(query: String): List<Seller>
    suspend fun getVillages(): List<Village>
    suspend fun sellProduce(sellApple: Sell): String
    fun getMovies(): Flow<List<Movie>>
    suspend fun storeMovies(movies: List<Movie>)
    fun searchCachedMoviesBy(searchParameters: String): Flow<List<Movie>>
    suspend fun searchMoviesRemotely(
        pageToLoad: Int,
        searchParameter: String,
        numberOfItems: Int
    ): PaginatedMovies

}