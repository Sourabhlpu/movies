package sourabh.pal.findfalcone.common.presentation.model

data class UIVehicle(
    val name: String,
    val quantity: Int,
    val range: Int,
    val speed: Int,
    val selectedQuantity: Int = 0,
    val enable: Boolean = selectedQuantity < quantity,
    val isSelected: Boolean = false,
    val remainingQuantity: Int = quantity
)
