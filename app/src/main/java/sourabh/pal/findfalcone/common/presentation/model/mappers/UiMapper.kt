package sourabh.pal.findfalcone.common.presentation.model.mappers

interface UiMapper<E,V> {
    fun mapToView(input: E): V
}