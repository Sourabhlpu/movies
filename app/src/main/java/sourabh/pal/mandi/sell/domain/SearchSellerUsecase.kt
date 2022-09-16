package sourabh.pal.mandi.sell.domain

import android.util.Log
import kotlinx.coroutines.flow.*
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class SearchSellerUsecase @Inject constructor(private val repository: FindFalconeRepository) {

    suspend operator fun invoke(query: SharedFlow<String>): Flow<List<Seller>> {

        val result = query
            .debounce(500)
            .filter { it.length > 2 }
            .distinctUntilChanged()
            .map {
                repository.searchSellersByName(it)
            }

        return flow {
            var lisst = emptyList<Seller>()
            result.collectLatest {
                lisst = it
            }
            emit(lisst)
        }
    }
}