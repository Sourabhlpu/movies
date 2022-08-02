package sourabh.pal.findfalcone.common.presentation.model

data class UIPlanet(
val name: String,
val distance: Int,
val isSelected: Boolean = false,
val selectedIndex: Int = 0
)