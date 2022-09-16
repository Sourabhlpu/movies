package sourabh.pal.mandi.sell.presentation

sealed class SellAppleEvent {
    data class OnNameUpdate(val name: String): SellAppleEvent()
    data class OnLoyaltyCardIdUpdate(val id: String): SellAppleEvent()
    data class OnVillageNameUpdate(val name: String): SellAppleEvent()
    data class onWeightUpdate(val double: String): SellAppleEvent()
    object SellProduce: SellAppleEvent()
}