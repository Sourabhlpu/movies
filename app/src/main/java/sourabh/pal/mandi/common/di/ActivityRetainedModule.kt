package sourabh.pal.mandi.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import sourabh.pal.mandi.common.data.FindFalconeRepositoryIml
import sourabh.pal.mandi.common.domain.repositories.FindFalconeRepository
import sourabh.pal.mandi.common.utils.CoroutineDispatchersProvider
import sourabh.pal.mandi.common.utils.DispatchersProvider

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