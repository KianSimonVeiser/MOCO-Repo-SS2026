package com.moco.DBNavigatorAlternative.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.local.AppDatabase
import com.moco.DBNavigatorAlternative.data.local.SettingsPreference
import com.moco.DBNavigatorAlternative.data.repository.FavoriteRepositoryImpl
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.repository.FavoriteRepository
import com.moco.DBNavigatorAlternative.domain.repository.LocationRepository
import com.moco.DBNavigatorAlternative.presentation.home.HomeViewModel.Companion.dbNavApiServiceInstance

class HomeViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (!modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            throw IllegalArgumentException(
                "Unbekannte ViewModel-Klasse: ${modelClass.name}"
            )
        }

        val application = checkNotNull(
            extras[
                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
            ]
        )

        val locationRepository: LocationRepository =
            LocationRepositoryImpl(
                application.applicationContext
            )
            
        val settingsPreference = SettingsPreference(application.applicationContext)
        
        val database = AppDatabase.getInstance(application.applicationContext)
        val favoriteRepository: FavoriteRepository = FavoriteRepositoryImpl(database.favoriteConnectionDao)

        return HomeViewModel(
            locationRepository = locationRepository,
            dbNavApiService = dbNavApiServiceInstance,
            favoriteRepository = favoriteRepository,
            settingsPreference = settingsPreference
        ) as T
    }
}
