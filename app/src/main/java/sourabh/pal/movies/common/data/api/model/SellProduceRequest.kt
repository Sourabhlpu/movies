package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SellProduceRequest(
    @field:Json(name = "sellerName") val sellerName: String?,
    @field:Json(name = "loyaltyId") val loyaltyId: String?,
    @field:Json(name = "villageName") val villageName: String?,
    @field:Json(name = "totalPrice") val totalPrice: String?,
)