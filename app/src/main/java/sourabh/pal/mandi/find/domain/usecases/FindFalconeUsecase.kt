package sourabh.pal.mandi.find.domain.usecases


import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.repositories.MandiRepository
import javax.inject.Inject

class FindFalconeUsecase @Inject constructor(private val repository: MandiRepository) {
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