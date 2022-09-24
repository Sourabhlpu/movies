package sourabh.pal.movies.common.data.api.model.mappers

import sourabh.pal.movies.common.data.api.model.ApiMovie
import sourabh.pal.movies.common.domain.model.Movie
import javax.inject.Inject

class ApiMovieMapper @Inject constructor(): ApiMapper<ApiMovie, Movie> {
    override fun mapToDomain(apiEntity: ApiMovie): Movie {
        return Movie(
            title = apiEntity.title.orEmpty(),
            id = apiEntity.imdbId ?: throw MappingException("Movie Id cannot be null"),
            poster = apiEntity.poster.orEmpty(),
            year = apiEntity.year.orEmpty()
        )
    }
}