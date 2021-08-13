package com.example.hymnappconcept.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@Database(entities = [HymnEntity::class, HymnFts::class], version = 3, exportSchema = false)
abstract class HymnDatabase : RoomDatabase() {
    abstract fun hymnDao(): HymnDao

    companion object {

        @Volatile
        private var INSTANCE: HymnDatabase? = null

        fun getDatabase(context: Context): HymnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HymnDatabase::class.java,
                    "hymn.db"
                ).createFromAsset("mhb.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}