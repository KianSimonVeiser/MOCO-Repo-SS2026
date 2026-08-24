package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.local.AppDatabase
import com.moco.DBNavigatorAlternative.data.repository.FavoriteRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.repository.FavoriteRepository
import com.moco.DBNavigatorAlternative.presentation.home.HomeViewModel

class DetailViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (!modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            throw IllegalArgumentException(
                "Unbekannte ViewModel-Klasse: ${modelClass.name}"
            )
        }

        val application = checkNotNull(
            extras[
                ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY
            ]
        )

        val database = AppDatabase.getInstance(application.applicationContext)
        val favoriteRepository: FavoriteRepository = FavoriteRepositoryImpl(database.favoriteConnectionDao)

        return DetailViewModel(
            dbNavApiService = HomeViewModel.dbNavApiServiceInstance,
            favoriteRepository = favoriteRepository
        ) as T
    }
}
