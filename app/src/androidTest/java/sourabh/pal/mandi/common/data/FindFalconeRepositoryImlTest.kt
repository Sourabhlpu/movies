package sourabh.pal.mandi.common.data

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
import retrofit2.Retrofit
import sourabh.pal.mandi.common.data.api.MandiApi
import sourabh.pal.mandi.common.data.api.model.mappers.*
import sourabh.pal.mandi.common.data.api.utils.FakeServer
import sourabh.pal.mandi.common.data.preferences.FakePreferences
import sourabh.pal.mandi.common.data.preferences.Preferences
import sourabh.pal.mandi.common.domain.FalconeNotFound
import sourabh.pal.mandi.common.domain.NetworkException
import sourabh.pal.mandi.common.domain.NoTokenToFindFalcone
import sourabh.pal.mandi.common.domain.model.planets.Planet
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.repositories.MandiRepository
import sourabh.pal.mandi.common.utils.DispatchersProvider
import javax.inject.Inject
import kotlin.test.assertFailsWith


@HiltAndroidTest
@UninstallModules(PreferencesModule::class)
class FindFalconeRepositoryImlTest {

    private val fakeServer = FakeServer()
    private lateinit var repository: MandiRepository
    private lateinit var api: MandiApi

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

    @Inject
    lateinit var apiSellerMapper: ApiSellerMapper

    @Inject
    lateinit var apiVillageMapper: ApiVillageMapper

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
            .create(MandiApi::class.java)

        repository = FindFalconeRepositoryIml(
            api,
            apiVehicleMapper,
            apiPlanetMapper,
            apiSellerMapper,
            apiVillageMapper,
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

    @Test
    fun findFalcone_failure() {
        preferences.putToken("afadsfAFAaoiafnpnva23iov12")
        fakeServer.setErrorPathDispatcher()
        val exception = assertFailsWith<NetworkException> {
            runBlocking {
                repository.findFalcone(VehiclesAndPlanets(emptyList(), emptyList()))
            }
        }
        assertThat(exception).isInstanceOf(NetworkException::class.java)
    }

    @Test
    fun findFalcone_unsuccessfull() {
        preferences.putToken("afadsfAFAaoiafnpnva23iov12")
        fakeServer.setUnsuccessfullPathDispatcherForFindFalcone("find_falcone_unsuccessful.json")
        val exception = assertFailsWith<FalconeNotFound> {
            runBlocking {
                repository.findFalcone(VehiclesAndPlanets(emptyList(), emptyList()))
            }
        }
        assertThat(exception).isInstanceOf(FalconeNotFound::class.java)
    }

    @Test
    fun findFalcone_no_token() {
        preferences.putToken("")
        fakeServer.setUnsuccessfullPathDispatcherForFindFalcone("find_falcone_no_token.json")
        val exception = assertFailsWith<NoTokenToFindFalcone> {
            runBlocking {
                repository.findFalcone(VehiclesAndPlanets(emptyList(), emptyList()))
            }
        }
        assertThat(exception).isInstanceOf(NoTokenToFindFalcone::class.java)
    }

    @Test
    fun searchSeller_success() = runBlocking{
        val expectedResult = listOf(Seller(name = "Sourabh", cardId = "A1221", isRegistered = true ))
        val response = repository.searchSellersByName("sou")
        assertThat(response).isEqualTo(expectedResult)
    }

    @Test
    fun searchSeller_notFound() = runBlocking{
        val expectedResult = emptyList<Seller>()
        val response = repository.searchSellersByName("xz")
        assertThat(response).isEqualTo(expectedResult)
    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }

}
