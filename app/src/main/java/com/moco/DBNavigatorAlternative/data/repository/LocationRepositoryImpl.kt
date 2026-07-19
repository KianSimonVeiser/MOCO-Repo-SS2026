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

    override suspend fun getCurrentLocation(): Location? {
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
            /*
             * Auf Emulatoren ist lastLocation oft veraltet (USA-Koordinaten).
             * Wir versuchen daher direkt eine frische Abfrage mit hoher Priorität.
             */
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()
            
            // Falls das fehlschlägt, nehmen wir als Fallback den letzten bekannten Standort
            location ?: fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }
}
