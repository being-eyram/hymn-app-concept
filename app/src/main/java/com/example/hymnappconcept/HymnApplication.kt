package com.example.hymnappconcept

import android.app.Application
import com.example.hymnappconcept.database.HymnDatabase
import com.example.hymnappconcept.repository.HymnRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HymnApplication : Application() {
    private val database by lazy { HymnDatabase.getDatabase(this) }
    val repository by lazy { HymnRepository(database.hymnDao()) }
}