package sourabh.pal.mandi.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiVehicle(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "total_no") val quantity: Int?,
    @field:Json(name = "max_distance") val range: Int?,
    @field:Json(name = "speed") val speed: Int?,
)