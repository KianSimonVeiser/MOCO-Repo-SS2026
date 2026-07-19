package com.moco.DBNavigatorAlternative.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
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

        return HomeViewModel(
            locationRepository = locationRepository,
            dbNavApiService = dbNavApiServiceInstance
        ) as T
    }
}