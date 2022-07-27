package sourabh.pal.findfalcone.find.presentation

import sourabh.pal.findfalcone.common.presentation.Event
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet

data class FindFalconeViewState(
    val loading: Boolean = true,
    val planets: List<UIPlanet> = listOf(
        UIPlanet("Donlon", 100),
        UIPlanet("Enchai", 234),
        UIPlanet("Jebing", 234),
        UIPlanet("Sapir", 234),
        UIPlanet("Lerbin", 234),
        UIPlanet("Pingasor",234)
    ),
    val failure: Event<Throwable>? = null
)