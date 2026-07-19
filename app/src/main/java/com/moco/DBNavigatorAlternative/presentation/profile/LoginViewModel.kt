package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Zustand des Login-Formulars.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val username: String = "" 
)

/**
 * ViewModel für die Login-Logik.
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(value: String) { _uiState.update { it.copy(email = value) } }
    fun onPasswordChanged(value: String) { _uiState.update { it.copy(password = value) } }

    /**
     * Prüft die Anmeldedaten gegen die Firestore-Datenbank.
     */
    fun login() {
        val state = _uiState.value
        
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Bitte E-Mail und Passwort eingeben") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                
                db.collection("users").document(state.email).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val dbPassword = document.getString("password")
                            val dbUsername = document.getString("username") ?: "Nutzer"
                            
                            if (dbPassword == state.password) {
                                // Nutzer im UserRepository setzen
                                val user = User(
                                    userId = state.email,
                                    username = dbUsername,
                                    email = state.email
                                )
                                UserRepository.setUser(user)

                                _uiState.update { 
                                    it.copy(
                                        isLoading = false, 
                                        isLoggedIn = true,
                                        username = dbUsername
                                    ) 
                                }
                            } else {
                                _uiState.update { it.copy(isLoading = false, errorMessage = "Falsches Passwort") }
                            }
                        } else {
                            _uiState.update { it.copy(isLoading = false, errorMessage = "Benutzer nicht gefunden") }
                        }
                    }
                    .addOnFailureListener { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Fehler: ${e.message}") }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
    
    fun resetLoginStatus() {
        _uiState.update { it.copy(isLoggedIn = false, password = "") }
    }

    /**
     * Meldet den Nutzer ab und löscht die Sitzung.
     */
    fun logout() {
        UserRepository.setUser(null)
        _uiState.update { LoginUiState() }
    }
}
