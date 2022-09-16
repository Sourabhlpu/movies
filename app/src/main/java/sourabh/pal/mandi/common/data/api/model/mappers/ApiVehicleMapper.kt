package sourabh.pal.mandi.common.data.api.model.mappers

import sourabh.pal.mandi.common.data.api.model.ApiVehicle
import sourabh.pal.mandi.common.domain.model.vehicles.Vehicle
import javax.inject.Inject

class ApiVehicleMapper @Inject constructor(): ApiMapper<ApiVehicle, Vehicle> {

    override fun mapToDomain(apiEntity: ApiVehicle): Vehicle {
        return Vehicle(
            name = apiEntity.name.orEmpty(),
            quantity = apiEntity.quantity ?: 0,
            range = apiEntity.range ?: 0,
            speed = apiEntity.speed ?: 0
        )
    }
}