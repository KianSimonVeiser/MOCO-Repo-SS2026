package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.domain.model.LineComment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Zustand des Einstellungsbildschirms.
 */
data class SettingsUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null,
    val userComments: List<LineComment> = emptyList() // NEU: Liste der eigenen Kommentare
)

/**
 * ViewModel zur Verwaltung der Benutzereinstellungen in Firebase.
 */
class SettingsViewModel(
    private val interactionRepository: InteractionRepository = InteractionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private var userEmail: String = ""

    /**
     * Initialisiert das ViewModel mit den aktuellen Nutzerdaten.
     */
    fun initUserData(username: String, email: String) {
        userEmail = email
        _uiState.update { it.copy(username = username) }
        loadUserComments() // Lädt die Kommentare beim Start
    }

    private fun loadUserComments() {
        if (userEmail.isBlank()) return
        viewModelScope.launch {
            val comments = interactionRepository.getCommentsForUser(userEmail)
            _uiState.update { it.copy(userComments = comments) }
        }
    }

    fun onUsernameChanged(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun onPasswordChanged(newPass: String) {
        _uiState.update { it.copy(password = newPass) }
    }

    /**
     * Aktualisiert alle geänderten Profildaten in einem Rutsch.
     */
    fun saveAllSettings() {
        if (userEmail.isBlank()) return
        
        _uiState.update { it.copy(isLoading = true, isSuccess = false, errorMessage = null) }

        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val updates = mutableMapOf<String, Any>()
                
                // Wir aktualisieren nur Felder, die nicht leer sind
                updates["username"] = _uiState.value.username
                if (_uiState.value.password.isNotBlank()) {
                    updates["password"] = _uiState.value.password
                }

                db.collection("users").document(userEmail)
                    .update(updates)
                    .addOnSuccessListener {
                        _uiState.update { it.copy(isLoading = false, isSuccess = true, password = "") }
                    }
                    .addOnFailureListener { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    /**
     * Löscht das Nutzerdokument unwiderruflich aus Firestore.
     */
    fun deleteAccount() {
        if (userEmail.isBlank()) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                db.collection("users").document(userEmail)
                    .delete()
                    .addOnSuccessListener {
                        _uiState.update { it.copy(isLoading = false, isDeleted = true) }
                    }
                    .addOnFailureListener { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    /**
     * Löscht alle Kommentare des Nutzers.
     */
    fun deleteUserComments() {
        if (userEmail.isBlank()) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            interactionRepository.clearAllUserComments(userEmail)
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    isSuccess = true,
                    userComments = emptyList() 
                ) 
            }
        }
    }

    /**
     * Löscht einen einzelnen Kommentar.
     */
    fun deleteSingleComment(commentId: String) {
        viewModelScope.launch {
            interactionRepository.deleteComment(commentId)
            loadUserComments() // Liste aktualisieren
        }
    }

    fun deleteFavoriteConnections() {
        if (userEmail.isBlank()) return
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            interactionRepository.clearAllFavorites(userEmail)
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    isSuccess = true,
                    errorMessage = null 
                ) 
            }
        }
    }

    fun clearStatus() {
        _uiState.update { it.copy(isSuccess = false, errorMessage = null) }
    }
}
