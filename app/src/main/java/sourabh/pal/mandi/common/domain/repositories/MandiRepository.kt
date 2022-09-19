package sourabh.pal.mandi.common.domain.repositories

import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.model.sell.Sell
import sourabh.pal.mandi.common.domain.model.seller.Seller


interface MandiRepository {
    suspend fun searchSellersByName(query: String): List<Seller>
    suspend fun getVillages(): List<Village>
    suspend fun sellProduce(sellApple: Sell): String
}