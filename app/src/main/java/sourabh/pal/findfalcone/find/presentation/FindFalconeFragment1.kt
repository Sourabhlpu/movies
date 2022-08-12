package sourabh.pal.findfalcone.find.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import sourabh.pal.findfalcone.R
import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.ScreenSlidePagerAdapter
import sourabh.pal.findfalcone.common.presentation.ZoomOutPageTransformer
import sourabh.pal.findfalcone.databinding.FragmentFindFalcone1Binding


class FindFalconeFragment1 : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentFindFalcone1Binding? = null
    private val viewModel: FindFalconeFragmentViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_falcone1, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.viewModel = viewModel
        val adapter = createAdapter()
        setViewPager(adapter)
        observerViewStateUpdates()
    }

    private fun setViewPager(vpAdapter: ScreenSlidePagerAdapter) {
        binding.pager.apply {
            adapter = vpAdapter
            setPageTransformer(ZoomOutPageTransformer())
        }

    }

    private fun createAdapter(): ScreenSlidePagerAdapter {
        return ScreenSlidePagerAdapter(this)
    }

    private fun observerViewStateUpdates() {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreen(it)
            handleFailures(it.failure)
        }
    }

    private fun updateScreen(state: FindFalconeViewState) {

    }

    private fun handleFailures(failure: Event<Throwable>?) {
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return

        val fallbackMessage = getString(R.string.an_error_occurred)

        val snackbarMessage = if (unhandledFailure.message.isNullOrEmpty()) {
            fallbackMessage
        } else {
            unhandledFailure.message!!
        }
        if (snackbarMessage.isNotEmpty()) {
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}