package sourabh.pal.findfalcone.find.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sourabh.pal.findfalcone.TestCoroutineRule
import sourabh.pal.findfalcone.common.data.FakeRepository
import sourabh.pal.findfalcone.common.domain.NetworkException
import sourabh.pal.findfalcone.common.presentation.Event
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

    @Before
    fun setup() {
        repository = FakeRepository()
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
                vehiclesForSelectedPlanet = VehiclesForPlanet(vehicles = expectedVehicles)
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetVehicles)

            //Then
            val viewState = viewModel.state.value!!
            assertThat(viewState).isEqualTo(expectedViewState)

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
            assertThat(viewState).isEqualTo(expectedViewState)

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
            assertThat(viewState).isEqualTo(expectedViewState)

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
            assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test
    fun `FindFalconeFragmentViewModel when planets and vehicles are fetched`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            repository.isHappyPath = true
            val planets = repository.getAllPlanets().map { uiPlanetsMapper.mapToView(it) }
            val vehicles = repository.getAllVehicles().map { uiVehiclesMapper.mapToView(it) }

            viewModel.state.observeForever { }

            val expectedState = FindFalconeViewState(
                vehicles = vehicles,
                planets = planets,
                vehiclesForSelectedPlanet = VehiclesForPlanet(planets.first(), vehicles)
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetPlanets)
            viewModel.onEvent(FindFalconeEvent.GetVehicles)
            viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))

            //Then
            val viewState = viewModel.state.value!!
            assertThat(viewState).isEqualTo(expectedState)
        }

    @Test
    fun `FindFalconeViewModel when a planet is selected`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            repository.isHappyPath = true
            val planets = repository.getAllPlanets().map { uiPlanetsMapper.mapToView(it) }
            val vehicles = repository.getAllVehicles().map { uiVehiclesMapper.mapToView(it) }

            viewModel.state.observeForever { }

            val expectedState = ExpectedStates.whenPlanetIsSelected(
                vehicles,
                planets,
                0,
                true
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetPlanets)
            viewModel.onEvent(FindFalconeEvent.GetVehicles)
            viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
            viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))

            //Then
            val viewState = viewModel.state.value!!
            assertThat(viewState).isEqualTo(expectedState)
        }


    @Test
    fun `FindFalconeFragmentViewModel when vehicle is selected for first planet`() =
        testCoroutineRule.runBlockingTest {
            //GIVEN
            repository.isHappyPath = true
            val planets = repository.getAllPlanets().map { uiPlanetsMapper.mapToView(it) }
            val vehicles = repository.getAllVehicles().map { uiVehiclesMapper.mapToView(it) }

            viewModel.state.observeForever { }

            val updatedPlanets =
                planets.mapIndexed { index, uiPlanet -> if (index == 0) uiPlanet.copy(isSelected = true) else uiPlanet }
            val updatedVehicles = vehicles.mapIndexed { index, uiVehicle ->
                if (index == 0)
                    uiVehicle.copy(
                        selectedFor = listOf(planets.first().copy(isSelected = true)),
                        isSelected = true,
                        remainingQuantity = uiVehicle.quantity - 1
                    )
                else
                    uiVehicle
            }

            val expectedState = FindFalconeViewState(
                vehicles = updatedVehicles,
                planets = updatedPlanets,
                vehiclesForSelectedPlanet = VehiclesForPlanet(
                    planets.first().copy(isSelected = true), updatedVehicles
                ),
                showVehicles = true
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetPlanets)
            viewModel.onEvent(FindFalconeEvent.GetVehicles)
            viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
            viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))
            viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicles.first()))

            //Then
            val viewState = viewModel.state.value!!
            assertThat(viewState).isEqualTo(expectedState)
        }


}