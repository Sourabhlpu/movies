
package sourabh.pal.mandi.common.data.preferences

interface Preferences {
  fun putToken(token: String)
  fun getToken(): String
}