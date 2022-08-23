package sourabh.pal.findfalcone.common.domain

import java.io.IOException

data class MaxPlanetSelected(override val message: String = "all planets selected"): Exception(message )
data class NetworkUnavailableException(override val message: String = "No network available :(") : IOException(message)
data class NetworkException(override val message: String): Exception(message)
data class PlanetNameEmpty(override val message: String): Exception(message)
data class FalconeNotFound(override val message: String): Exception(message)
data class NoTokenToFindFalcone(override val message: String): Exception(message)