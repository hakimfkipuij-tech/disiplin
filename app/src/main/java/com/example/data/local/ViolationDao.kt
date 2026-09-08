package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ViolationDao {
    @Query("SELECT * FROM violations ORDER BY timestamp DESC")
    fun getAllViolations(): Flow<List<ViolationEntity>>

    @Query("SELECT * FROM violations WHERE requiresApproval = 1 AND status = 'PENDING' ORDER BY timestamp DESC")
    fun getPendingApprovals(): Flow<List<ViolationEntity>>

    @Query("SELECT * FROM violations WHERE id = :id")
    suspend fun getViolationById(id: Long): ViolationEntity?

    @Query("SELECT COUNT(*) FROM violations")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViolation(violation: ViolationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViolations(violations: List<ViolationEntity>)

    @Update
    suspend fun updateViolation(violation: ViolationEntity)

    @Query("UPDATE violations SET status = :status, principalNote = :note, principalDecisionDate = :decisionDate WHERE id = :id")
    suspend fun updateApprovalStatus(id: Long, status: String, note: String?, decisionDate: String)

    @Query("DELETE FROM violations WHERE id = :id")
    suspend fun deleteViolation(id: Long)
}
