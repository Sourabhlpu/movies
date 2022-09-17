package sourabh.pal.mandi.sell.presentation


import sourabh.pal.mandi.common.presentation.Event
import sourabh.pal.mandi.common.presentation.model.UISeller
import sourabh.pal.mandi.common.presentation.model.UIVillage
import sourabh.pal.mandi.find.presentation.FindFalconeViewState

data class SellAppleViewState(
    val isSubmitting: Boolean = false,
    val isSearchingNames: Boolean = false,
    val sellerName: String = "",
    val loyaltyCardNumber: String = "",
    val villageName: String = "",
    val grossWeight: Double = 0.0,
    val loyaltyIndex: Double = 0.0,
    val totalPrice: Double = 0.0,
    val currency: String = "",
    val sellerNameSuggestions: List<UISeller> = emptyList(),
    val villages:List<UIVillage> = emptyList(),
    val failure: Event<Throwable>? = null
){
    fun updateToFailure(throwable: Throwable): SellAppleViewState {
        return copy(
            failure = Event(throwable),
            isSubmitting = false
        )
    }
}