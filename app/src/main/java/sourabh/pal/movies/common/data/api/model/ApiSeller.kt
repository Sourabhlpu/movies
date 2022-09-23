package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSeller(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "id") val id: String?,
)