package sourabh.pal.mandi

import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sourabh.pal.mandi.common.data.FindFalconeRepositoryIml
import sourabh.pal.mandi.common.data.api.FindFalconeApi
import sourabh.pal.mandi.common.data.api.model.ApiVehicle
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository

class FindFalconeApiTestUsingMockWebServer {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val findFalconeApi by lazy {
        retrofit.create(FindFalconeApi::class.java)
    }

    private val expectedResponse = listOf(ApiVehicle(name= "Space pod", quantity=2, range=200, speed=2), ApiVehicle(name="Space rocket", quantity=1, range=300, speed=4), ApiVehicle(name="Space shuttle", quantity=1, range=400, speed=5), ApiVehicle(name="Space ship", quantity=2, range=600, speed=10))
    private val testJson =
        """[{"name":"Space pod","total_no":2,"max_distance":200,"speed":2},{"name":"Space rocket","total_no":1,"max_distance":300,"speed":4},{"name":"Space shuttle","total_no":1,"max_distance":400,"speed":5},{"name":"Space ship","total_no":2,"max_distance":600,"speed":10}]"""

    @Test
    fun getAllVehiclesReturnsVehiclesList() = runBlocking{
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testJson)
                .setResponseCode(200)
        )

        val testObserver = findFalconeApi.getAllVehicles()
        assertEquals(expectedResponse, testObserver)
    }

    @Test
    fun getAllVehiclesReturnsVehiclesJson() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(testJson)
                .setResponseCode(200)
        )

        val result = findFalconeApi.getAllVehicles()
        assertEquals("/vehicles", mockWebServer.takeRequest().path)
    }
}
