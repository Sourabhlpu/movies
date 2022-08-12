package sourabh.pal.findfalcone.common.utils

import android.util.DisplayMetrics
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import sourabh.pal.findfalcone.R

@BindingAdapter("adapter")
fun AutoCompleteTextView.setAdapter(items: List<String>) {
    val adapter = ArrayAdapter(context, R.layout.dropdown_item, items)
    adapter.setNotifyOnChange(true)
    setAdapter(adapter)
}

fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

fun Int.pxToDp(displayMetrics: DisplayMetrics): Int = (this / displayMetrics.density).toInt()
