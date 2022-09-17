package sourabh.pal.mandi.sell.presentation

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ProgressBar
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import sourabh.pal.mandi.R
import sourabh.pal.mandi.common.utils.getProgressBarDrawable
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
        setSearchProgress(newState)

    }

    private fun setSearchProgress(newState: SellAppleViewState) {
        val isSearching = newState.isSearchingNames
        val textInput = binding.enterNameWidget
        textInput.endIconMode = if (isSearching) TextInputLayout.END_ICON_CUSTOM else TextInputLayout.END_ICON_CLEAR_TEXT
        if (isSearching) {
            val progressDrawable = requireContext().getProgressBarDrawable()
            textInput.endIconDrawable = progressDrawable
            textInput.endIconMode = TextInputLayout.END_ICON_CUSTOM
            (progressDrawable as? Animatable)?.start()
        }
    }

    private fun setupValuesFor(dropdown: AutoCompleteTextView, dropdownValues: List<String>) {
        dropdown.setAdapter(createAdapter(dropdownValues))
        //dropdown.setText(dropdownValues.first(), false)
    }
}