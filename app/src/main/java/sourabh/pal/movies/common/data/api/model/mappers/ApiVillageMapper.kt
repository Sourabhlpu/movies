package sourabh.pal.movies.common.data.api.model.mappers

import sourabh.pal.movies.common.data.api.model.ApiVillage
import sourabh.pal.movies.common.domain.model.Village
import javax.inject.Inject

class ApiVillageMapper @Inject constructor(): ApiMapper<ApiVillage, Village> {
    override fun mapToDomain(apiEntity: ApiVillage): Village {
        return Village(
            name = apiEntity.name.orEmpty(),
            pricePerKgApple = apiEntity.price ?: 0.00
        )
    }
}