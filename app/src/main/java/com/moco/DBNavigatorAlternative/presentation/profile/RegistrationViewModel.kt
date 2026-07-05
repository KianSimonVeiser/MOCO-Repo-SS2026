package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Zustand des Registrierungsformulars.
 */
data class RegistrationUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

/**
 * ViewModel für die Registrierungs-Logik.
 * Speichert Nutzerdaten direkt in Firestore (unsicher, da Klartext).
 */
class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(value: String) { _uiState.update { it.copy(username = value) } }
    fun onEmailChanged(value: String) { _uiState.update { it.copy(email = value) } }
    fun onPasswordChanged(value: String) { _uiState.update { it.copy(password = value) } }
    fun onConfirmPasswordChanged(value: String) { _uiState.update { it.copy(confirmPassword = value) } }

    /**
     * Erstellt einen neuen Nutzer-Account in Firestore.
     */
    fun registerUser() {
        val state = _uiState.value
        
        // Einfache Validierung
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwörter stimmen nicht überein") }
            return
        }
        
        if (state.username.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Bitte alle Felder ausfüllen") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                
                // Wir erstellen ein einfaches Datenpaket für die Cloud
                val userData = hashMapOf(
                    "username" to state.username,
                    "email" to state.email,
                    "password" to state.password // WICHTIG: Das ist unsicher (Klartext)!
                )

                // Wir speichern den Nutzer unter seiner Email als ID ab
                db.collection("users")
                    .document(state.email)
                    .set(userData)
                    .addOnSuccessListener {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    }
                    .addOnFailureListener { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
    
    fun clearError() { _uiState.update { it.copy(errorMessage = null) } }
}
