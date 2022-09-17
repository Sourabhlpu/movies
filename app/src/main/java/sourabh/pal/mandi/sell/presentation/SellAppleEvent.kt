package sourabh.pal.mandi.sell.presentation

sealed class SellAppleEvent {
    data class OnNameUpdate(val name: String): SellAppleEvent()
    data class OnSubmitName(val name: String): SellAppleEvent()
    data class OnLoyaltyCardIdUpdate(val id: String): SellAppleEvent()
    data class OnVillageNameUpdate(val name: String): SellAppleEvent()
    data class OnWeightUpdate(val weight: Double): SellAppleEvent()
    object SellProduce: SellAppleEvent()
    object GetVillages: SellAppleEvent()
    object OnNameCleared: SellAppleEvent()
}