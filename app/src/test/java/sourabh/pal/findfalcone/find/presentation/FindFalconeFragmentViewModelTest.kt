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
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIPlanetMapper
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIVehicleMapper
import sourabh.pal.findfalcone.find.domain.usecases.FindFalconeUsecase
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
    private lateinit var findFalcone: FindFalconeUsecase
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
        findFalcone = FindFalconeUsecase(repository)
        viewModel = FindFalconeFragmentViewModel(
            getPlanets,
            getVehicles,
            findFalcone,
            uiPlanetsMapper,
            uiVehiclesMapper
        )
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
            viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)

            //Then
            val viewState = viewModel.state.value!!
            assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test
    fun `FindFalconeFragmentViewModel getPlanetsAndVehiclesSuccess`() =
        testCoroutineRule.runBlockingTest {
            repository.isHappyPath
            val expectedPlanets =
                repository.getAllPlanets().map { uiPlanetsMapper.mapToView(it) }
            val expectedVehicles = repository.getAllVehicles().map { uiVehiclesMapper.mapToView(it) }
            viewModel.state.observeForever {}

            val expectedViewState = FindFalconeViewState(
                loading = false,
                planets = expectedPlanets,
                vehicles = expectedVehicles,
                vehiclesForCurrentPlanet = expectedVehicles.map { UIVehicleWitDetails(vehicle = it) },
                failure = null
            )

            //When
            viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)

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
            viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)

            //Then
            val viewState = viewModel.state.value!!
            assertThat(viewState).isEqualTo(expectedViewState)

        }

    @Test
    fun `FindFalconeFragmentViewModel when a page is selected`() {
        //Given
        val expectedState = expectedStateWhenPageIsVisible(0)

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }


    @Test
    fun `FindFalconeFragmentViewModel when a planet is selected`() {
        //Given
        repository.isHappyPath = true
        repository.sendFullList = false
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenPlanetIsSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when a vehicle is selected`() {
        //Given
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenVehicleIsSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when a vehicle is selected and then planet is unselected`() {
        //Given
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenPageIsVisible(0)

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))
        viewModel.onEvent(FindFalconeEvent.PlanetUnSelected(0))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when a vehicle is selected and then vehicle is unselected`() {
        //Given
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenPlanetIsSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when multiple vehicles are selected for planet`() {
        //Given
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenSecondVehicleIsSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[1]))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }

    @Test
    fun `FindFalconeFragmentViewModel when all four pairs are selected`() {
        //Given
        repository.sendFullList = true
        repository.isHappyPath = true
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenAllPlanetsAndVehiclesAreSelected().copy(navigateToSuccess = null)

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))

        viewModel.onEvent(FindFalconeEvent.OnPageSelected(1))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(1))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[1]))

        viewModel.onEvent(FindFalconeEvent.OnPageSelected(2))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(2))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[2]))

        viewModel.onEvent(FindFalconeEvent.OnPageSelected(3))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(3))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[3]))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)

    }

    @Test
    fun `FindFalconeFragmentViewModel when all four pairs are selected and submit is clicked`() {
        //Given
        repository.sendFullList = true
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenAllPlanetsAndVehiclesAreSelected()

        //When
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected(0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))

        viewModel.onEvent(FindFalconeEvent.OnPageSelected(1))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(1))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[1]))

        viewModel.onEvent(FindFalconeEvent.OnPageSelected(2))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(2))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[2]))

        viewModel.onEvent(FindFalconeEvent.OnPageSelected(3))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected(3))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[3]))
        viewModel.onEvent(FindFalconeEvent.Submit)

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)

    }

/*    @Test
    fun `FindFalconeFragmentViewModel when all four planets are selected`() {
        //Given
        repository.isHappyPath
        viewModel.state.observeForever { }
        val expectedState = expectedStateWhenSecondVehicleIsSelected()

        //When
viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
        viewModel.onEvent(FindFalconeEvent.OnPageSelected( 0))
        viewModel.onEvent(FindFalconeEvent.PlanetSelected( 0))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(viewModel.state.value!!.vehiclesForCurrentPlanet[0]))
        viewModel.onEvent(FindFalconeEvent.OnVehicleClicked( viewModel.state.value!!.vehiclesForCurrentPlanet[1]))

        val viewState = viewModel.state.value!!

        assertThat(viewState).isEqualTo(expectedState)
    }*/


}