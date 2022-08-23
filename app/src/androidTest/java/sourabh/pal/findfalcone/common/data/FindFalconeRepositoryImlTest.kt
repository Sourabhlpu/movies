package sourabh.pal.findfalcone.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.notNull
import retrofit2.HttpException
import retrofit2.Retrofit
import sourabh.pal.findfalcone.common.data.api.FindFalconeApi
import sourabh.pal.findfalcone.common.data.api.model.ApiFindFalconeRespone
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiFindFalconeResponseMapper
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiPlanetMapper
import sourabh.pal.findfalcone.common.data.api.model.mappers.ApiVehicleMapper
import sourabh.pal.findfalcone.common.data.api.utils.FakeServer
import sourabh.pal.findfalcone.common.data.di.PreferencesModule
import sourabh.pal.findfalcone.common.data.preferences.FakePreferences
import sourabh.pal.findfalcone.common.data.preferences.Preferences
import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets
import sourabh.pal.findfalcone.common.domain.model.planets.Planet
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import sourabh.pal.findfalcone.common.utils.DispatchersProvider
import javax.inject.Inject
import kotlin.math.exp
import kotlin.test.assertFailsWith


@HiltAndroidTest
@UninstallModules(PreferencesModule::class)
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

    @Inject
    lateinit var apiFindFalcomeMapper: ApiFindFalconeResponseMapper

    @BindValue
    @JvmField
    val preferences: Preferences = FakePreferences()


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
            apiFindFalcomeMapper,
            preferences,
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


    @Test
    fun requestToken_success() = runBlocking {
        val expectedToken = "afadsfAFAaoiafnpnva23iov12"
        fakeServer.setHappyPathDispatcher()

        repository.getToken()

        val localToken = preferences.getToken()
        assertThat(expectedToken).isEqualTo(localToken)

    }

    @Test
    fun requestToken_failure() {
        fakeServer.setErrorPathDispatcher()
        val exception = assertFailsWith<NetworkException> {
            runBlocking {
                repository.getToken()
            }
        }
        assertThat(exception).isInstanceOf(NetworkException::class.java)
    }

    @Test
    fun findFalcone_success() = runBlocking {
        fakeServer.setHappyPathDispatcher()
        preferences.putToken("afadsfAFAaoiafnpnva23iov12")
        val expectedResponse = Planet(
            name = "Jebing"
        )

        val response = repository.findFalcone(VehiclesAndPlanets(emptyList(), emptyList()))

        assertThat(response).isEqualTo(expectedResponse)

    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }



}
