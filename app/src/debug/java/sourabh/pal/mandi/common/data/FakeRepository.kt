package sourabh.pal.mandi.common.data

import sourabh.pal.mandi.common.data.api.model.ApiSeller
import sourabh.pal.mandi.common.data.api.model.ApiVillage
import sourabh.pal.mandi.common.domain.NetworkException
import sourabh.pal.mandi.common.domain.model.VehiclesAndPlanets
import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.sell.Sell
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import sourabh.pal.mandi.common.presentation.model.UIPlanet
import sourabh.pal.mandi.common.presentation.model.UIVehicle
import javax.inject.Inject

class FakeRepository @Inject constructor() : FindFalconeRepository {

    var isHappyPath = true
    var sendFullList = false


    val planets by lazy {
        listOf(
            Planet(
                name = "Donlon",
                distance = 100
            ),
            Planet(
                name = "Enchai",
                distance = 200
            ),
            Planet(
                name = "Jebing",
                distance = 300
            ),
            Planet(
                name = "Sapir",
                distance = 400
            ),
            Planet(
                name = "Lerbin",
                distance = 500
            ),
            Planet(
                name = "Pingasor",
                distance = 600
            )
        )
    }
    val vehicles by lazy {
        listOf(
            Vehicle(
                "space pod",
                quantity = 2,
                range = 150,
                speed = 2
            ),
            Vehicle(
                "space rocket",
                quantity = 1,
                range = 200,
                speed = 4
            ),
            Vehicle(
                "space shuttle",
                quantity = 1,
                range = 400,
                speed = 5
            ),
            Vehicle(
                "space ship",
                quantity = 2,
                range = 600,
                speed = 10
            )
        )
    }


    val sellers by lazy {
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

    override suspend fun getAllVehicles(): List<Vehicle> {
        return when {
            isHappyPath && !sendFullList -> vehicles.subList(0, 2)
            isHappyPath && sendFullList -> vehicles
            else -> throw NetworkException("Network Exception")
        }
    }

    override suspend fun getAllPlanets(): List<Planet> {
        return when {
            isHappyPath && !sendFullList -> planets.subList(0, 2)
            isHappyPath && sendFullList -> planets
            else -> throw NetworkException("Network Exception")
        }
    }

    override suspend fun findFalcone(vehicleForPlanet: VehiclesAndPlanets): Planet {
        return Planet("Donlon")
    }

    override suspend fun getToken() {

    }

    override suspend fun searchSellersByName(query: String): List<Seller> {
        return sellers.filter { it.name.startsWith(query, true) }
    }

    override suspend fun getVillages(): List<Village> {
        return villages
    }

    override suspend fun sellProduce(sellApple: Sell): String {
        return if(isHappyPath) "Success" else "failure"
    }

    override fun getLocalToken(): String {
        return ""
    }

}