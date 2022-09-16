package sourabh.pal.mandi.common.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import sourabh.pal.mandi.R
import sourabh.pal.mandi.common.presentation.model.UIVehicle
import sourabh.pal.mandi.common.presentation.model.UIVehicleWitDetails

class VehiclesAdapter(private val clickHandler: Any): BaseAdapter<UIVehicleWitDetails>(DiffCallback(), clickHandler) {

    override fun getItemViewType(position: Int): Int  = R.layout.item_vehicle

    class DiffCallback: DiffUtil.ItemCallback<UIVehicleWitDetails>(){

        override fun areItemsTheSame(oldItem: UIVehicleWitDetails, newItem: UIVehicleWitDetails): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UIVehicleWitDetails, newItem: UIVehicleWitDetails): Boolean {
           return  oldItem == newItem
        }
    }
}