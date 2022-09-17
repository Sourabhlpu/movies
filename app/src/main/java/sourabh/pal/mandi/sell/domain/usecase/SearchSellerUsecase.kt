package sourabh.pal.mandi.sell.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.*
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class SearchSellerUsecase @Inject constructor(private val repository: FindFalconeRepository) {

    suspend operator fun invoke(query: String): List<Seller> {
        return repository.searchSellersByName(query)
    }
}