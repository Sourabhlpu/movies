package sourabh.pal.findfalcone.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets

@JsonClass(generateAdapter = true)
data class ApiFindFalconeRequest(
    @field:Json(name = "token")
    val token: String,
    @field:Json(name = "planet_names")
    val planetNames: List<String>,
    @field:Json(name = "vehicle_names")
    val vehicleNames: List<String>
){
    companion object{
        fun fromDomain(domainModel: VehiclesAndPlanets, token: String): ApiFindFalconeRequest{
            return ApiFindFalconeRequest(
                token = token,
                planetNames = domainModel.planets,
                vehicleNames = domainModel.vehicles
            )
        }
    }
}