package sourabh.pal.mandi.sell.presentation


import sourabh.pal.mandi.common.presentation.model.UISeller
import sourabh.pal.mandi.common.presentation.model.UIVillage

data class SellAppleViewState(
    val isSubmitting: Boolean = false,
    val sellerName: String,
    val loyaltyCardNumber: String,
    val villageName: String,
    val grossWeight: Double,
    val loyaltyIndex: Double,
    val totalPrice: Double,
    val currency: String,
    val sellerNameSuggestions: List<UISeller>,
    val villages:List<UIVillage>
)