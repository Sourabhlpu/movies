package sourabh.pal.findfalcone.common.presentation.model.mappers

import sourabh.pal.findfalcone.common.presentation.model.UIPlanet

object UiPlanetToStringMapper: UiMapper<List<UIPlanet>, List<String>> {
    override fun mapToView(input: List<UIPlanet>) = input.map { it.name }
}