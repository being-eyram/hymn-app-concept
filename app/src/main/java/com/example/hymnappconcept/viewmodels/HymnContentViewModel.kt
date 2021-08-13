package com.example.hymnappconcept.viewmodels

import androidx.lifecycle.*
import com.example.hymnappconcept.database.HymnEntity
import com.example.hymnappconcept.repository.HymnRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HymnContentViewModel(private val repository: HymnRepository) : ViewModel() {

    private val _result = MutableLiveData<HymnEntity>()
    val result: LiveData<HymnEntity>
        get() = _result

    fun getHymn(id: Int) {
        viewModelScope.launch {
            val hymn = repository.getHymn(id)
            withContext(Dispatchers.Main) {
                _result.value = hymn
            }
        }
    }
}

class HymnContentViewModelFactory(private val repository: HymnRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HymnContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HymnContentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}