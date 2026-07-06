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
            // Versuche erst den letzten bekannten Standort (schnell)
            var location: Location? = fusedLocationClient.lastLocation.await()
            
            // Wenn kein letzter Standort bekannt ist (oft auf Emulatoren), fordere einen neuen an
            if (location == null) {
                location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()
            }

            location?.let {
                /*getAddressFromLocation(it.latitude, it.longitude)*/
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
            // Versuche den Stadtnamen zu bekommen, sonst Fallback auf Koordinaten
            addresses?.firstOrNull()?.locality ?: "$lat, $lon"
        } catch (e: Exception) {
            // Bei Fehlern (z.B. kein Netzwerk) Koordinaten anzeigen
            "$lat, $lon"
        }
    }
}
