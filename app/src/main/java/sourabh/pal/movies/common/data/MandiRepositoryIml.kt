package sourabh.pal.movies.common.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import sourabh.pal.movies.common.data.api.MandiApi
import sourabh.pal.movies.common.data.api.model.ApiSeller
import sourabh.pal.movies.common.data.api.model.ApiVillage
import sourabh.pal.movies.common.data.api.model.mappers.ApiSellerMapper
import sourabh.pal.movies.common.data.api.model.mappers.ApiVillageMapper
import sourabh.pal.movies.common.domain.NetworkException
import sourabh.pal.movies.common.domain.model.Village
import sourabh.pal.movies.common.domain.model.sell.Sell
import sourabh.pal.movies.common.domain.model.seller.Seller
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import sourabh.pal.movies.common.utils.DispatchersProvider
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

class MandiRepositoryIml @Inject constructor(
    private val api: MandiApi,
    private val apiSellerMapper: ApiSellerMapper,
    private val apiVillageMapper: ApiVillageMapper,
    private val ioDispatcher: DispatchersProvider
) : MandiRepository {

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

    override suspend fun sellProduce(sellApple: Sell): String {
        return try {
            withContext(ioDispatcher.io()) {
                delay(500)
                "Yay! Sold your apples."
            }
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code ${exception.code()}")
        }
    }
}