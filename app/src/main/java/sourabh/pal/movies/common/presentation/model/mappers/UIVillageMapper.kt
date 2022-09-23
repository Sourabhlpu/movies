package sourabh.pal.movies.common.presentation.model.mappers

import sourabh.pal.movies.common.domain.model.Village
import sourabh.pal.movies.common.presentation.model.UIVillage
import javax.inject.Inject

class UIVillageMapper @Inject constructor(): UiMapper<Village, UIVillage>{

    override fun mapToView(input: Village): UIVillage {
        return UIVillage(
            name = input.name,
            pricePerKgApple = input.pricePerKgApple
        )
    }
}