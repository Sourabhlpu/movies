package sourabh.pal.movies.common.domain.model.seller

 const val LOYALTY_INDEX_REGISTERED = 1.12
 const val DEFAULT_LOYALTY_INDEX = 0.98

data class Seller(
    val cardId: String,
    val name: String,
    val isRegistered: Boolean,
    val loyaltyIndex: Double = if(isRegistered) LOYALTY_INDEX_REGISTERED else DEFAULT_LOYALTY_INDEX
)
