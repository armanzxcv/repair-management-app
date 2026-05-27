package com.armanaci.repairmanagement.data.repository

import com.armanaci.repairmanagement.data.database.RepairDao
import com.armanaci.repairmanagement.data.model.Repair
import kotlinx.coroutines.flow.Flow

class RepairRepository(private val repairDao: RepairDao) {

    suspend fun insertRepair(repair: Repair) {
        repairDao.insert(repair)
    }

    suspend fun updateRepair(repair: Repair) {
        repairDao.update(repair)
    }

    suspend fun deleteRepair(repair: Repair) {
        repairDao.delete(repair)
    }

    fun getAllRepairs(): Flow<List<Repair>> {
        return repairDao.getAllRepairs()
    }

    suspend fun getRepairById(id: Int): Repair? {
        return repairDao.getRepairById(id)
    }

    fun searchRepairs(query: String): Flow<List<Repair>> {
        return repairDao.searchRepairs("%$query%")
    }

    fun getTotalRepairsCount(): Flow<Int> {
        return repairDao.getTotalRepairsCount()
    }

    fun getCompletedRepairsCount(): Flow<Int> {
        return repairDao.getCompletedRepairsCount()
    }

    fun getPendingRepairsCount(): Flow<Int> {
        return repairDao.getPendingRepairsCount()
    }
}
