package sourabh.pal.mandi.common.presentation.model.mappers

import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.presentation.model.UISeller
import javax.inject.Inject

class UISellerMapper @Inject constructor(): UiMapper<Seller, UISeller> {
    override fun mapToView(input: Seller): UISeller {
        return UISeller(
            name = input.name,
            id = input.cardId,
            isRegistered = input.isRegistered
        )
    }
}