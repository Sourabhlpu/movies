package sourabh.pal.mandi.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiFindFalconeRespone(
    @field:Json(name = "planet_name")
    val planetName: String?,

    @field:Json(name = "status")
    val status: String?,

    @field:Json(name = "error")
    val error: String?,
)