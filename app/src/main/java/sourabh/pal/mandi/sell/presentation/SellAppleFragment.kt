package sourabh.pal.mandi.sell.presentation

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import sourabh.pal.mandi.R
import sourabh.pal.mandi.common.presentation.Event
import sourabh.pal.mandi.common.utils.getProgressBarDrawable
import sourabh.pal.mandi.databinding.FragmentSellAppleBinding
import sourabh.pal.mandi.find.presentation.views.FindFalconeFragmentDirections

@AndroidEntryPoint
class SellAppleFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentSellAppleBinding? = null
    private val viewModel: SellAppleFragmentViewModel by viewModels()
    private var isNameSelected = false

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
        viewModel.onEvent(SellAppleEvent.GetVillages)
        setupUI()
    }

    private fun setInputFieldListeners() {
        setTextChangeListener((binding.enterNameWidget.editText as? AutoCompleteTextView)) {
            if(it.isEmpty()) {
                viewModel.onEvent(SellAppleEvent.OnNameCleared)
                return@setTextChangeListener
            }
            if (isNameSelected) isNameSelected = false
            else viewModel.onEvent(SellAppleEvent.OnNameUpdate(it))
        }

        setTextChangeListener((binding.enterWeightWidget.editText)) {
            val weightDouble = it.toDoubleOrNull() ?: 0.00
            viewModel.onEvent(SellAppleEvent.OnWeightUpdate(weightDouble))
        }
        setTextChangeListener((binding.enterVillageWidget.editText as? AutoCompleteTextView)) {
            viewModel.onEvent(SellAppleEvent.OnVillageNameUpdate(it))
        }

        (binding.enterNameWidget.editText as? AutoCompleteTextView)?.setOnItemClickListener { parent, _, position, _ ->
            val name = parent?.getItemAtPosition(position).toString()
            isNameSelected = true
            viewModel.onEvent(SellAppleEvent.OnSubmitName(name))
        }

    }

    private fun setupUI() {
        binding.clickHandler = this
        setInputFieldListeners()
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
        setupValuesFor(binding.villageEt, newState.villages.map { it.name })
        setSearchProgress(newState)
        handleNavigation(newState.navigateOnSuccess)

        binding.enterLoyaltyCardIdWidget.editText?.setText(newState.loyaltyCardNumber)
        binding.tvLoyaltyIndex.text = getString(R.string.format_loyalty_index, newState.loyaltyIndex)
        binding.tvTotalPrice.text = newState.totalPrice
        binding.btnSell.isEnabled = newState.enableSubmitButton
        binding.loader.isVisible = newState.isSubmitting
    }

    private fun setSearchProgress(newState: SellAppleViewState) {
        val isSearching = newState.isSearchingNames
        val textInput = binding.enterNameWidget
        textInput.endIconMode =
            if (isSearching) TextInputLayout.END_ICON_CUSTOM else TextInputLayout.END_ICON_CLEAR_TEXT
        if (isSearching) {
            val progressDrawable = requireContext().getProgressBarDrawable()
            textInput.endIconDrawable = progressDrawable
            textInput.endIconMode = TextInputLayout.END_ICON_CUSTOM
            (progressDrawable as? Animatable)?.start()
        }
    }

    private fun setupValuesFor(dropdown: AutoCompleteTextView, dropdownValues: List<String>) {
        dropdown.setAdapter(createAdapter(dropdownValues))
    }

    private fun setTextChangeListener(
        inputField: EditText?,
        action: (text: String) -> Unit
    ) {
        inputField?.doAfterTextChanged {
            action(it?.toString().orEmpty())
        }
    }

    fun onSubmitClicked(){
        viewModel.onEvent(SellAppleEvent.SellProduce)
    }

    private fun handleNavigation(navigateToSuccess: Event<Triple<String, String, String>>?) {
        if(navigateToSuccess != null){
            val triple = navigateToSuccess.getContentIfNotHandled()
            val sellerName = triple?.first.orEmpty()
            val totalFormattedPrice = triple?.second.orEmpty()
            val weightInTonnes = triple?.third.orEmpty()
            val action =  SellAppleFragmentDirections.actionSellProduceToSuccessFragment(sellerName, totalFormattedPrice, weightInTonnes)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}