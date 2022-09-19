package sourabh.pal.mandi.common.data.api.model.mappers

import sourabh.pal.mandi.common.data.api.model.ApiVillage
import sourabh.pal.mandi.common.domain.model.Village
import javax.inject.Inject

class ApiVillageMapper @Inject constructor(): ApiMapper<ApiVillage, Village> {
    override fun mapToDomain(apiEntity: ApiVillage): Village {
        return Village(
            name = apiEntity.name.orEmpty(),
            pricePerKgApple = apiEntity.price ?: 0.00
        )
    }
}