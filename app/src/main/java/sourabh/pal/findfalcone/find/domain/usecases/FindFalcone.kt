package sourabh.pal.findfalcone.find.domain.usecases


import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class FindFalcone @Inject constructor(private val repository: FindFalconeRepository) {
    suspend operator fun invoke(vehicles: List<String>, planets: List<String>) {
        val localToken = repository.getLocalToken()
        if (localToken.isNotEmpty())
            repository.findFalcone(VehiclesAndPlanets(vehicles = vehicles, planets = planets))
        else {
            val token = repository.getToken()
            repository.findFalcone(VehiclesAndPlanets(vehicles = vehicles, planets = planets))
        }
    }

}