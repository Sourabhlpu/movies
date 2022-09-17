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
import sourabh.pal.mandi.common.presentation.model.mappers.UISellerMapper
import sourabh.pal.mandi.common.utils.createExceptionHandler
import sourabh.pal.mandi.sell.domain.SearchSellerUsecase
import javax.inject.Inject

@HiltViewModel
class SellAppleFragmentViewModel @Inject constructor(
    val searchSeller: SearchSellerUsecase,
    private val uiSellerMapper: UISellerMapper
) : ViewModel() {

    val state: LiveData<SellAppleViewState> get() = _state
    private val _state = MutableLiveData<SellAppleViewState>()

    private var searchJob: Job? = null

    init { _state.value = SellAppleViewState() }

    fun onEvent(event: SellAppleEvent) {
        when (event) {
            is SellAppleEvent.OnNameUpdate -> onSellerNameUpdate(event.name)
        }
    }

    private fun onSellerNameUpdate(query: String) {
        if (query.isEmpty())
            return
        _state.value = state.value!!.copy(isSearchingNames = true)
        val exceptionHandler = createExceptionHandler(message = "failed to fetch data")
        searchJob?.cancel()
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(500)
            val result = searchSeller(query)
            val uiSeller = result.map { uiSellerMapper.mapToView(it) }
            _state.value = state.value!!.copy(sellerNameSuggestions = uiSeller, isSearchingNames = false)
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