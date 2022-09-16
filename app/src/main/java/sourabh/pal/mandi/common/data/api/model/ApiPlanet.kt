package sourabh.pal.mandi.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPlanet(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "distance") val distance: Int?,
)