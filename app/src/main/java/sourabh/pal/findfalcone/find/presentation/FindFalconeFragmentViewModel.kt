package sourabh.pal.findfalcone.find.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIVehicleWitDetails
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIPlanetMapper
import sourabh.pal.findfalcone.common.presentation.model.mappers.UIVehicleMapper
import sourabh.pal.findfalcone.common.utils.createExceptionHandler
import sourabh.pal.findfalcone.find.domain.usecases.FindFalconeUsecase
import sourabh.pal.findfalcone.find.domain.usecases.GetPlanets
import sourabh.pal.findfalcone.find.domain.usecases.GetVehicles
import javax.inject.Inject

const val MAX_NO_OF_PLANETS_TO_BE_SELECTED = 4

@HiltViewModel
class FindFalconeFragmentViewModel @Inject constructor(
    private val getPlanets: GetPlanets,
    private val getVehicles: GetVehicles,
    private val findFalcone: FindFalconeUsecase,
    private val uiPlanetMapper: UIPlanetMapper,
    private val uiVehicleMapper: UIVehicleMapper
) : ViewModel() {

    val state: LiveData<FindFalconeViewState> get() = _state
    private val _state = MutableLiveData<FindFalconeViewState>()


    init {
        _state.value = FindFalconeViewState()
    }

    fun onEvent(event: FindFalconeEvent) {
        when (event) {
            is FindFalconeEvent.PlanetSelected -> onPlanetSelected(event.selectedIndex)
            is FindFalconeEvent.PlanetUnSelected -> onPlanetUnSelected(event.selectedIndex)
            is FindFalconeEvent.OnPageSelected -> updateSelectedPageIndex(event.position)
            is FindFalconeEvent.GetPlanetsAndVehicles -> getPlanetsAndVehicles()
            FindFalconeEvent.Submit -> getDataAndFindFalcone()
            is FindFalconeEvent.OnVehicleClicked -> updateVehicleSelection(event.vehicle)
        }
    }

    private fun getPlanetsAndVehicles() {
        _state.value = state.value!!.copy(loading = true)

        val exceptionHandler = createExceptionHandler(message = "failed to fetch data")

        viewModelScope.launch(exceptionHandler) {
            val planets = async { getPlanets() }
            val vehicles = async { getVehicles() }

            _state.value = state.value!!.updatedToGetVehiclesAndPlanetsSuccess(
                uiPlanets = planets.await().map { uiPlanetMapper.mapToView(it) },
                uiVehicles = vehicles.await().map { uiVehicleMapper.mapToView(it) }
            )
        }
    }

    private fun getDataAndFindFalcone() {
        val selectedPair = state.value!!.selectedPairs
        val planetNames = selectedPair.keys.map { it.name }
        val vehicleNames = selectedPair.values.map { it.name }

        _state.value = state.value!!.copy(loading = true)

        val exceptionHandler = createExceptionHandler(message = "failed to submit request")

        viewModelScope.launch(exceptionHandler) {
            val planet = findFalcone(vehicles = vehicleNames, planets = planetNames)
            _state.value = state.value!!.copy(
                loading = false,
                navigateToSuccess = Event(Pair(planet.name, state.value!!.totalTime))
            )
        }
    }

    private fun updateVehicleSelection(vehicle: UIVehicleWitDetails) {
        if (vehicle.isSelected)
            _state.value = state.value!!.updateToVehicleUnSelected(vehicle)
        else
            _state.value = state.value!!.updateToVehicleSelected(vehicle)
    }

    private fun updateSelectedPageIndex(position: Int) {
        _state.value = state.value!!.updateToWhenPageIsChanged(position)
    }

    private fun onPlanetSelected(selectedIndex: Int) {
        if (areAllPlanetsSelected()) {
            _state.value = state.value!!.updateToAllPlanetsSelected()
        } else {
            _state.value = state.value!!.updateToPlanetSelected(selectedIndex)
        }
    }

    private fun onPlanetUnSelected(selectedIndex: Int) {
        _state.value = state.value!!.updateToPlanetUnSelected(selectedIndex)
    }

    private fun areAllPlanetsSelected() =
        state.value!!.numberOfSelectedPlanets >= MAX_NO_OF_PLANETS_TO_BE_SELECTED

    private fun createExceptionHandler(message: String): CoroutineExceptionHandler {
        return viewModelScope.createExceptionHandler(message) {
            onFailure(it)
        }
    }

    private fun onFailure(throwable: Throwable) {
        _state.value = state.value!!.updateToFailure(throwable)
    }

}
