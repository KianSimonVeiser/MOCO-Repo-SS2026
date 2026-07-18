package com.moco.DBNavigatorAlternative.data

import com.moco.DBNavigatorAlternative.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton Repository zur Verwaltung der aktuellen Nutzersitzung.
 */
object UserRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    /**
     * Setzt den aktuell angemeldeten Nutzer.
     */
    fun setUser(user: User?) {
        _currentUser.value = user
    }

    /**
     * Prüft, ob ein Nutzer angemeldet ist.
     */
    fun isLoggedIn(): Boolean = _currentUser.value != null
}
