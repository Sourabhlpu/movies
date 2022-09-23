package sourabh.pal.movies.common.data

import sourabh.pal.movies.common.domain.NetworkException
import sourabh.pal.movies.common.domain.model.Village
import sourabh.pal.movies.common.domain.model.sell.Sell
import sourabh.pal.movies.common.domain.model.seller.Seller
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject

class FakeRepository @Inject constructor() : MandiRepository {

    var isHappyPath = true

    private val sellers by lazy {
        listOf(
            Seller(name = "Rohan", cardId = "A1241", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name = "Sourabh", cardId = "A1221", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name = "Ankit", cardId = "A1261", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name = "Priya", cardId = "B1241", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name ="Nityam", cardId = "N1241", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name ="Nityammm", cardId = "N1241", isRegistered = true, loyaltyIndex = 1.12),
            Seller( name = "Aman", cardId = "A1234", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name ="Rahul", cardId = "A1121", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name ="Vikas", cardId = "", isRegistered = false, loyaltyIndex = 0.98),
            Seller(name = "Sanjeet", cardId = "", isRegistered = false, loyaltyIndex = 0.98),
            Seller(name = "Shreya", cardId = "A2241", isRegistered = true, loyaltyIndex = 1.12),
            Seller(name = "Gautam", cardId = "C1241", isRegistered = true, loyaltyIndex = 1.12),
        )
    }

    val villages by lazy {
        listOf(
            Village("Johari", pricePerKgApple = 100.12),
            Village("Sinola", pricePerKgApple = 90.34),
            Village("Malsi", pricePerKgApple = 81.22),
            Village("Guniyal", pricePerKgApple = 78.42),
            Village("Purkul", pricePerKgApple = 93.32),
            Village("Anarwala", pricePerKgApple = 87.32),
            Village("Jakhan", pricePerKgApple = 78.23),
            Village("Salan", pricePerKgApple = 120.00),
            Village("Galjwadi", pricePerKgApple = 150.33),
            Village("Kolukeht", pricePerKgApple = 200.77),
            Village("Kimadi", pricePerKgApple = 150.77),
        )
    }


    override suspend fun searchSellersByName(query: String): List<Seller> {
        return if(isHappyPath) sellers.filter { it.name.startsWith(query, true) } else emptyList()
    }

    override suspend fun getVillages(): List<Village> {
        return if(isHappyPath) villages else throw NetworkException("Network Exception")
    }

    override suspend fun sellProduce(sellApple: Sell): String {
        return if(isHappyPath) "Success" else "failure"
    }

}