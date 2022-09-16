package sourabh.pal.mandi.common.domain.model.vehicles


data class Vehicles(
    val vehicles: List<Vehicle>
)
data class Vehicle(
    val name: String,
    val quantity: Int,
    val range: Int,
    val speed: Int
)