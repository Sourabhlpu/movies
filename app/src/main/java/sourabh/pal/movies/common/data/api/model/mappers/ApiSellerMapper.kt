package sourabh.pal.movies.common.data.api.model.mappers

import sourabh.pal.movies.common.data.api.model.ApiSeller
import sourabh.pal.movies.common.domain.model.seller.Seller
import javax.inject.Inject

class ApiSellerMapper @Inject constructor(): ApiMapper<ApiSeller, Seller> {
    override fun mapToDomain(apiEntity: ApiSeller): Seller {
        return Seller(
            name = apiEntity.name.orEmpty(),
            cardId = apiEntity.id.orEmpty(),
            isRegistered = !apiEntity.id.isNullOrEmpty()
        )
    }
}