package sourabh.pal.mandi.common.data.api.interceptors

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response
import sourabh.pal.mandi.common.data.api.ConnectionManager
import sourabh.pal.mandi.common.domain.NetworkUnavailableException
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(private val connectionManager: ConnectionManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected) {
            chain.proceed(chain.request())
        } else {
            throw NetworkUnavailableException()
        }
    }
}