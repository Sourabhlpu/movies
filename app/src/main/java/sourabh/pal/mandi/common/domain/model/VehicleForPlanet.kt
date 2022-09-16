package sourabh.pal.mandi.common.domain.model

import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle

data class VehicleForPlanet(
    val vehicle: Vehicle,
    val planet: Planet
){
    private val canReachPlanet
    get() = vehicle.range >= planet.distance

    fun timeToReachPlanet(): Int {
        check(canReachPlanet)
        check(vehicle.speed > 0) {
            "Vehicle speed cannot be zero or less"
        }
        return planet.distance / vehicle.speed
    }
}