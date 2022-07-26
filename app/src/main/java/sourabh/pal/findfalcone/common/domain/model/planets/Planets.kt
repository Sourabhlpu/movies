package sourabh.pal.findfalcone.common.domain.model.planets


data class Planets(val planets: List<Planet>)

data class Planet(
    val name: String,
    val distance: Int,
)