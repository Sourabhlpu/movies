package sourabh.pal.movies.movies.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        requestInitialMoviesList()
        prepareForSearch()
    }

    private fun requestInitialMoviesList() {
        viewModel.onEvent(MoviesEvent.RequestInitialMovieList)
    }

    private fun prepareForSearch() {
        setupSearchViewListener()
    }

    private fun setupSearchViewListener() {
        val searchView = binding.search
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.onEvent(MoviesEvent.QueryInput(query.orEmpty()))
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onEvent(MoviesEvent.QueryInput(newText.orEmpty()))
                    return true
                }
            }
        )
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
            MoviesFragmentViewModel.UI_PAGE_SIZE
        ) {
            override fun loadMoreItems() {
                requestMoreMovies()
            }

            override fun isLoading(): Boolean = viewModel.isLoadingMoreMovies
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun requestMoreMovies() {
        viewModel.onEvent(MoviesEvent.RequestMoreMovies)
    }

    private fun observeViewStateUpdates(adapter: MoviesAdapter) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    updateScreenState(it, adapter)
                }
            }
        }
    }

    private fun updateScreenState(state: MoviesViewState, adapter: MoviesAdapter) {
        val (
            inInitialState,
            searchResults,
            searchingRemotely,
            noResultState,
            noMoreMovies,
            failure) = state

        updateInitialStateViews(inInitialState)
        adapter.submitList(searchResults)
        updateRemoteSearchViews(searchingRemotely)
        updateNoResultsViews(noResultState)
        handleFailures(failure)
    }

    private fun updateNoResultsViews(noResultsState: Boolean) {
        binding.noSearchResultsImageView.isVisible = noResultsState
        binding.noSearchResultsText.isVisible = noResultsState
    }

    private fun updateRemoteSearchViews(searchingRemotely: Boolean) {
        binding.progressBar.isVisible = searchingRemotely
        binding.searchRemotelyText.isVisible = searchingRemotely
    }

    private fun updateInitialStateViews(inInitialState: Boolean) {
        binding.initialSearchImageView.isVisible = inInitialState
        binding.initialSearchText.isVisible = inInitialState
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}