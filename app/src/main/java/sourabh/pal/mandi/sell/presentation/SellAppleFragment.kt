package sourabh.pal.mandi.sell.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sourabh.pal.mandi.R
import sourabh.pal.mandi.databinding.FragmentSellAppleBinding
import sourabh.pal.mandi.find.presentation.FindFalconeFragmentViewModel

@AndroidEntryPoint
class SellAppleFragment: Fragment() {

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
    }
}