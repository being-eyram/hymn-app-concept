package com.example.hymnappconcept.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.hymnappconcept.database.HymnEntity
import com.example.hymnappconcept.repository.HymnRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HymnViewModel(private val repository: HymnRepository) : ViewModel() {

    private var _result = MutableLiveData<List<HymnEntity>>()
    val result: LiveData<List<HymnEntity>>
        get() = _result

    init {
         viewModelScope.launch {
            val result = repository.allHymns()
            _result.value = result
        }
    }

    fun search (query : String) {
        viewModelScope.launch {
            val result = repository.search("*$query*")
            withContext(Dispatchers.Main){
                _result.value = result
                Log.i("Search", "${_result.value}")
            }
        }
    }

}

class HymnViewModelFactory(private val repository: HymnRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HymnViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HymnViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}