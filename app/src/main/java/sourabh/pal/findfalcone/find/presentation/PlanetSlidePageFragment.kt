package sourabh.pal.findfalcone.find.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sourabh.pal.findfalcone.R
import sourabh.pal.findfalcone.databinding.FragmentPlanetSlidePageBinding

class PlanetSlidePageFragment(private val position: Int) : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentPlanetSlidePageBinding? = null
    private val viewModel: FindFalconeFragmentViewModel1 by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_planet_slide_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setClickListeners()
        observerViewStateUpdates()
    }

    private fun setClickListeners() {
        binding.card.apply {
            setOnClickListener {
                viewModel.onEvent(FindFalconeEvent.PlanetSelected( !isChecked, selectedIndex = position))
            }
        }
    }

    private fun observerViewStateUpdates() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if(state.planets.isNotEmpty()){
                binding.planetName = state.planets.map { it.name }[position]
                binding.card.isChecked = state.planets[position].isSelected
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}