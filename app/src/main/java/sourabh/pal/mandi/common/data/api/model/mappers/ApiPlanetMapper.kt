package sourabh.pal.mandi.common.data.api.model.mappers

import sourabh.pal.mandi.common.data.api.model.ApiPlanet
import sourabh.pal.mandi.common.domain.model.planets.Planet
import javax.inject.Inject

class ApiPlanetMapper @Inject constructor(): ApiMapper<ApiPlanet, Planet> {

    override fun mapToDomain(apiEntity: ApiPlanet): Planet {
        return Planet(
            name = apiEntity.name.orEmpty(),
            distance = apiEntity.distance ?: 0
        )
    }
}