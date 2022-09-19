package sourabh.pal.mandi.sell.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sourabh.pal.mandi.R
import sourabh.pal.mandi.databinding.FragmentSuccessBinding


class SuccessFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_success, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sellerName = navArgs<SuccessFragmentArgs>().value.sellerName
        val totalPrice = navArgs<SuccessFragmentArgs>().value.totalPrice
        val totalWeight = navArgs<SuccessFragmentArgs>().value.totalWeight

        val thankYouMessage = getString(R.string.format_success_message, sellerName)
        val subSuccessMessage = getString(R.string.format_message_ensure, totalPrice, totalWeight)

        binding.tvThanks.text = thankYouMessage
        binding.tvEnsure.text = subSuccessMessage

        binding.btnSellMore.setOnClickListener {
            val action =  SuccessFragmentDirections.actionSuccessFragmentToSellAppleFragment()
            findNavController().navigate(action)
        }
    }

}