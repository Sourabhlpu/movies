package sourabh.pal.mandi.common.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPlanets(
    val planets: List<ApiPlanet>?
)