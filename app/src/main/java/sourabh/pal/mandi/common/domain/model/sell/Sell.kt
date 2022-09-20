package sourabh.pal.mandi.common.domain.model.sell

data class Sell(
    val sellerName: String = "",
    val loyaltyId: String = "",
    val villageName: String = "",
    val totalWeight: Double = 0.00,
    val totalPrice: Double = 0.00
)
