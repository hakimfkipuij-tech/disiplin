package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ViolationEntity
import com.example.data.repository.ViolationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FormState(
    val studentName: String = "",
    val studentNis: String = "",
    val studentClass: String = "X MIPA 1",
    val category: String = "Kedisiplinan & Keterlambatan",
    val violationTitle: String = "",
    val violationPoints: Int = 15,
    val location: String = "Ruang Kelas",
    val reporterName: String = "Guru Piket / Tim Tatib",
    val incidentDate: String = "",
    val incidentTime: String = "",
    val description: String = "",
    val recommendedSanction: String = "",
    val requiresApproval: Boolean = true,
    val isSubmittedSuccessfully: Boolean = false,
    val errorMessage: String? = null
)

class SchoolDisciplineViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ViolationRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = ViolationRepository(database.violationDao())
    }

    val allViolations: StateFlow<List<ViolationEntity>> = repository.allViolations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pendingApprovals: StateFlow<List<ViolationEntity>> = repository.pendingApprovals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filter states
    private val _dashboardGradeFilter = MutableStateFlow("Semua")
    val dashboardGradeFilter: StateFlow<String> = _dashboardGradeFilter.asStateFlow()

    private val _approvalPortalTab = MutableStateFlow("PENDING")
    val approvalPortalTab: StateFlow<String> = _approvalPortalTab.asStateFlow()

    private val _approvalSearchQuery = MutableStateFlow("")
    val approvalSearchQuery: StateFlow<String> = _approvalSearchQuery.asStateFlow()

    // Form state
    private val _formState = MutableStateFlow(initFormWithDefaults())
    val formState: StateFlow<FormState> = _formState.asStateFlow()

    private fun initFormWithDefaults(): FormState {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm 'WIB'", Locale("id", "ID"))
        val now = Date()
        return FormState(
            incidentDate = dateFormat.format(now),
            incidentTime = timeFormat.format(now)
        )
    }

    fun setDashboardGradeFilter(filter: String) {
        _dashboardGradeFilter.value = filter
    }

    fun setApprovalPortalTab(tab: String) {
        _approvalPortalTab.value = tab
    }

    fun setApprovalSearchQuery(query: String) {
        _approvalSearchQuery.value = query
    }

    fun updateFormField(update: (FormState) -> FormState) {
        _formState.value = update(_formState.value)
    }

    fun resetForm() {
        _formState.value = initFormWithDefaults()
    }

    fun clearSubmissionStatus() {
        _formState.value = _formState.value.copy(isSubmittedSuccessfully = false, errorMessage = null)
    }

    fun approveViolation(id: Long, principalNote: String) {
        viewModelScope.launch {
            val nowStr = SimpleDateFormat("dd MMM yyyy HH:mm 'WIB'", Locale("id", "ID")).format(Date())
            repository.updateApprovalStatus(
                id = id,
                status = "APPROVED",
                note = principalNote.ifBlank { "Disetujui oleh Kepala Sekolah sesuai rekomendasi sanksi yang diajukan." },
                decisionDate = nowStr
            )
        }
    }

    fun rejectViolation(id: Long, principalNote: String) {
        viewModelScope.launch {
            val nowStr = SimpleDateFormat("dd MMM yyyy HH:mm 'WIB'", Locale("id", "ID")).format(Date())
            repository.updateApprovalStatus(
                id = id,
                status = "REJECTED",
                note = principalNote.ifBlank { "Ditolak / Dikembalikan untuk pembinaan konseling ulang oleh Guru BK dan Wali Kelas." },
                decisionDate = nowStr
            )
        }
    }

    fun submitNewViolation(onSuccess: () -> Unit) {
        val state = _formState.value
        if (state.studentName.isBlank()) {
            _formState.value = state.copy(errorMessage = "Nama siswa wajib diisi")
            return
        }
        if (state.violationTitle.isBlank()) {
            _formState.value = state.copy(errorMessage = "Judul / jenis pelanggaran wajib diisi")
            return
        }

        viewModelScope.launch {
            val requiresApproval = state.requiresApproval || state.violationPoints >= 25 || state.category == "Pelanggaran Berat"
            val newEntity = ViolationEntity(
                studentName = state.studentName.trim(),
                studentNis = state.studentNis.ifBlank { "NIS-${(10000..99999).random()}" },
                studentClass = state.studentClass,
                category = state.category,
                violationTitle = state.violationTitle.trim(),
                violationPoints = state.violationPoints,
                location = state.location.ifBlank { "Lingkungan Sekolah" },
                reporterName = state.reporterName.ifBlank { "Guru Piket" },
                incidentDate = state.incidentDate.ifBlank { "Hari ini" },
                incidentTime = state.incidentTime.ifBlank { "08:00 WIB" },
                description = state.description.ifBlank { "Pencatatan pelanggaran tata tertib sekolah oleh tim ketertiban." },
                recommendedSanction = state.recommendedSanction.ifBlank {
                    when {
                        state.violationPoints >= 50 -> "Panggilan Orang Tua & Skorsing Khusus"
                        state.violationPoints >= 25 -> "Panggilan Orang Tua & SP-1"
                        else -> "Peringatan Lisan & Pembinaan Wali Kelas"
                    }
                },
                requiresApproval = requiresApproval,
                status = if (requiresApproval) "PENDING" else "APPROVED",
                principalNote = if (!requiresApproval) "Disetujui otomatis (Pelanggaran Ringan)" else null,
                principalDecisionDate = null,
                timestamp = System.currentTimeMillis()
            )

            repository.insertViolation(newEntity)
            _formState.value = _formState.value.copy(
                isSubmittedSuccessfully = true,
                errorMessage = null
            )
            onSuccess()
        }
    }
}
