package sourabh.pal.findfalcone.common.presentation.model.mappers

import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import javax.inject.Inject

class UIPlanetMapper @Inject constructor(): UiMapper<Planet, UIPlanet> {
    override fun mapToView(input: Planet): UIPlanet {
        return UIPlanet(name = input.name, distance = input.distance)
    }
}