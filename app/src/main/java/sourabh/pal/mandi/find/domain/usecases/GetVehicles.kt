package sourabh.pal.mandi.find.domain.usecases

import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject


class GetVehicles @Inject constructor(private val repository: FindFalconeRepository){
    suspend  operator fun invoke() = repository.getAllVehicles()
}