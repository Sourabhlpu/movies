
package sourabh.pal.mandi.common.data.preferences

import android.content.Context
import android.content.SharedPreferences
import sourabh.pal.mandi.common.data.preferences.PreferencesConstants.KEY_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindFalconePreferences @Inject constructor(
    @ApplicationContext context: Context
): Preferences {

  companion object {
    const val PREFERENCES_NAME = "FIND_FALCONE_PREFERENCES"
  }

  private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

  override fun putToken(token: String) {
    edit { putString(KEY_TOKEN, token) }
  }



  private inline fun edit(block: SharedPreferences.Editor.() -> Unit) {
    with(preferences.edit()) {
      block()
      commit()
    }
  }

  override fun getToken(): String {
    return preferences.getString(KEY_TOKEN, "").orEmpty()
  }

}