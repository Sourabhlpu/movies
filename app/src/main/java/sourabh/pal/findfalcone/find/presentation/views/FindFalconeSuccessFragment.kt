package sourabh.pal.findfalcone.find.presentation.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import sourabh.pal.findfalcone.R
import sourabh.pal.findfalcone.databinding.FragmentFindFalconeSuccessBinding
import sourabh.pal.findfalcone.databinding.FragmentPlanetSlidePageBinding
import sourabh.pal.findfalcone.find.presentation.FindFalconeFragmentViewModel


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


}