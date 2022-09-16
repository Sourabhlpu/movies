package sourabh.pal.mandi.common.presentation.model.mappers

import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.presentation.model.UIPlanet
import javax.inject.Inject

class UIPlanetMapper @Inject constructor(): UiMapper<Planet, UIPlanet> {
    override fun mapToView(input: Planet): UIPlanet {
        return UIPlanet(name = input.name, distance = input.distance)
    }
}