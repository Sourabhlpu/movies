package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiMovie(
    @field:Json(name = "Title") val title: String?,
    @field:Json(name = "Year") val year: String?,
    @field:Json(name = "imdbID") val imdbId: String?,
    @field:Json(name = "Type") val type: String?,
    @field:Json(name = "Poster") val poster: String?

)