package sourabh.pal.mandi.sell.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sourabh.pal.mandi.TestCoroutineRule
import sourabh.pal.mandi.common.data.FakeRepository
import sourabh.pal.mandi.common.domain.NetworkException
import sourabh.pal.mandi.common.presentation.Event
import sourabh.pal.mandi.common.presentation.model.UISeller
import sourabh.pal.mandi.common.presentation.model.UIVillage
import sourabh.pal.mandi.common.presentation.model.mappers.UISellerMapper
import sourabh.pal.mandi.common.presentation.model.mappers.UIVillageMapper
import sourabh.pal.mandi.sell.domain.usecase.GetApplePrice
import sourabh.pal.mandi.sell.domain.usecase.GetVillagesUseCase
import sourabh.pal.mandi.sell.domain.usecase.SearchSellerUseCase
import sourabh.pal.mandi.sell.domain.usecase.SellProduceUseCase


@ExperimentalCoroutinesApi
class SellAppleFragmentViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: SellAppleFragmentViewModel
    private lateinit var repository: FakeRepository
    private lateinit var searchSeller: SearchSellerUseCase
    private lateinit var getVillages: GetVillagesUseCase
    private lateinit var getApplePrice: GetApplePrice
    private lateinit var sellProduceUseCase: SellProduceUseCase
    private val uiSellerMapper: UISellerMapper = UISellerMapper()
    private val uiVillageMapper: UIVillageMapper = UIVillageMapper()
    private lateinit var villages: List<UIVillage>
    //private lateinit var sellers: List<UISeller>

    @Before
    fun setup() {
        repository = FakeRepository()
        villages = repository.villages.map { uiVillageMapper.mapToView(it) }
        searchSeller = SearchSellerUseCase(repository)
        getVillages = GetVillagesUseCase(repository)
        getApplePrice = GetApplePrice()
        sellProduceUseCase = SellProduceUseCase(repository)

        viewModel = SellAppleFragmentViewModel(
            searchSeller,
            uiSellerMapper,
            uiVillageMapper,
            getVillages,
            getApplePrice,
            sellProduceUseCase
        )
    }

    @Test
    fun `SellAppleFragmentViewModel GetVillagesSuccess`() = testCoroutineRule.runBlockingTest {

        //Given
        repository.isHappyPath = true
        val villages = getVillages().map { uiVillageMapper.mapToView(it) }
        viewModel.state.observeForever {}

        val expectedViewState = SellAppleViewState(
            villages = villages,
            villageName = villages.first().name
        )

        //When
        viewModel.onEvent(SellAppleEvent.GetVillages)

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedViewState)
    }

    @Test(expected = NetworkException::class)
    fun `SellAppleFragmentViewModel GetVillagesFailure`() = testCoroutineRule.runBlockingTest {

        //Given
        repository.isHappyPath = false
        getVillages().map { uiVillageMapper.mapToView(it) }
        viewModel.state.observeForever {}

        val expectedViewState = SellAppleViewState(
            failure = Event(NetworkException("Network Exception"))
        )

        //When
        viewModel.onEvent(SellAppleEvent.GetVillages)

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedViewState)
    }

    @Test
    fun `SellAppleFragmentViewModel onNameUpdate Success`() = testCoroutineRule.runBlockingTest {
        //Given
        repository.isHappyPath = true
        val searchResult =
            listOf(UISeller("Sourabh", id = "A1221", isRegistered = true, loyaltyIndex = 1.12))
        val villages = repository.villages.map { uiVillageMapper.mapToView(it) }
        viewModel.state.observeForever {}

        val expectedViewState = SellAppleViewState(
            sellerNameSuggestions = searchResult,
            villageName = villages.first().name,
            villages = villages,
            isSearchingNames = false
        )

        //When
        viewModel.onEvent(SellAppleEvent.GetVillages)
        viewModel.onEvent(SellAppleEvent.OnNameUpdate("sour"))
        delay(600) // to delay longer than search event threshold

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedViewState)
    }

    @Test
    fun `SellAppleFragmentViewModel onNameUpdate failure`() = testCoroutineRule.runBlockingTest {
        //Given
        repository.isHappyPath = true
        val searchResult = emptyList<UISeller>()
        val villages = repository.villages.map { uiVillageMapper.mapToView(it) }
        viewModel.state.observeForever {}

        val expectedViewState = SellAppleViewState(
            sellerNameSuggestions = searchResult,
            villageName = villages.first().name,
            villages = villages,
            isSearchingNames = false
        )

        //When
        viewModel.onEvent(SellAppleEvent.GetVillages)
        viewModel.onEvent(SellAppleEvent.OnNameUpdate("xz"))
        delay(600) // to delay longer than search event threshold

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedViewState)
    }

    @Test
    fun `SellAppleFragmentViewModel onNameCleared`() = testCoroutineRule.runBlockingTest {
        //Given

    }
}