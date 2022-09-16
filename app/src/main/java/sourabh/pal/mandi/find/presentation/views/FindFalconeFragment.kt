package sourabh.pal.mandi.find.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.mockito.internal.matchers.Find
import sourabh.pal.mandi.R
import sourabh.pal.mandi.common.presentation.Event
import sourabh.pal.mandi.common.presentation.ScreenSlidePagerAdapter
import sourabh.pal.mandi.common.presentation.ZoomOutPageTransformer
import sourabh.pal.mandi.common.presentation.adapter.VehiclesAdapter
import sourabh.pal.mandi.common.presentation.model.UIVehicleWitDetails
import sourabh.pal.mandi.databinding.FragmentFindFalconeBinding
import sourabh.pal.mandi.find.presentation.FindFalconeEvent
import sourabh.pal.mandi.find.presentation.FindFalconeFragmentViewModel
import sourabh.pal.mandi.find.presentation.FindFalconeViewState


@AndroidEntryPoint
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
        requestInitialData()
    }

    private fun requestInitialData() {
        viewModel.onEvent(FindFalconeEvent.GetPlanetsAndVehicles)
    }

    private fun setupUI() {
        setBindings()
        val viewPagerAdapter = createViewPagerAdapter()
        val vehiclesAdapter = createVehiclesAdapter()
        setViewPager(viewPagerAdapter)
        setRecyclerView(vehiclesAdapter)
        observerViewStateUpdates(vehiclesAdapter)
    }

    fun onVehicleClicked(vehicle: UIVehicleWitDetails){
      viewModel.onEvent(FindFalconeEvent.OnVehicleClicked(vehicle))
    }

    fun onSubmitClicked(){
        viewModel.onEvent(FindFalconeEvent.Submit)
    }

    private fun setBindings() {
        binding.viewModel = viewModel
        binding.clickHandler = this
    }

    private fun setRecyclerView(vehiclesAdapter: VehiclesAdapter) {
        binding.rvPlanets.apply {
            adapter = vehiclesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setViewPager(vpAdapter: ScreenSlidePagerAdapter) {
        binding.pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.onEvent(FindFalconeEvent.OnPageSelected(position))
            }
        })
        binding.pager.apply {
            adapter = vpAdapter
            setPageTransformer(ZoomOutPageTransformer())
        }

    }

    private fun createViewPagerAdapter(): ScreenSlidePagerAdapter {
        return ScreenSlidePagerAdapter(this)
    }

    private fun createVehiclesAdapter(): VehiclesAdapter {
        return VehiclesAdapter(this)
    }

    private fun observerViewStateUpdates(vehiclesAdapter: VehiclesAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreen(it, vehiclesAdapter)
            handleFailures(it.failure)
            handleNavigation(it.navigateToSuccess)
        }
    }

    private fun handleNavigation(navigateToSuccess: Event<Pair<String, String>>?) {
        if(navigateToSuccess != null){
            val pair = navigateToSuccess.getContentIfNotHandled()
            val planet = pair?.first.orEmpty()
            val totalTime = pair?.second.orEmpty()
            val action =  FindFalconeFragmentDirections.actionFindFalconeFragmentToSuccessFragment(planet, totalTime)
            findNavController().navigate(action)
        }
    }

    private fun updateScreen(state: FindFalconeViewState, vehiclesAdapter: VehiclesAdapter) {
        vehiclesAdapter.submitList(state.vehiclesForCurrentPlanet)
        binding.rvPlanets.isVisible = state.showVehicles
        binding.submitBtn.isEnabled = state.enableButton
        binding.loader.isVisible = state.loading
        binding.tvTime.text = getString(R.string.format_time, state.totalTime)
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