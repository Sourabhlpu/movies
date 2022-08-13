package sourabh.pal.findfalcone.common.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import sourabh.pal.findfalcone.R
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle

class VehiclesAdapter(private val clickHandler: Any): BaseAdapter<UIVehicle>(DiffCallback(), clickHandler) {

    override fun getItemViewType(position: Int): Int  = R.layout.item_vehicle

    class DiffCallback: DiffUtil.ItemCallback<UIVehicle>(){

        override fun areItemsTheSame(oldItem: UIVehicle, newItem: UIVehicle): Boolean {
           return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: UIVehicle, newItem: UIVehicle): Boolean {
           return  oldItem == newItem
        }
    }
}