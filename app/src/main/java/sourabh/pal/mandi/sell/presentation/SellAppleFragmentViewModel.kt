package sourabh.pal.mandi.sell.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sourabh.pal.mandi.common.domain.model.sell.Sell
import sourabh.pal.mandi.common.presentation.model.mappers.UISellerMapper
import sourabh.pal.mandi.common.presentation.model.mappers.UIVillageMapper
import sourabh.pal.mandi.common.utils.createExceptionHandler
import sourabh.pal.mandi.sell.domain.usecase.GetApplePrice
import sourabh.pal.mandi.sell.domain.usecase.GetVillagesUseCase
import sourabh.pal.mandi.sell.domain.usecase.SearchSellerUseCase
import sourabh.pal.mandi.sell.domain.usecase.SellProduceUseCase
import javax.inject.Inject

@HiltViewModel
class SellAppleFragmentViewModel @Inject constructor(
    private val searchSeller: SearchSellerUseCase,
    private val uiSellerMapper: UISellerMapper,
    private val uiVillageMapper: UIVillageMapper,
    private val getVillages: GetVillagesUseCase,
    private val getApplePrice: GetApplePrice,
    private val sellProduceUseCase: SellProduceUseCase
) : ViewModel() {

    val state: LiveData<SellAppleViewState> get() = _state
    private val _state = MutableLiveData<SellAppleViewState>()

    private var searchJob: Job? = null


    init {
        _state.value = SellAppleViewState()
    }

    fun onEvent(event: SellAppleEvent) {
        when (event) {
            is SellAppleEvent.OnNameUpdate -> onSellerNameUpdate(event.name)
            is SellAppleEvent.OnSubmitName -> handleOnNameSubmit(event)
            is SellAppleEvent.OnNameCleared -> handleClearName()
            is SellAppleEvent.OnLoyaltyCardIdUpdate -> handleLoyaltyCardIdUpdate(event.id)
            is SellAppleEvent.GetVillages -> fetchVillages()
            is SellAppleEvent.OnWeightUpdate -> handleWeightUpdate(event.weight)
            is SellAppleEvent.OnVillageNameUpdate -> handleVillageNameUpdate(event.name)
            is SellAppleEvent.SellProduce -> handleSellButtonClick()
        }
    }

    private fun handleSellButtonClick() {
        _state.value = state.value!!.copy(isSubmitting = true)
        val exceptionHandler = createExceptionHandler(message = "failed to sell")
        searchJob = viewModelScope.launch(exceptionHandler) {
            val selectedSeller = state.value!!.selectedSeller
            val selectedVillage = state.value!!.selectedVillage
            val sellProduce = Sell(
                sellerName = selectedSeller?.name.orEmpty(),
                loyaltyId = selectedSeller?.id.orEmpty(),
                villageName = selectedVillage?.name.orEmpty(),
                totalWeight = state.value!!.grossWeight,
                totalPrice = state.value!!.totalPrice.toDoubleOrNull() ?: 0.00
            )
            val message = sellProduceUseCase(sellProduce)
            _state.value = state.value!!.updateToSuccess(message, selectedSeller?.name.orEmpty())
        }
    }

    private fun handleClearName() {
        _state.value = state.value!!.resetSeller()
        updateTotalPrice()
    }

    private fun handleVillageNameUpdate(name: String) {
        _state.value = state.value!!.updateVillageName(name)
        updateTotalPrice()
    }

    private fun handleWeightUpdate(weight: Double) {
        _state.value = state.value!!.copy(grossWeight = weight)
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        _state.value = state.value!!.updateTotalPrice(getUpdatedPrice())
    }

    private fun getUpdatedPrice(): String {
        return getApplePrice(
            loyaltyIndex = state.value!!.loyaltyIndex,
            pricePerKg = state.value!!.selectedVillage?.pricePerKgApple ?: 0.00,
            weightInTonnes = state.value!!.grossWeight
        )
    }

    private fun fetchVillages() {
        _state.value = state.value!!.copy(isLoadingVillages = true)
        val exceptionHandler = createExceptionHandler(message = "failed to fetch villages")
        searchJob = viewModelScope.launch(exceptionHandler) {
            val result = getVillages()
            val uiVillages = result.map { uiVillageMapper.mapToView(it) }
            _state.value = state.value!!.updateToVillagesFetched(uiVillages)
        }
    }

    private fun handleLoyaltyCardIdUpdate(id: String) {}

    private fun handleOnNameSubmit(event: SellAppleEvent.OnSubmitName) {
        _state.value = _state.value!!.updateToNameSubmitted(event.name)
        updateTotalPrice()
    }

    private fun onSellerNameUpdate(query: String) {
        if (query.isEmpty())
            return
        _state.value = state.value!!.updateToSearchingNames()
        val exceptionHandler = createExceptionHandler(message = "failed to fetch data")
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            val result = searchSeller(query)
            val uiSeller = result.map { uiSellerMapper.mapToView(it) }
            _state.value = state.value!!.updateToSellerNameUpdate(uiSeller)
        }
    }

    private fun createExceptionHandler(message: String): CoroutineExceptionHandler {
        return viewModelScope.createExceptionHandler(message) {
            onFailure(it)
        }
    }

    private fun onFailure(throwable: Throwable) {
        _state.value = state.value!!.updateToFailure(throwable)
    }


}