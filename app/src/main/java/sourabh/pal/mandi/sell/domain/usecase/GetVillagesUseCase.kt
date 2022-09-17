package sourabh.pal.mandi.sell.domain.usecase

import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository

import javax.inject.Inject

class GetVillagesUseCase @Inject constructor(private val repository: FindFalconeRepository) {

    suspend operator fun invoke(): List<Village>{
       return repository.getVillages()
    }
}