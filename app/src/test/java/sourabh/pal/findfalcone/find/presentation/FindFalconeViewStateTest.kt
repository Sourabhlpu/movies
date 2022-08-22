package sourabh.pal.findfalcone.find.presentation

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import org.junit.Test
import sourabh.pal.findfalcone.common.presentation.model.UIPlanet
import sourabh.pal.findfalcone.common.presentation.model.UIVehicle
import sourabh.pal.findfalcone.common.presentation.model.VehiclesForPlanet


class FindFalconeViewStateTest {

    private val planetsInitial by lazy {
        listOf(
            UIPlanet(
                name = "Donlon",
                distance = 100
            ),
            UIPlanet(
                name = "Enchai",
                distance = 200
            ),
/*            UIPlanet(
                name = "Jebing",
                distance = 300
            ),
            UIPlanet(
                name = "Sapir",
                distance = 400
            ),
            UIPlanet(
                name = "Lerbin",
                distance = 500
            ),
            UIPlanet(
                name = "Pingasor",
                distance = 600
            )*/
        )
    }

    private val vehiclesInitial by lazy {
        listOf(
            UIVehicle(
                "space pod",
                quantity = 2,
                range = 200,
                speed = 2
            ),
            UIVehicle(
                "space rocket",
                quantity = 1,
                range = 300,
                speed = 4
            )/*,
            UIVehicle(
                "space shuttle",
                quantity = 1,
                range = 400,
                speed = 5
            ),
            UIVehicle(
                "space ship",
                quantity = 2,
                range = 600,
                speed = 10
            )*/
        )
    }

    private val initialState = FindFalconeViewState(
        planets = planetsInitial,
        vehicles = vehiclesInitial
    )

    @Test
    fun `updateWhenPlanetsPageChanged when planets is empty`() {
        val exptectedState = FindFalconeViewState()
        val updatedState = exptectedState.updateWhenPlanetsPageChanged(any())
        assertThat(updatedState).isEqualTo(exptectedState)

    }

    @Test
    fun `updateWhenPlanets when first page is visible`() {
        val expectedState = initialState.copy(
            vehiclesForSelectedPlanet = VehiclesForPlanet(
                planetsInitial[0],
                vehiclesInitial
            )
        )
        val updatedState = initialState.updateWhenPlanetsPageChanged(0)
        assertThat(updatedState).isEqualTo(expectedState)
    }

    @Test
    fun `updateWhenPlanets when second page is visible`() {
        val expectedState = initialState.copy(
            vehiclesForSelectedPlanet = VehiclesForPlanet(
                planetsInitial[1],
                vehiclesInitial
            )
        )
        val updatedState = initialState.updateWhenPlanetsPageChanged(1)
        assertThat(updatedState).isEqualTo(expectedState)
    }

    @Test
    fun `updateWhenPlanets when last page is visible`() {
        val expectedState = initialState.copy(
            vehiclesForSelectedPlanet = VehiclesForPlanet(
                planetsInitial.last(),
                vehiclesInitial
            )
        )
        val updatedState = initialState.updateWhenPlanetsPageChanged(1)
        assertThat(updatedState).isEqualTo(expectedState)
    }

}