package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SellProduceResponse(
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "error") val error: String?
)