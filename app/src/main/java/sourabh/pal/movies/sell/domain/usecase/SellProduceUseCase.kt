package sourabh.pal.movies.sell.domain.usecase

import sourabh.pal.movies.common.domain.model.sell.Sell
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject

class SellProduceUseCase @Inject constructor(private val repository: MandiRepository) {

    suspend operator fun invoke(sellApples: Sell): String{
        return repository.sellProduce(sellApples)
    }
}