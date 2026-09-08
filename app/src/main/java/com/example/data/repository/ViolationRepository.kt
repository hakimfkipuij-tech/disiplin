package com.example.data.repository

import com.example.data.local.ViolationDao
import com.example.data.local.ViolationEntity
import kotlinx.coroutines.flow.Flow

class ViolationRepository(private val violationDao: ViolationDao) {
    val allViolations: Flow<List<ViolationEntity>> = violationDao.getAllViolations()
    val pendingApprovals: Flow<List<ViolationEntity>> = violationDao.getPendingApprovals()

    suspend fun getViolationById(id: Long): ViolationEntity? {
        return violationDao.getViolationById(id)
    }

    suspend fun insertViolation(violation: ViolationEntity): Long {
        return violationDao.insertViolation(violation)
    }

    suspend fun updateApprovalStatus(id: Long, status: String, note: String?, decisionDate: String) {
        violationDao.updateApprovalStatus(id, status, note, decisionDate)
    }

    suspend fun updateViolation(violation: ViolationEntity) {
        violationDao.updateViolation(violation)
    }

    suspend fun deleteViolation(id: Long) {
        violationDao.deleteViolation(id)
    }
}
