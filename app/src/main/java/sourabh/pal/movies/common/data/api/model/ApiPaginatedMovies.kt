package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedMovies(
    @field:Json(name = "Search") val movies: List<ApiMovie>?,
    @field:Json(name = "totalResults") val totalResults: Int?,
    @field:Json(name = "currentPage") val currentPage: Int?,
    @field:Json(name = "Response") val response: String?,
    @field:Json(name = "Error") val error: String?,
)
