package sourabh.pal.mandi.sell.domain.usecase

import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.repositories.MandiRepository
import javax.inject.Inject

class SearchSellerUseCase @Inject constructor(private val repository: MandiRepository) {

    suspend operator fun invoke(query: String): List<Seller> {
        return repository.searchSellersByName(query)
    }
}