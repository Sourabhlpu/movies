package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet

data class FindFalconeViewState(
    val loading: Boolean = true,
    val planets: List<UIPlanet> = emptyList(),
    val failure: Event<Throwable>? = null
)