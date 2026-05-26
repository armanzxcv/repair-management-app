package com.armanaci.repairmanagement.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.armanaci.repairmanagement.data.database.RepairDatabase
import com.armanaci.repairmanagement.data.model.Repair
import com.armanaci.repairmanagement.data.repository.RepairRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RepairViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RepairRepository
    
    val allRepairs: Flow<List<Repair>>
    val totalRepairsCount: Flow<Int>
    val completedRepairsCount: Flow<Int>
    val pendingRepairsCount: Flow<Int>

    private val _searchResults = MutableStateFlow<List<Repair>>(emptyList())
    val searchResults: StateFlow<List<Repair>> = _searchResults

    private val _operationStatus = MutableStateFlow<String>("")
    val operationStatus: StateFlow<String> = _operationStatus

    init {
        val db = RepairDatabase.getDatabase(application)
        val repairDao = db.repairDao()
        repository = RepairRepository(repairDao)

        allRepairs = repository.getAllRepairs()
        totalRepairsCount = repository.getTotalRepairsCount()
        completedRepairsCount = repository.getCompletedRepairsCount()
        pendingRepairsCount = repository.getPendingRepairsCount()
    }

    fun insertRepair(repair: Repair) = viewModelScope.launch {
        try {
            repository.insertRepair(repair)
            _operationStatus.value = "تعمیر با موفقیت ثبت شد"
        } catch (e: Exception) {
            _operationStatus.value = "خطا: ${e.message}"
        }
    }

    fun updateRepair(repair: Repair) = viewModelScope.launch {
        try {
            repository.updateRepair(repair)
            _operationStatus.value = "تعمیر با موفقیت بروزرسانی شد"
        } catch (e: Exception) {
            _operationStatus.value = "خطا: ${e.message}"
        }
    }

    fun deleteRepair(repair: Repair) = viewModelScope.launch {
        try {
            repository.deleteRepair(repair)
            _operationStatus.value = "تعمیر با موفقیت حذف شد"
        } catch (e: Exception) {
            _operationStatus.value = "خطا: ${e.message}"
        }
    }

    fun searchRepairs(query: String) = viewModelScope.launch {
        try {
            repository.searchRepairs(query).collect {
                _searchResults.value = it
            }
        } catch (e: Exception) {
            _operationStatus.value = "خطا در جستجو: ${e.message}"
        }
    }

    fun clearStatus() {
        _operationStatus.value = ""
    }
}
