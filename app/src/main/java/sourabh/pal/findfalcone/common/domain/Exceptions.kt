package sourabh.pal.findfalcone.common.domain

import java.io.IOException

class MaxPlanetSelected(message: String = "all planets selected"): Exception(message )
class NetworkUnavailableException(message: String = "No network available :(") : IOException(message)