package sourabh.pal.mandi.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiVillage(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "price") val price: Double?,
)