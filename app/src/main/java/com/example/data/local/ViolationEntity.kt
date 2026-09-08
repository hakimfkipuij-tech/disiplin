package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "violations")
data class ViolationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentName: String,
    val studentNis: String,
    val studentClass: String,
    val category: String,
    val violationTitle: String,
    val violationPoints: Int,
    val location: String,
    val reporterName: String,
    val incidentDate: String,
    val incidentTime: String,
    val description: String,
    val recommendedSanction: String,
    val requiresApproval: Boolean,
    val status: String, // "PENDING", "APPROVED", "REJECTED"
    val principalNote: String? = null,
    val principalDecisionDate: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
