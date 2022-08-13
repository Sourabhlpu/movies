package sourabh.pal.findfalcone.find.domain.usecases

import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class GetPlanets @Inject constructor(private val repository: FindFalconeRepository){
    suspend  operator fun invoke() = repository.getAllPlanets()
}