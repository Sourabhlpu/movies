package sourabh.pal.mandi.common.data

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
import retrofit2.Retrofit
import sourabh.pal.mandi.common.data.api.MandiApi
import sourabh.pal.mandi.common.data.api.model.mappers.ApiSellerMapper
import sourabh.pal.mandi.common.data.api.model.mappers.ApiVillageMapper
import sourabh.pal.mandi.common.data.api.utils.FakeServer
import sourabh.pal.mandi.common.domain.model.seller.Seller
import sourabh.pal.mandi.common.domain.repositories.MandiRepository
import sourabh.pal.mandi.common.utils.DispatchersProvider
import javax.inject.Inject


@HiltAndroidTest
class MandiRepositoryImlTest {

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
    lateinit var apiSellerMapper: ApiSellerMapper

    @Inject
    lateinit var apiVillageMapper: ApiVillageMapper


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

        repository = MandiRepositoryIml(
            api,
            apiSellerMapper,
            apiVillageMapper,
            dispatchersProvider
        )
    }



    @Test
    fun searchSeller_success() = runBlocking {
        val expectedResult = listOf(Seller(name = "Sourabh", cardId = "A1221", isRegistered = true))
        val response = repository.searchSellersByName("sou")
        assertThat(response).isEqualTo(expectedResult)
    }

    @Test
    fun searchSeller_notFound() = runBlocking {
        val expectedResult = emptyList<Seller>()
        val response = repository.searchSellersByName("xz")
        assertThat(response).isEqualTo(expectedResult)
    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }

}
