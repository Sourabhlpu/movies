package sourabh.pal.findfalcone.find.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import sourabh.pal.findfalcone.TestCoroutineRule
import sourabh.pal.findfalcone.common.domain.model.VehiclesAndPlanets
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository

@ExperimentalCoroutinesApi
class FindFalconeUsecaseTest{


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Test
    fun whenLocalTokenExists_returnPlanet() = testCoroutineRule.runBlockingTest{
        //Given
        val repository = mock<FindFalconeRepository>()
        val vehiclesAndPlanets = VehiclesAndPlanets(emptyList(), emptyList())
        val token = "aif2309z4920342"
        whenever(repository.getLocalToken()).thenReturn(token)

        //When
        FindFalconeUsecase(repository).invoke(emptyList(), emptyList())

        //Then
        verify(repository).findFalcone(vehiclesAndPlanets)
    }

    @Test
    fun whenLocalDoesNotTokenExist_returnPlanet() = testCoroutineRule.runBlockingTest{
        //Given
        val repository = mock<FindFalconeRepository>()
        val vehiclesAndPlanets = VehiclesAndPlanets(emptyList(), emptyList())
        whenever(repository.getLocalToken()).thenReturn("")

        //When
        FindFalconeUsecase(repository).invoke(emptyList(), emptyList())

        //Then
        inOrder(repository) {
            verify(repository).getToken()
            verify(repository).findFalcone(vehiclesAndPlanets)
        }
    }
}