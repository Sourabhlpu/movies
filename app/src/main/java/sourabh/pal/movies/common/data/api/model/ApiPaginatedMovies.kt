package sourabh.pal.movies.common.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedMovies(
    @field:Json(name = "Search") val movies: List<ApiMovie>?,
    @field:Json(name = "pagination") val pagination: ApiPagination?
)


@JsonClass(generateAdapter = true)
data class ApiPagination(
    @field:Json(name = "totalResults") val totalPages: Int?
)