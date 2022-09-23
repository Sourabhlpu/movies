package sourabh.pal.movies.sell.domain.usecase

import sourabh.pal.movies.common.domain.model.seller.Seller
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import javax.inject.Inject

class SearchSellerUseCase @Inject constructor(private val repository: MandiRepository) {

    suspend operator fun invoke(query: String): List<Seller> {
        return repository.searchSellersByName(query)
    }
}