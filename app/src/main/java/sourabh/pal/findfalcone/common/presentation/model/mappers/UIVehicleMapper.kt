package sourabh.pal.findfalcone.common.presentation.model.mappers

import sourabh.pal.findfalcone.common.domain.model.vehicles.Vehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import javax.inject.Inject

class UIVehicleMapper @Inject constructor() : UiMapper<Vehicle, UIVehicle> {
    override fun mapToView(input: Vehicle): UIVehicle {
        return UIVehicle(
            name = input.name,
            quantity = input.quantity,
            range = input.range,
            speed = input.speed
        )
    }
}