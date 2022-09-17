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
        val sellerName = navArgs<FindFalconeSuccessFragmentArgs>().value.sellerName
        val totalPrice = navArgs<FindFalconeSuccessFragmentArgs>().value.totalPrice
        val totalWeight = navArgs<FindFalconeSuccessFragmentArgs>().value.totalWeight

        val thankYouMessage = getString(R.string.format_success_message, sellerName)
        val subSuccessMessage = getString(R.string.format_message_ensure, totalPrice, totalWeight)

        binding.tvThanks.text = thankYouMessage
        binding.tvEnsure.text = subSuccessMessage
    }

}