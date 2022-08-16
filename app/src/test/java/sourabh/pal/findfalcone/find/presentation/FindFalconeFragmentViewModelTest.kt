package sourabh.pal.findfalcone.find.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sourabh.pal.findfalcone.TestCoroutineRule
import sourabh.pal.findfalcone.common.data.FakeRepository
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

}