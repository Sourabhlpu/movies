package sourabh.pal.mandi.sell.domain.usecase

import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import sourabh.pal.mandi.common.utils.toKg
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class GetApplePrice @Inject constructor() {

    operator fun invoke(loyaltyIndex: Double, pricePerKg: Double, weightInTonnes: Double): String {
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val totalPriceDouble = weightInTonnes.toKg() * pricePerKg * loyaltyIndex
        return currencyFormatter.format(totalPriceDouble)
    }
}