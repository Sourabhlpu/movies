package sourabh.pal.findfalcone.common.presentation.model

data class UIVehicle(
    val name: String,
    val quantity: Int,
    val range: Int,
    val speed: Int
)

data class UIVehicleWitDetails(
    val vehicle: UIVehicle,
    val remainingQuantity: Int = vehicle.quantity,
    val enable: Boolean = remainingQuantity > 0,
    var isSelected: Boolean = false
)
