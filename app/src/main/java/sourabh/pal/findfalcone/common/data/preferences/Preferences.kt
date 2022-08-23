
package sourabh.pal.findfalcone.common.data.preferences

interface Preferences {
  fun putToken(token: String)
  fun getToken(): String
}