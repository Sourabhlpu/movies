package sourabh.pal.movies.common.presentation.adapter

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteScrollListener(
    private val layoutManager: GridLayoutManager,
    private val pageSize: Int
) :
    RecyclerView.OnScrollListener() {

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val visibleItemCount = layoutManager.childCount
    val totalItemCount = layoutManager.itemCount
    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

      Log.d("Scroll Listener", "isNotLoading = ${!isLoading()}  isNotLastPage =  ${!isLastPage()}")
    if (!isLoading() && !isLastPage()) {
        Log.d("Scroll Listener", "areAllItemsVisible ${(visibleItemCount + firstVisibleItemPosition) >= totalItemCount}")
        Log.d("Scroll Listener", "Second Condition? ${ firstVisibleItemPosition >= 0}")
        Log.d("Scroll Listener", "third Condition? ${ totalItemCount >= pageSize }")

        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= pageSize
      ) {
        loadMoreItems()
      }
    }
  }

  abstract fun loadMoreItems()

  abstract fun isLastPage(): Boolean

  abstract fun isLoading(): Boolean
}