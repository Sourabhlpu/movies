package sourabh.pal.findfalcone.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import sourabh.pal.findfalcone.common.data.FindFalconeRepositoryIml
import sourabh.pal.findfalcone.common.domain.repositories.FindFalconeRepository
import sourabh.pal.findfalcone.common.utils.CoroutineDispatchersProvider
import sourabh.pal.findfalcone.common.utils.DispatchersProvider

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

  @Binds
  @ActivityRetainedScoped
  abstract fun bindFindFalconeRepository(repository: FindFalconeRepositoryIml): FindFalconeRepository

  @Binds
  abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider):
          DispatchersProvider

}