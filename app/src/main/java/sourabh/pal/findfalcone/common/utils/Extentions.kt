package sourabh.pal.findfalcone.common.utils

import android.util.DisplayMetrics
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sourabh.pal.findfalcone.R


inline fun CoroutineScope.createExceptionHandler(
    message: String,
    crossinline action: (throwable: Throwable) -> Unit
) = CoroutineExceptionHandler { _, throwable ->

    throwable.printStackTrace()

    /**
     * A [CoroutineExceptionHandler] can be called from any thread. So, if [action] is supposed to
     * run in the main thread, you need to be careful and call this function on the a scope that
     * runs in the main thread, such as a [viewModelScope].
     */

    /**
     * A [CoroutineExceptionHandler] can be called from any thread. So, if [action] is supposed to
     * run in the main thread, you need to be careful and call this function on the a scope that
     * runs in the main thread, such as a [viewModelScope].
     */
    launch {
        action(throwable)
    }
}
