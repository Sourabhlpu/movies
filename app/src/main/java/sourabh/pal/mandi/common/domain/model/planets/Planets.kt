package sourabh.pal.mandi.common.domain.model.planets


data class Planets(val planets: List<Planet>)

data class Planet(
    val name: String,
    val distance: Int = 0,
)