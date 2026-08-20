package com.moco.DBNavigatorAlternative.data

import androidx.compose.foundation.text.input.TextFieldState
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import java.text.SimpleDateFormat
import java.util.*

/**
 * Ein einfacher Singleton-Speicher, um Suchparameter über Screen-Wechsel hinweg zu erhalten.
 * Dies ermöglicht es, dass Eingaben im Home-Screen erhalten bleiben, wenn man zum Profil 
 * oder zur Suche und zurück navigiert.
 */
object SearchStateStore {
    val fromTextFieldState = TextFieldState()
    val toTextFieldState = TextFieldState()
    var fromLocation: NearbyLocationDto? = null
    var toLocation: NearbyLocationDto? = null
    
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    
    var date: String = dateFormatter.format(Date())
    var time: String = String.format(Locale.GERMANY, "%02d:%02d", 
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        Calendar.getInstance().get(Calendar.MINUTE))
        
    var isArrival: Boolean = false
    var onlyDTicket: Boolean = false
}
