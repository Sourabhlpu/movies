package sourabh.pal.mandi.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import retrofit2.HttpException
import retrofit2.Response
import sourabh.pal.mandi.TestCoroutineRule
import sourabh.pal.mandi.common.data.api.MandiApi
import sourabh.pal.mandi.common.data.api.model.ApiSearchSellerResponse
import sourabh.pal.mandi.common.data.api.model.ApiSeller
import sourabh.pal.mandi.common.data.api.model.ApiVillage
import sourabh.pal.mandi.common.data.api.model.mappers.ApiSellerMapper
import sourabh.pal.mandi.common.data.api.model.mappers.ApiVillageMapper
import sourabh.pal.mandi.common.domain.NetworkException
import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.domain.model.sell.Sell
import sourabh.pal.mandi.common.utils.DispatchersProvider
import java.net.HttpRetryException

class MandiRepositoryImlTest {

    private val villagesApi = listOf(
        ApiVillage("Johari", price = 100.12),
        ApiVillage("Sinola", price = 90.34),
        ApiVillage("Malsi", price = 81.22),
        ApiVillage("Guniyal", price = 78.42),
        ApiVillage("Purkul", price = 93.32),
        ApiVillage("Anarwala", price = 87.32),
        ApiVillage("Jakhan", price = 78.23),
        ApiVillage("Salan", price = 120.00),
        ApiVillage("Galjwadi", price = 150.33),
        ApiVillage("Kolukeht", price = 200.77),
        ApiVillage("Kimadi", price = 150.77),
    )

    private val villages = listOf(
        Village("Johari", pricePerKgApple = 100.12),
        Village("Sinola", pricePerKgApple = 90.34),
        Village("Malsi", pricePerKgApple = 81.22),
        Village("Guniyal", pricePerKgApple = 78.42),
        Village("Purkul", pricePerKgApple = 93.32),
        Village("Anarwala", pricePerKgApple = 87.32),
        Village("Jakhan", pricePerKgApple = 78.23),
        Village("Salan", pricePerKgApple = 120.00),
        Village("Galjwadi", pricePerKgApple = 150.33),
        Village("Kolukeht", pricePerKgApple = 200.77),
        Village("Kimadi", pricePerKgApple = 150.77),
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mandiApi: MandiApi = mock()
    private val apiSellerMapper = ApiSellerMapper()
    private val apiVillageMapper = ApiVillageMapper()
    private val dispatchersProvider = object : DispatchersProvider {
        override fun io() = Dispatchers.Main
    }
    private val repository =
        MandiRepositoryIml(mandiApi, apiSellerMapper, apiVillageMapper, dispatchersProvider)


    @Test
    fun `MandiRepository search seller by name success`() = testCoroutineRule.runBlockingTest {
        val apiSearchResponse = ApiSearchSellerResponse(
            status = "success",
            error = null,
            sellers = listOf(ApiSeller("Sourabh", "A2341"))
        )
        whenever(mandiApi.searchSellers(anyString())).thenReturn(apiSearchResponse)

        val result = repository.searchSellersByName("sou")
        delay(600)
        assertThat(result.first().name).isEqualTo("Sourabh")

    }

    @Test
    fun `MandiRepository search seller by name failure`() = testCoroutineRule.runBlockingTest {
        val apiSearchResponse = ApiSearchSellerResponse(
            status = "success",
            error = null,
            sellers = emptyList()
        )
        whenever(mandiApi.searchSellers(anyString())).thenReturn(apiSearchResponse)

        val result = repository.searchSellersByName("x")
        delay(600)
        assertThat(result).isEmpty()

    }

    @Test
    fun `MandiRepository getVillages Success`() = testCoroutineRule.runBlockingTest {
        whenever(mandiApi.getVillages()).thenReturn(villagesApi)

        val result = repository.getVillages()
        assert(result == villages)
    }

    @Test(expected = Exception::class)
    fun `MandiRepository getVillages failure`() = testCoroutineRule.runBlockingTest {
        whenever(mandiApi.getVillages()).thenThrow(Exception())
        repository.getVillages()
    }

    @Test
    fun `MandiRepository sellProduce success`() = testCoroutineRule.runBlockingTest {
        whenever(mandiApi.sellProduce(any())).thenReturn("Yay! Sold your apples.")
        val result = repository.sellProduce(Sell())
        assertThat(result).isEqualTo("Yay! Sold your apples.")
    }

    @Test(expected = Exception::class)
    fun `MandiRepository sellProduce failure`() = testCoroutineRule.runBlockingTest {
        whenever(mandiApi.sellProduce(any())).thenThrow(Exception())
        val result = repository.sellProduce(Sell())
    }

}