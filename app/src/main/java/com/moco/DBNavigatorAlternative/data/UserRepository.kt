package com.moco.DBNavigatorAlternative.data

import android.annotation.SuppressLint
import android.content.Context
import com.moco.DBNavigatorAlternative.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// # Nutzer-Repository
// Ein einfacher Speicher für die aktuelle Nutzersitzung.
@SuppressLint("StaticFieldLeak")
object UserRepository {
    var context: Context? = null

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Den angemeldeten Nutzer speichern
    fun setUser(user: User?) {
        _currentUser.value = user
    }

    // Kurz nachschauen, ob gerade jemand eingeloggt ist
    fun isLoggedIn(): Boolean = _currentUser.value != null
}

