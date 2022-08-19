package sourabh.pal.findfalcone.common.domain

import java.io.IOException

data class MaxPlanetSelected(override val message: String = "all planets selected"): Exception(message )
data class NetworkUnavailableException(override val message: String = "No network available :(") : IOException(message)
data class NetworkException(override val message: String): Exception(message)