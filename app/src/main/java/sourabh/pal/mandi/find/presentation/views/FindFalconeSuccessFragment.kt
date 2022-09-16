package sourabh.pal.mandi.find.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sourabh.pal.mandi.R
import sourabh.pal.mandi.databinding.FragmentFindFalconeSuccessBinding
import sourabh.pal.mandi.databinding.FragmentPlanetSlidePageBinding
import sourabh.pal.mandi.find.presentation.FindFalconeFragmentViewModel


class FindFalconeSuccessFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFindFalconeSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_falcone_success, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val planet = navArgs<FindFalconeSuccessFragmentArgs>().value.planetName
        val totalTime = navArgs<FindFalconeSuccessFragmentArgs>().value.timeTaken
        binding.tvPlanet.text = getString(R.string.found_on_format, planet)
        binding.tvTimeTaken.text = getString(R.string.time_taken_format, totalTime)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(FindFalconeSuccessFragmentDirections.actionSuccessFragmentToFindFalconeFragment())
            }
        })
    }


}