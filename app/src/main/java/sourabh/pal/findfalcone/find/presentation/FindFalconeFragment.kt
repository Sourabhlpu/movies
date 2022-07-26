package sourabh.pal.findfalcone.find.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sourabh.pal.findfalcone.databinding.FragmentFindFalconeBinding

class FindFalconeFragment: Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFindFalconeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindFalconeBinding.inflate(inflater, container, false)
        return binding.root
    }
}

