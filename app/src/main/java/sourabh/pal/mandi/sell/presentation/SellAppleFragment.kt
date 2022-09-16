package sourabh.pal.mandi.sell.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sourabh.pal.mandi.R
import sourabh.pal.mandi.databinding.FragmentSellAppleBinding

@AndroidEntryPoint
class SellAppleFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentSellAppleBinding? = null
    private val viewModel: SellAppleFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sell_apple, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        prepareForSearch()
    }

    private fun prepareForSearch() {
        (binding.enterNameWidget.editText as? AutoCompleteTextView)?.doAfterTextChanged {
            viewModel.onEvent(SellAppleEvent.OnNameUpdate(it?.toString().orEmpty()))
        }
    }

    private fun setupUI() {
        observeViewStateUpdates()
    }

    private fun createAdapter(sellers: List<String> = emptyList()): ArrayAdapter<String> {
        return ArrayAdapter<String>(requireContext(), R.layout.search_item, sellers)
    }

    private fun observeViewStateUpdates() {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it)
        }
    }

    private fun updateScreenState(newState: SellAppleViewState) {
        setupValuesFor(binding.nameEt, newState.sellerNameSuggestions.map { it.name })
    }

    private fun setupValuesFor(dropdown: AutoCompleteTextView, dropdownValues: List<String>) {
        dropdown.setAdapter(createAdapter(dropdownValues))
        //dropdown.setText(dropdownValues.first(), false)
    }
}