package sourabh.pal.movies.common.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import sourabh.pal.movies.common.data.MandiRepositoryIml
import sourabh.pal.movies.common.domain.repositories.MandiRepository
import sourabh.pal.movies.common.utils.CoroutineDispatchersProvider
import sourabh.pal.movies.common.utils.DispatchersProvider

@Module
@InstallIn(ActivityRetainedComponent::class)

abstract class ActivityRetainedModule {

  @Binds
  @ActivityRetainedScoped
  abstract fun bindMandiRepository(repository: MandiRepositoryIml): MandiRepository

  @Binds
  abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider):
          DispatchersProvider

}