package sourabh.pal.mandi.common.data.api

import android.content.Context
import android.net.*
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject



class ConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var _isConnected = false
    val isConnected
        get() = _isConnected

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected = true
            super.onAvailable(network)
        }

        override fun onLost(network: Network) {
            _isConnected = false
            super.onLost(network)
        }
    }

    init {
        val connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}