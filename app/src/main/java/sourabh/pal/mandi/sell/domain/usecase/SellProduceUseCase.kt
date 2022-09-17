package sourabh.pal.mandi.sell.domain.usecase

import sourabh.pal.mandi.common.domain.model.sell.Sell
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class SellProduceUseCase @Inject constructor(private val repository: FindFalconeRepository) {

    suspend operator fun invoke(sellApples: Sell): String{
        return repository.sellProduce(sellApples)
    }
}