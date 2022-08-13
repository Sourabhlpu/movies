package sourabh.pal.findfalcone.common.presentation.model

data class UIVehicle(
    val name: String,
    val quantity: Int,
    val range: Int,
    val speed: Int,
    val selectedQuantity: Int = 0,
    val enable: Boolean = selectedQuantity < quantity,
    val remainingQuantity: Int = quantity,
    var isSelected: Boolean = false,
    val selectedFor: List<UIPlanet> = emptyList()
){
    fun isSelected(currentPlanet: UIPlanet): Boolean{
        isSelected = selectedFor.contains(currentPlanet)
        return isSelected
    }
}
