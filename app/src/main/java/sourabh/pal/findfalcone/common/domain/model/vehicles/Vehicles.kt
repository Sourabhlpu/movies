package sourabh.pal.findfalcone.common.domain.model.vehicles


data class Vehicles(
    val vehicles: List<Vehicle>
)
data class Vehicle(
    val name: String,
    val quantity: Int,
    val maxDistance: Int,
    val speed: Int
)