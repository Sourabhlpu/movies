package sourabh.pal.findfalcone.common.data.api.model.mappers

import sourabh.pal.findfalcone.common.data.api.model.ApiFindFalconeRespone
import sourabh.pal.findfalcone.common.data.api.model.ApiPlanet
import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.domain.PlanetNameEmpty
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import javax.inject.Inject

class ApiFindFalconeResponseMapper @Inject constructor(): ApiMapper<ApiFindFalconeRespone, Planet> {
    override fun mapToDomain(apiEntity: ApiFindFalconeRespone): Planet {
        return Planet(
            name = apiEntity.planetName ?: throw PlanetNameEmpty("Planet name cannot be empty")
        )
    }

}

