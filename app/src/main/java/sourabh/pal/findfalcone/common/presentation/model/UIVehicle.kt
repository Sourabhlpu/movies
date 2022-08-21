package sourabh.pal.findfalcone.common.presentation.model

data class UIVehicle(
    val name: String,
    val quantity: Int,
    val range: Int,
    val speed: Int,
    val remainingQuantity: Int = quantity,
    val enable: Boolean = remainingQuantity > 0,
    var isSelected: Boolean = false,
    val selectedFor: List<UIPlanet> = emptyList()
){
    fun isSelectedForPlanet(currentPlanet: UIPlanet): Boolean{
        return selectedFor.contains(currentPlanet)
    }

}
