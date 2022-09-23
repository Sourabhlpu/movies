package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchSellerResponse(
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "error") val error: String?,
    @field:Json(name = "sellers") val sellers: List<ApiSeller>
)