package sourabh.pal.movies.common.presentation.model.mappers

import sourabh.pal.movies.common.domain.model.seller.Seller
import sourabh.pal.movies.common.presentation.model.UISeller
import javax.inject.Inject

class UISellerMapper @Inject constructor(): UiMapper<Seller, UISeller> {
    override fun mapToView(input: Seller): UISeller {
        return UISeller(
            name = input.name,
            id = input.cardId,
            isRegistered = input.isRegistered,
            loyaltyIndex = input.loyaltyIndex
        )
    }
}