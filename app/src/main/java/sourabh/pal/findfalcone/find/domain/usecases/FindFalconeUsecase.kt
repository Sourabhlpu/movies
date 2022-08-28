package sourabh.pal.findfalcone.find.domain.usecases


import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import javax.inject.Inject

class FindFalconeUsecase @Inject constructor(private val repository: FindFalconeRepository) {
    suspend operator fun invoke(vehicles: List<String>, planets: List<String>): Planet {
        val localToken = repository.getLocalToken()
        return  if (localToken.isNotEmpty())
            repository.findFalcone(VehiclesAndPlanets(vehicles = vehicles, planets = planets))
        else {
            repository.getToken()
            repository.findFalcone(VehiclesAndPlanets(vehicles = vehicles, planets = planets))
        }
    }

}