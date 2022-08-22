package sourabh.pal.findfalcone.find.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sourabh.pal.findfalcone.TestCoroutineRule
import sourabh.pal.findfalcone.common.data.FakeRepository
import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIPlanetMapper
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIVehicleMapper
import sourabh.pal.findfalcone.find.domain.usecases.GetPlanets
import sourabh.pal.findfalcone.find.domain.usecases.GetVehicles

@ExperimentalCoroutinesApi
class FindFalconeFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: FindFalconeFragmentViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getPlanets: GetPlanets
    private lateinit var getVehicles: GetVehicles
    private val uiPlanetsMapper = UIPlanetMapper()
    private val uiVehiclesMapper = UIVehicleMapper()
    private lateinit var planets: List<UIPlanet>
    private lateinit var vehicles: List<UIVehicle>

    @Before
    fun setup() {
        repository = FakeRepository()
        planets = repository.planets.map { uiPlanetsMapper.mapToView(it) }
        vehicles = repository.vehicles.map { uiVehiclesMapper.mapToView(it) }
        getPlanets = GetPlanets(repository)
        getVehicles = GetVehicles(repository)
        viewModel = FindFalconeFragmentViewModel(
            getPlanets,
            getVehicles,
            uiPlanetsMapper,
            uiVehiclesMapper
        )
    }

    @Test
    fun `FindFalconeFragmentViewModel with getVehiclesSuccess`() =
        testCoroutineRule.runBlockingTest {
            repository.isHappyPath = true
            val expectedVehicles =
                repository.getAllVehicles().map { uiVehiclesMapper.mapToView(it) }
            viewModel.state.observeForever {}

            val expectedViewState = FindFalconeViewState(
                loading = false,
                vehicles = expectedVehicles,
                failure = null,
                vehiclesForCurrentPlanet = expectedVehicles
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetVehicles)

            //Then
            val viewState = viewModel.state.value!!
            Truth.assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test(expected = NetworkException::class)
    fun `FindFalconeFragmentViewModel with getVehiclesFailure`() =
        testCoroutineRule.runBlockingTest {
            repository.isHappyPath = false
            repository.getAllVehicles().map { uiVehiclesMapper.mapToView(it) }
            viewModel.state.observeForever {}

            val expectedViewState = FindFalconeViewState(
                loading = false,
                failure = Event(NetworkException("Network Exception"))
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetVehicles)

            //Then
            val viewState = viewModel.state.value!!
            Truth.assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test
    fun `FindFalconeFragmentViewModel with getPlanetsSuccess`() =
        testCoroutineRule.runBlockingTest {
            repository.isHappyPath
            val expectedPlanets =
                repository.getAllPlanets().map { uiPlanetsMapper.mapToView(it) }
            viewModel.state.observeForever {}

            val expectedViewState = FindFalconeViewState(
                loading = false,
                planets = expectedPlanets,
                failure = null
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetPlanets)

            //Then
            val viewState = viewModel.state.value!!
            Truth.assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test(expected = NetworkException::class)
    fun `FindFalconeFragmentViewModel with getPlanetsFailure`() =
        testCoroutineRule.runBlockingTest {
            repository.isHappyPath = false
            repository.getAllPlanets().map { uiPlanetsMapper.mapToView(it) }
            viewModel.state.observeForever {}

            val expectedViewState = FindFalconeViewState(
                loading = false,
                failure = Event(NetworkException("Network Exception"))
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetPlanets)

            //Then
            val viewState = viewModel.state.value!!
            Truth.assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test
    fun `FindFalconeFragmentViewModel when a page is selected`() {
        //Given
        val expectedState = expectedStateWhenPageIsVisible(0)

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected( 0))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }


    @Test
    fun `FindFalconeFragmentViewModel when a planet is selected`() {
        //Given
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenPlanetIsSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected( 0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when a vehicle is selected`() {
        //Given
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenPlanetIsSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected( 0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

}