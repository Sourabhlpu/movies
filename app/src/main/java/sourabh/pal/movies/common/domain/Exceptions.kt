package sourabh.pal.movies.common.domain

import java.io.IOException

data class NetworkUnavailableException(override val message: String = "No network available :(") : IOException(message)
data class NetworkException(override val message: String): Exception(message)
class NoMoreMoviesException(message: String): Exception(message)
