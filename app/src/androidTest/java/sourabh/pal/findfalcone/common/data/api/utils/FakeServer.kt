package sourabh.pal.findfalcone.common.data.api.utils

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import sourabh.pal.findfalcone.common.data.api.ApiConstants
import java.io.IOException
import java.io.InputStream

class FakeServer {
  private val mockWebServer = MockWebServer()

  private val endpointSeparator = "/"
  private val vehiclesEndpoint = endpointSeparator + ApiConstants.VEHICLES_ENDPOINT
  private val planetsEndpoint = endpointSeparator + ApiConstants.PLANETS_ENDPOINT
  private val authEndpoint = endpointSeparator + ApiConstants.AUTH_ENDPOINT
  private val findFalcone = endpointSeparator + ApiConstants.FIND_FALCONE
  private val notFoundResponse = MockResponse().setResponseCode(404)

  val baseEndpoint
    get() = mockWebServer.url(endpointSeparator)

  fun start() {
    mockWebServer.start(8080)
  }

  fun setHappyPathDispatcher() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path ?: return notFoundResponse

        return with(path) {
          when {
            startsWith(vehiclesEndpoint) -> {
              MockResponse().setResponseCode(200).setBody(getJson("vehicles.json"))
            }
            startsWith(planetsEndpoint) -> {
              MockResponse().setResponseCode(200).setBody(getJson("planets.json"))
            }
            startsWith(authEndpoint) -> {
              MockResponse().setResponseCode(200).setBody(getJson("token.json"))
            }
            startsWith(findFalcone) -> {
              MockResponse().setResponseCode(200).setBody(getJson("find_falcone.json"))
            }
            else -> {
              notFoundResponse
            }
          }
        }
      }
    }
  }

  fun setErrorPathDispatcher() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path ?: return notFoundResponse

        return with(path) {
          when {
            startsWith(vehiclesEndpoint) -> {
              notFoundResponse
            }
            startsWith(planetsEndpoint) -> {
              notFoundResponse
            }
            startsWith(authEndpoint) -> {
              notFoundResponse
            }
            else -> {
              notFoundResponse
            }
          }
        }
      }
    }
  }

  fun shutdown() {
    mockWebServer.shutdown()
  }

  private fun getJson(path: String): String {
    return try {
      val context = InstrumentationRegistry.getInstrumentation().context
      val jsonStream: InputStream = context.assets.open("networkresponses/$path")
      String(jsonStream.readBytes())
    } catch (exception: IOException) {
      Log.e(exception.toString(), "Error reading network response json asset")
      throw exception
    }
  }
}