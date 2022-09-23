package sourabh.pal.movies.movies.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sourabh.pal.movies.R
import sourabh.pal.movies.common.presentation.Event
import sourabh.pal.movies.common.presentation.adapter.InfiniteScrollListener
import sourabh.pal.movies.common.presentation.adapter.MoviesAdapter
import sourabh.pal.movies.databinding.FragmentMoviesBinding


@AndroidEntryPoint
class MoviesFragment : Fragment() {

    companion object {
        private const val ITEMS_PER_ROW = 2
    }

    private val viewModel: MoviesFragmentViewModel by viewModels()
    private val binding get() = _binding!!

    private var _binding: FragmentMoviesBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        requestInitialMoviesList()
    }

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        observeViewStateUpdates(adapter)
    }

    private fun createAdapter(): MoviesAdapter {
        return MoviesAdapter(this)
    }

    private fun setupRecyclerView(moviesAdapter: MoviesAdapter) {
        binding.moviesRecyclerView.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(requireContext(), ITEMS_PER_ROW)
            setHasFixedSize(true)
            addOnScrollListener(createInfiniteScrollListener(layoutManager as GridLayoutManager))
        }
    }

    private fun createInfiniteScrollListener(
        layoutManager: GridLayoutManager
    ): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListener(
            layoutManager,
            AnimalsNearYouFragmentViewModel.UI_PAGE_SIZE
        ) {
            override fun loadMoreItems() { requestMoreAnimals() }
            override fun isLoading(): Boolean = viewModel.isLoadingMoreAnimals
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun requestMoreAnimals() {
        viewModel.onEvent(MoviesEvent.RequestMoreMovies)
    }

    private fun observeViewStateUpdates(adapter: MoviesAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it, adapter)
        }
    }

    private fun updateScreenState(state: MoviesViewState, adapter: MoviesAdapter) {
        binding.progressBar.isVisible = state.loading
        adapter.submitList(state.animals)
        handleNoMoreAnimalsNearby(state.noMoreAnimalsNearby)
        handleFailures(state.failure)
    }

    private fun handleNoMoreAnimalsNearby(noMoreAnimalsNearby: Boolean) {
        // Show a warning message and a prompt for the user to try a different
        // distance or postcode
    }

    private fun handleFailures(failure: Event<Throwable>?) {
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return

        val fallbackMessage = getString(R.string.an_error_occurred)

        val snackbarMessage = if (unhandledFailure.message.isNullOrEmpty()) {
            fallbackMessage
        }
        else {
            unhandledFailure.message!! }
        if (snackbarMessage.isNotEmpty()) {
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun requestInitialMoviesList() {
        viewModel.onEvent(MoviesEvent.RequestInitialMovieList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}