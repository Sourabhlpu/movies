package sourabh.pal.findfalcone.find.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sourabh.pal.findfalcone.R
import sourabh.pal.findfalcone.databinding.FragmentFindFalconeBinding


class FindFalconeFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFindFalconeBinding? = null

    private val viewModel: FindFalconeFragmentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_falcone, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.viewModel = viewModel
        setDropDownListeners(binding.dropdownWidget1.autoCompleteTextView, 0)
        setDropDownListeners(binding.dropdownWidget2.autoCompleteTextView, 1)
        setDropDownListeners(binding.dropdownWidget3.autoCompleteTextView, 2)
        setDropDownListeners(binding.dropdownWidget4.autoCompleteTextView, 3)
        observerViewStateUpdates()
    }

    private fun observerViewStateUpdates() {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreen(it)
        }
    }

    private fun updateScreen(state: FindFalconeViewState) {
        updateDropdownList(state)
    }

    private fun updateDropdownList(state: FindFalconeViewState) {
        val adapter = binding.dropdownWidget1.autoCompleteTextView.adapter
        adapter?.let {
            (adapter as ArrayAdapter<String>)
                .apply {
                    clear()
                    addAll(state.planetsName)
                }
        }
    }

    private fun setDropDownListeners(dropDown: AutoCompleteTextView, dropdownIndex: Int) {
        dropDown.doOnTextChanged { text, _, _, _ ->
            viewModel.onEvent(
                FindFalconeEvent.PlanetSelected(
                    text.toString(),
                    dropdownIndex
                )
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



