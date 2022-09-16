package sourabh.pal.mandi.common.data.preferences

class FakePreferences: Preferences {

    private val preferences = mutableMapOf<String, Any>()

    override fun putToken(token: String) {
        preferences[PreferencesConstants.KEY_TOKEN] = token
    }

    override fun getToken(): String {
        return preferences[PreferencesConstants.KEY_TOKEN] as String
    }
}