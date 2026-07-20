package com.moco.DBNavigatorAlternative.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.moco.DBNavigatorAlternative.domain.repository.LocationRepository
import kotlinx.coroutines.tasks.await
import java.util.Locale

/**
 * Implementierung des LocationRepository unter Verwendung von Google Play Services.
 */
class LocationRepositoryImpl(
    private val context: Context
) : LocationRepository {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): String? {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        return try {
            // Abruf des zuletzt bekannten Standorts für eine schnelle Antwort
            var location: Location? = fusedLocationClient.lastLocation.await()
            
            // Fallback auf aktive Standortanfrage, falls kein Cache-Wert verfügbar ist
            if (location == null) {
                location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()
            }

            location?.let {
                "${it.latitude}, ${it.longitude}"
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getAddressFromLocation(lat: Double, lon: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            addresses?.firstOrNull()?.locality ?: "$lat, $lon"
        } catch (e: Exception) {
            "$lat, $lon"
        }
    }
}
