package sourabh.pal.mandi.sell.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sourabh.pal.mandi.common.presentation.model.UISeller
import sourabh.pal.mandi.common.utils.createExceptionHandler
import sourabh.pal.mandi.sell.domain.SearchSellerUsecase
import javax.inject.Inject

@HiltViewModel
class SellAppleFragmentViewModel @Inject constructor(
    val searchSeller: SearchSellerUsecase
) : ViewModel() {

    val state: LiveData<SellAppleViewState> get() = _state
    private val _state = MutableLiveData<SellAppleViewState>()

    private val _nameSharedFlow = MutableSharedFlow<String>()

    val nameSharedFlow = _nameSharedFlow.asSharedFlow()


    init {
        _state.value = SellAppleViewState()
        setupSearchSubscription()
    }

    fun onEvent(event: SellAppleEvent) {
        when (event) {
            is SellAppleEvent.OnNameUpdate -> onSellerNameUpdate(event.name)
        }
    }

    private fun onSellerNameUpdate(name: String) {
        val exceptionHandler = createExceptionHandler(message = "failed to fetch data")
        viewModelScope.launch(exceptionHandler) {
            _nameSharedFlow.emit(name)
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

    private fun setupSearchSubscription() {
        val exceptionHandler = createExceptionHandler(message = "failed to fetch data")
        viewModelScope.launch(exceptionHandler) {
            searchSeller(_nameSharedFlow)
                .collectLatest { sellers ->
                    _state.value = state.value!!.copy(sellerNameSuggestions = sellers.map {
                        UISeller(
                            name = it.name,
                            id = it.cardId,
                            isRegistered = it.isRegistered,
                        )
                    })
                }
        }

    }
}