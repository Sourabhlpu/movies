package sourabh.pal.movies.common.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import sourabh.pal.movies.R
import sourabh.pal.movies.common.presentation.model.UIMovie

class MoviesAdapter(private val clickHandler: Any): BaseAdapter<UIMovie>(DiffCallback(), clickHandler) {

    override fun getItemViewType(position: Int): Int  = R.layout.recycler_view_movie_item

    class DiffCallback: DiffUtil.ItemCallback<UIMovie>(){

        override fun areItemsTheSame(oldItem: UIMovie, newItem: UIMovie): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UIMovie, newItem: UIMovie): Boolean {
           return  oldItem == newItem
        }
    }
}