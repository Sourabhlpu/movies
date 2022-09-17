package sourabh.pal.mandi.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.getDrawableOrThrow
import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sourabh.pal.mandi.R


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

fun Context.getProgressBarDrawable(): Drawable {
    val value = TypedValue()
    theme.resolveAttribute(android.R.attr.progressBarStyleSmall, value, false)
    val progressBarStyle = value.data
    val attributes = intArrayOf(android.R.attr.indeterminateDrawable)
    val array = obtainStyledAttributes(progressBarStyle, attributes)
    val drawable = array.getDrawableOrThrow(0)
    array.recycle()
    return drawable
}

fun Double.toKg(): Double{
    return this * 1016.04691
}
