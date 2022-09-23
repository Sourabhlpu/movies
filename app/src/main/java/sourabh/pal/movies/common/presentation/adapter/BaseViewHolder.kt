package sourabh.pal.movies.common.presentation.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import sourabh.pal.movies.BR

class BaseViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, clickHandler: Any) {
        binding.setVariable(BR.item, item)
        binding.setVariable(BR.clickHandler, clickHandler)
        binding.executePendingBindings()
    }
}