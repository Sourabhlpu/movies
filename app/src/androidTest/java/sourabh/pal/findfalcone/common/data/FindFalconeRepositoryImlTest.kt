package sourabh.pal.findfalcone.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import sourabh.pal.findfalcone.common.data.api.FindFalconeApi
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiPlanetMapper
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiVehicleMapper
import sourabh.pal.findfalcone.common.data.api.utils.FakeServer
import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import sourabh.pal.findfalcone.common.utils.DispatchersProvider
import javax.inject.Inject
import kotlin.test.assertFailsWith


@HiltAndroidTest
class FindFalconeRepositoryImlTest {

    private val fakeServer = FakeServer()
    private lateinit var repository: FindFalconeRepository
    private lateinit var api: FindFalconeApi

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiVehicleMapper: ApiVehicleMapper

    @Inject
    lateinit var apiPlanetMapper: ApiPlanetMapper


    private val dispatchersProvider = object : DispatchersProvider {
        override fun io() = Dispatchers.Main
    }

    @Before
    fun setup() {
        fakeServer.start()
        hiltRule.inject()
        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndpoint)
            .build()
            .create(FindFalconeApi::class.java)

        repository = FindFalconeRepositoryIml(
            api,
            apiVehicleMapper,
            apiPlanetMapper,
            dispatchersProvider
        )
    }

    @Test
    fun requestAllVehicles_success() = runBlocking {
        val expectedPlanetName = "Space pod"
        fakeServer.setHappyPathDispatcher()

        val allVehicles = repository.getAllVehicles()

        val vehicle = allVehicles[0]
        assertThat(vehicle.name).isEqualTo(expectedPlanetName)
    }


    @Test
    fun requestAllVehicles_failure() {
        fakeServer.setErrorPathDispatcher()
        val exception = assertFailsWith<NetworkException> {
            runBlocking {
                repository.getAllVehicles()
            }
        }
        assertThat(exception).isInstanceOf(NetworkException::class.java)
    }

    @Test
    fun requestAllPlanets_success() = runBlocking {
        val expectedPlanetName = "Donlon"
        fakeServer.setHappyPathDispatcher()

        val allPlanets = repository.getAllPlanets()

        val vehicle = allPlanets[0]
        assertThat(vehicle.name).isEqualTo(expectedPlanetName)
    }

    @Test
    fun requestAllPlanets_failure() {
        fakeServer.setErrorPathDispatcher()
        val exception = assertFailsWith<NetworkException> {
            runBlocking {
                repository.getAllPlanets()
            }
        }
        assertThat(exception).isInstanceOf(NetworkException::class.java)
    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }

}
