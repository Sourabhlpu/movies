package sourabh.pal.movies.common.data.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sourabh.pal.movies.common.data.cache.Cache
import sourabh.pal.movies.common.data.cache.MoviesDatabase
import sourabh.pal.movies.common.data.cache.RoomCache
import sourabh.pal.movies.common.data.cache.daos.MovieDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): MoviesDatabase {
            return Room.databaseBuilder(
                context,
                MoviesDatabase::class.java,
                "movies.db"
            )
                .build()
        }

        @Provides
        fun provideAnimalsDao(
            moviesDatabase: MoviesDatabase
        ): MovieDao = moviesDatabase.moviesDao()
    }
}