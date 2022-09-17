package sourabh.pal.mandi.sell.presentation


import sourabh.pal.mandi.common.domain.model.seller.DEFAULT_LOYALTY_INDEX
import sourabh.pal.mandi.common.presentation.Event
import sourabh.pal.mandi.common.presentation.model.UISeller
import sourabh.pal.mandi.common.presentation.model.UIVillage
import sourabh.pal.mandi.find.presentation.FindFalconeViewState

data class SellAppleViewState(
    val isSubmitting: Boolean = false,
    val isSearchingNames: Boolean = false,
    val isLoadingVillages: Boolean = false,
    val sellerName: String = "",
    val loyaltyCardNumber: String = "",
    val villageName: String = "",
    val grossWeight: Double = 0.0,
    val loyaltyIndex: Double = DEFAULT_LOYALTY_INDEX,
    val totalPrice: String = "0.00",
    val currency: String = "",
    val sellerNameSuggestions: List<UISeller> = emptyList(),
    val villages: List<UIVillage> = emptyList(),
    val enableSubmitButton: Boolean = false,
    val failure: Event<Throwable>? = null,
    val navigateOnSuccess: Event<String>? = null,
) {
    fun updateToFailure(throwable: Throwable): SellAppleViewState {
        return copy(
            failure = Event(throwable),
            isSubmitting = false
        )
    }

    fun updateToSearchingNames(): SellAppleViewState {
        return copy(
            isSearchingNames = true, loyaltyCardNumber = "", loyaltyIndex = DEFAULT_LOYALTY_INDEX
        )
    }

    fun resetSeller(enableButton: Boolean): SellAppleViewState {
        return copy(
            loyaltyIndex = DEFAULT_LOYALTY_INDEX, sellerName = "", loyaltyCardNumber = "", enableSubmitButton = enableButton
        )
    }

    fun updateTotalPrice(totalPrice: String, enableButton: Boolean): SellAppleViewState {
        return copy(
            totalPrice = totalPrice,
            grossWeight = grossWeight,
            enableSubmitButton = enableButton
        )
    }

    fun updateToVillagesFetched(villages: List<UIVillage>): SellAppleViewState {
        return copy(
            villages = villages, isLoadingVillages = false,
            villageName = villages.first().name
        )
    }

    fun updateToNameSubmitted(id: String, loyaltyIndex: Double, enableButton: Boolean): SellAppleViewState {
        return copy(
            loyaltyCardNumber = id,
            isSearchingNames = false,
            loyaltyIndex = loyaltyIndex,
            enableSubmitButton = enableButton
        )
    }

    fun updateToSellerNameUpdate( suggestions: List<UISeller>, enableButton: Boolean): SellAppleViewState {
        return copy(
            sellerNameSuggestions = suggestions, isSearchingNames = false, enableSubmitButton = enableButton
        )
    }

    fun updateToSuccess(message: String): SellAppleViewState{
        return copy(
            navigateOnSuccess = Event(message)
        )
    }

}