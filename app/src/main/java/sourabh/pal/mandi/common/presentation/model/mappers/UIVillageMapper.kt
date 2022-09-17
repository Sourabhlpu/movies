package sourabh.pal.mandi.common.presentation.model.mappers

import sourabh.pal.mandi.common.domain.model.Village
import sourabh.pal.mandi.common.presentation.model.UIVillage
import javax.inject.Inject

class UIVillageMapper @Inject constructor(): UiMapper<Village, UIVillage>{

    override fun mapToView(input: Village): UIVillage {
        return UIVillage(
            name = input.name,
            pricePerKgApple = input.pricePerKgApple
        )
    }
}