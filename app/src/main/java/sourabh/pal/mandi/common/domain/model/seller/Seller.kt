package sourabh.pal.mandi.common.domain.model.seller

private const val LOYALTY_INDEX_REGISTERED = 1.12
private const val LOYALTY_INDEX_UNREGISTERED = 0.98

data class Seller(
    val cardId: Int,
    val name: String,
    val isRegistered: Boolean,
    val loyaltyIndex: Double = if(isRegistered) LOYALTY_INDEX_REGISTERED else LOYALTY_INDEX_UNREGISTERED
)
