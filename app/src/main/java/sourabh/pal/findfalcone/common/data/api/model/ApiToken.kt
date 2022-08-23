package sourabh.pal.findfalcone.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiToken(
    @field:Json(name = "token")
     val token: String
)