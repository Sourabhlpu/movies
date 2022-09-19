package sourabh.pal.mandi.sell.domain.usecase

import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.repositories.MandiRepository

import javax.inject.Inject

class GetVillagesUseCase @Inject constructor(private val repository: MandiRepository) {

    suspend operator fun invoke(): List<Village>{
       return repository.getVillages()
    }
}