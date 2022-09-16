package sourabh.pal.mandi.common.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sourabh.pal.mandi.common.data.preferences.FindFalconePreferences
import sourabh.pal.mandi.common.data.preferences.Preferences

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

  @Binds
  abstract fun providePreferences(preferences: FindFalconePreferences): Preferences
}