package sourabh.pal.findfalcone.common.utils

import android.widget.ArrayAdapter
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import sourabh.pal.findfalcone.R
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet

object FindFalconeBindingAdapter{

    @BindingAdapter("items")
    fun setAutocompleteAdapter(view: MaterialAutoCompleteTextView, items: List<UIPlanet>) {
        val planetsNameList = items.filter { it.isSelected }.map { it.name }
        val adapter = ArrayAdapter(view.context, R.layout.dropdown_item, planetsNameList)
        view.setAdapter(adapter)
    }

}