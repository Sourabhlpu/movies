package sourabh.pal.movies.common.domain.repositories

import sourabh.pal.movies.common.domain.model.Village
import sourabh.pal.movies.common.domain.model.sell.Sell
import sourabh.pal.movies.common.domain.model.seller.Seller


interface MandiRepository {
    suspend fun searchSellersByName(query: String): List<Seller>
    suspend fun getVillages(): List<Village>
    suspend fun sellProduce(sellApple: Sell): String
}