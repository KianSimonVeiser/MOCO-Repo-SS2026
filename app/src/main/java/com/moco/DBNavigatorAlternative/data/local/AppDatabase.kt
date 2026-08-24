package com.moco.DBNavigatorAlternative.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moco.DBNavigatorAlternative.data.local.dao.FavoriteConnectionDao
import com.moco.DBNavigatorAlternative.data.local.entity.FavoriteConnectionEntity

@Database(entities = [FavoriteConnectionEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val favoriteConnectionDao: FavoriteConnectionDao

    companion object {
        const val DATABASE_NAME = "db_navigator_alt_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
