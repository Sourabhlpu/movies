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
    val selectedSeller: UISeller? = null,
    val selectedVillage: UIVillage? = null,
    val sellerNameSuggestions: List<UISeller> = emptyList(),
    val villages: List<UIVillage> = emptyList(),
    val enableSubmitButton: Boolean = false,
    val failure: Event<Throwable>? = null,
    val navigateOnSuccess: Event<Triple<String, String, String>>? = null,
) {
    fun updateToFailure(throwable: Throwable): SellAppleViewState {
        return copy(
            failure = Event(throwable),
            isSubmitting = false
        )
    }

    fun updateToSearchingNames(): SellAppleViewState {
        return copy(
            isSearchingNames = true, loyaltyCardNumber = "", loyaltyIndex = DEFAULT_LOYALTY_INDEX, selectedSeller = null
        )
    }

    fun resetSeller(): SellAppleViewState {
        return copy(
            loyaltyIndex = DEFAULT_LOYALTY_INDEX,
            sellerName = "",
            loyaltyCardNumber = "",
            enableSubmitButton = enableSubmitButton(),
            selectedSeller = null
        )
    }

    fun updateTotalPrice(totalPrice: String): SellAppleViewState {
        return copy(
            totalPrice = totalPrice,
            grossWeight = grossWeight,
            enableSubmitButton = enableSubmitButton()
        )
    }

    fun updateToVillagesFetched(villages: List<UIVillage>): SellAppleViewState {
        return copy(
            villages = villages, isLoadingVillages = false,
            villageName = villages.first().name
        )
    }

    fun updateToNameSubmitted(name: String): SellAppleViewState {
        val seller = sellerNameSuggestions.find { it.name.equals(name, true) }
        return copy(
            loyaltyCardNumber = seller?.id.orEmpty(),
            isSearchingNames = false,
            loyaltyIndex = seller?.loyaltyIndex ?: 0.0,
            enableSubmitButton = enableSubmitButton(),
            selectedSeller = seller
        )
    }

    fun updateToSellerNameUpdate(
        suggestions: List<UISeller>
    ): SellAppleViewState {
        return copy(
            sellerNameSuggestions = suggestions,
            isSearchingNames = false,
            enableSubmitButton = enableSubmitButton()
        )
    }

    fun updateToSuccess(message: String, sellerName: String): SellAppleViewState {
        return copy(
            isSubmitting = false,
            navigateOnSuccess = Event(Triple(sellerName, totalPrice, grossWeight.toString()))
        )
    }

    fun updateVillageName(name: String): SellAppleViewState? {
        val village = villages.find { it.name.equals(name, true) }
        return copy(
            selectedVillage = village
        )

    }

    fun enableSubmitButton() =
        selectedSeller != null && selectedVillage != null && grossWeight > 0.0


}