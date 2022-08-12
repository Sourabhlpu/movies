package sourabh.pal.findfalcone.common.presentation.model

data class VehiclesForPlanet(
    val planet: UIPlanet = UIPlanet(),
    val vehicles: List<UIVehicle> = emptyList()
){
    val usableVehiclesForPlanet: List<UIVehicle> get() {
        return vehicles.map {
            if(it.range >= planet.distance)
                it
            else
                it.copy(enable = false)
        }
    }
}
