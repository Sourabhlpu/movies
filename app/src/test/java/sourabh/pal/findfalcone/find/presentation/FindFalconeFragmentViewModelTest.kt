package sourabh.pal.findfalcone.find.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.internal.matchers.Find
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
    fun `FindFalconeFragmentViewModel when planets and vehicles are fetched and first planet is visible`() {
        //GIVEN
        repository.isHappyPath = true
        val planets = repository.planets.map { uiPlanetsMapper.mapToView(it) }
        val vehicles = repository.vehicles.map { uiVehiclesMapper.mapToView(it) }

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
    fun `FindFalconeFragmentViewModel when planets and vehicles are fetched and a planet is selected vehicles list should be visible`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }
        val expectedState = getExpectedStateWhenPlanetIsSelected(0)

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(isSelected = true, 0))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when furthest planet is selected then unreachable vehicles are disabled`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState = getExpectedStateWhenPlanetIsSelected(5)

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(5))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 5))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when planets and vehicles are fetched and planet at x position is visible`() {
        `FindFalconeFragmentViewModel planet becomes visible and no vehicle is selected`(0)
        `FindFalconeFragmentViewModel planet becomes visible and no vehicle is selected`(1)
        `FindFalconeFragmentViewModel planet becomes visible and no vehicle is selected`(5)
    }

    @Test
    fun `FindFalconeFragmentViewModel when vehicle is selected for first planet`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState = expectedStateWhenVehicleIsSelectedForFirstPlanet()

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

    @Test
    fun `FindFalconeFragmentViewModel when vehicle is selected for first planet and second planet is swiped`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState =
            expectedStateWhenVehicleIsSelectedForFirstPlanetAndThenSecondPlanetIsVisible()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicles.first()))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(1))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 1))


        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when first vehicle is selected for first and second planet`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState = expectedStateWhenSameVehicleIsSelectedForFirstTwoPlanets()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicles.first()))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(1))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 1))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehicles[0]))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when vehicle is selected for first planet and second planet`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState = expectedStateWhenFirstAndSecondPlanetSelectFirstPlanet()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicles.first()))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(1))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 1))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehicles.first()))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when all four planets are selected`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState = expectedStateWhenAllFourPlanetsAreSelectedAndFifthIsClicked()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(1))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 1))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(2))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 2))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(3))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 3))
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(4))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 4))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when second vehicle is selected for a planet`() {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }

        val expectedState = expectedStateForSingleVehicleSelectionForPlanet()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(true, 0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicles.first()))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicles[1]))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    private fun `FindFalconeFragmentViewModel planet becomes visible and no vehicle is selected`(
        position: Int
    ) = testCoroutineRule.runBlockingTest {
        //GIVEN
        repository.isHappyPath = true
        viewModel.state.observeForever { }
        val expectedState = FindFalconeViewState(
            vehicles = vehicles,
            planets = planets,
            vehiclesForSelectedPlanet = VehiclesForPlanet(planets[position], vehicles)
        )

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanets)
        viewModel.onEvent(FindFalconeEvent.GetVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(position))

        //Then
        val viewState = viewModel.state.value!!
        assertThat(viewState).isEqualTo(expectedState)
    }

    //todo remove these methods
    private fun getExpectedStateWhenPlanetIsSelected(
        selectedPosition: Int,
        previousState: FindFalconeViewState? = null
    ): FindFalconeViewState {
        val updatedPlanets = getPlanetsAfterSelection(selectedPosition, previousState)

        return FindFalconeViewState(
            vehicles = previousState?.vehicles ?: vehicles,
            planets = updatedPlanets,
            showVehicles = true,
            vehiclesForSelectedPlanet = VehiclesForPlanet(
                updatedPlanets[selectedPosition],
                previousState?.vehicles ?: vehicles
            )
        )
    }

    //todo remove these methods
    private fun getPlanetsAfterSelection(
        selectedPosition: Int,
        previousState: FindFalconeViewState? = null
    ): List<UIPlanet> {
        val planets = previousState?.planets ?: planets
        return planets.mapIndexed { index, uiPlanet ->
            if (index == selectedPosition) uiPlanet.copy(
                isSelected = true
            ) else uiPlanet
        }
    }

}