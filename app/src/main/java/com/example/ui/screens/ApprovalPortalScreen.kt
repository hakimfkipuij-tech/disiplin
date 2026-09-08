package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ViolationEntity
import com.example.ui.theme.AuthorityAmber
import com.example.ui.theme.AuthorityAmberContainer
import com.example.ui.theme.PrincipalNavy
import com.example.ui.theme.PrincipalNavyContainer
import com.example.ui.theme.PrincipalNavyDark
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusDangerContainer
import com.example.ui.theme.StatusDangerText
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessContainer
import com.example.ui.theme.StatusSuccessText
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningContainer
import com.example.ui.theme.StatusWarningText
import com.example.ui.viewmodel.SchoolDisciplineViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApprovalPortalScreen(
    viewModel: SchoolDisciplineViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val violations by viewModel.allViolations.collectAsStateWithLifecycle()
    val pendingApprovals by viewModel.pendingApprovals.collectAsStateWithLifecycle()
    val currentTab by viewModel.approvalPortalTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.approvalSearchQuery.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedViolationForApproval by remember { mutableStateOf<ViolationEntity?>(null) }
    var selectedViolationForRejection by remember { mutableStateOf<ViolationEntity?>(null) }

    val pendingCount = pendingApprovals.size
    val approvedCount = violations.count { it.status == "APPROVED" }
    val rejectedCount = violations.count { it.status == "REJECTED" }

    val displayedViolations = violations.filter { violation ->
        val matchesTab = when (currentTab) {
            "PENDING" -> violation.status == "PENDING"
            "APPROVED" -> violation.status == "APPROVED"
            "REJECTED" -> violation.status == "REJECTED"
            else -> true
        }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            violation.studentName.contains(searchQuery, ignoreCase = true) ||
            violation.studentClass.contains(searchQuery, ignoreCase = true) ||
            violation.studentNis.contains(searchQuery, ignoreCase = true) ||
            violation.violationTitle.contains(searchQuery, ignoreCase = true)
        }
        matchesTab && matchesSearch
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Portal Persetujuan Kepala Sekolah",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Wewenang Pengesahan Tindakan & Sanksi Disiplin",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("approval_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Dashboard",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrincipalNavy
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 1100.dp)
                    .fillMaxSize()
            ) {
            // Header stats banner
            Surface(
                color = Color.White,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setApprovalSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("approval_search_input"),
                        placeholder = { Text("Cari nama siswa, NIS, kelas, atau judul pelanggaran...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setApprovalSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Hapus pencarian")
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tab filter chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = currentTab == "PENDING",
                                onClick = { viewModel.setApprovalPortalTab("PENDING") },
                                label = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Menunggu Acc")
                                        if (pendingCount > 0) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(CircleShape)
                                                    .background(if (currentTab == "PENDING") Color.White else AuthorityAmber),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$pendingCount",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (currentTab == "PENDING") PrincipalNavy else Color.White
                                                )
                                            }
                                        }
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AuthorityAmber,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("tab_pending")
                            )
                        }

                        item {
                            FilterChip(
                                selected = currentTab == "APPROVED",
                                onClick = { viewModel.setApprovalPortalTab("APPROVED") },
                                label = { Text("Disetujui ($approvedCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StatusSuccess,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("tab_approved")
                            )
                        }

                        item {
                            FilterChip(
                                selected = currentTab == "REJECTED",
                                onClick = { viewModel.setApprovalPortalTab("REJECTED") },
                                label = { Text("Ditolak ($rejectedCount)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StatusDanger,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("tab_rejected")
                            )
                        }

                        item {
                            FilterChip(
                                selected = currentTab == "ALL",
                                onClick = { viewModel.setApprovalPortalTab("ALL") },
                                label = { Text("Semua (${violations.size})") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrincipalNavy,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("tab_all")
                            )
                        }
                    }
                }
            }

            // Violation list
            if (displayedViolations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(PrincipalNavyContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = PrincipalNavy,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tidak Ada Kasus Dalam Kategori Ini",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrincipalNavyDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = when (currentTab) {
                                "PENDING" -> "Bagus! Semua kasus yang memerlukan persetujuan Kepala Sekolah telah selesai diproses."
                                "APPROVED" -> "Belum ada kasus yang berstatus telah disetujui."
                                "REJECTED" -> "Belum ada kasus yang ditolak / dikembalikan untuk pembinaan."
                                else -> "Tidak ditemukan pelanggaran yang cocok dengan filter pencarian."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(displayedViolations, key = { it.id }) { violation ->
                        ApprovalViolationDetailCard(
                            violation = violation,
                            onApproveClick = { selectedViolationForApproval = violation },
                            onRejectClick = { selectedViolationForRejection = violation }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
        }
    }

    // Modal Dialog: Approve Case
    selectedViolationForApproval?.let { violation ->
        ApproveCaseDialog(
            violation = violation,
            onDismiss = { selectedViolationForApproval = null },
            onConfirm = { officialNote ->
                viewModel.approveViolation(violation.id, officialNote)
                selectedViolationForApproval = null
                scope.launch {
                    snackbarHostState.showSnackbar("Sanksi pelanggaran untuk ${violation.studentName} berhasil disahkan!")
                }
            }
        )
    }

    // Modal Dialog: Reject Case
    selectedViolationForRejection?.let { violation ->
        RejectCaseDialog(
            violation = violation,
            onDismiss = { selectedViolationForRejection = null },
            onConfirm = { note ->
                viewModel.rejectViolation(violation.id, note)
                selectedViolationForRejection = null
                scope.launch {
                    snackbarHostState.showSnackbar("Kasus ${violation.studentName} dikembalikan ke Tim BK untuk pembinaan ulang.")
                }
            }
        )
    }
}

@Composable
private fun ApprovalViolationDetailCard(
    violation: ViolationEntity,
    onApproveClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    val isPending = violation.status == "PENDING"
    val isApproved = violation.status == "APPROVED"
    val isRejected = violation.status == "REJECTED"

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("approval_detail_card_${violation.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Student info & Points badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = violation.studentName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrincipalNavyDark
                        )
                    }
                    Text(
                        text = "${violation.studentClass}  •  NIS: ${violation.studentNis}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Severity / Points Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                violation.violationPoints >= 50 -> StatusDangerContainer
                                violation.violationPoints >= 25 -> AuthorityAmberContainer
                                else -> PrincipalNavyContainer
                            }
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${violation.violationPoints} Poin",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                violation.violationPoints >= 50 -> StatusDangerText
                                violation.violationPoints >= 25 -> Color(0xFF78350F)
                                else -> PrincipalNavyDark
                            }
                        )
                        Text(
                            text = when {
                                violation.violationPoints >= 50 -> "Sanksi Berat"
                                violation.violationPoints >= 25 -> "Sanksi Sedang"
                                else -> "Sanksi Ringan"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = when {
                                violation.violationPoints >= 50 -> StatusDangerText
                                violation.violationPoints >= 25 -> Color(0xFF78350F)
                                else -> PrincipalNavyDark
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Violation Title & Category Chip
            Text(
                text = violation.violationTitle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = violation.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = PrincipalNavyDark
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = violation.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Incident Description box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Kronologi Kejadian:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = violation.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Pelapor: ${violation.reporterName} • ${violation.incidentDate} ${violation.incidentTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Recommended Sanction Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFFBEB))
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = AuthorityAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rekomendasi Tindakan Sanksi (Tim Tatib/BK):",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = violation.recommendedSanction,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF78350F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section according to approval status
            if (isPending) {
                // Principal Action Decision Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRejectClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_reject_${violation.id}"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = StatusDanger
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tolak / Revisi",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onApproveClick,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp)
                            .testTag("btn_approve_${violation.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusSuccess
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Setujui Sanksi",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else if (isApproved) {
                // Approved Verification Box & Digital Stamp
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StatusSuccessContainer)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = StatusSuccessText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DISETUJUI OLEH KEPALA SEKOLAH",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccessText
                                )
                            }
                            violation.principalDecisionDate?.let { date ->
                                Text(
                                    text = date,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusSuccessText
                                )
                            }
                        }
                        violation.principalNote?.let { note ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Instruksi Kepsek: \"$note\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusSuccessText
                            )
                        }
                    }
                }
            } else if (isRejected) {
                // Rejected Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StatusDangerContainer)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = StatusDangerText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "DIKEMBALIKAN KE GURU BK / PEMBINAAN ULANG",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusDangerText
                                )
                            }
                            violation.principalDecisionDate?.let { date ->
                                Text(
                                    text = date,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusDangerText
                                )
                            }
                        }
                        violation.principalNote?.let { note ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Arahan Kepsek: \"$note\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = StatusDangerText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ApproveCaseDialog(
    violation: ViolationEntity,
    onDismiss: () -> Unit,
    onConfirm: (officialNote: String) -> Unit
) {
    var officialNote by remember {
        mutableStateOf("Disetujui. Segera tindak lanjuti pemanggilan orang tua dan laksanakan sanksi sesuai ketentuan sekolah.")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = StatusSuccess,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pengesahan Sanksi Kepala Sekolah", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "Anda akan mengesahkan sanksi kedisiplinan resmi untuk:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "${violation.studentName} (${violation.studentClass})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrincipalNavyDark
                        )
                        Text(
                            text = "Sanksi: ${violation.recommendedSanction}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF0F172A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Catatan & Instruksi Resmi Kepala Sekolah:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = officialNote,
                    onValueChange = { officialNote = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_approve_note_input"),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(officialNote) },
                colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                modifier = Modifier.testTag("dialog_confirm_approve_button")
            ) {
                Text("Sahkan & Terapkan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel_approve_button")
            ) {
                Text("Batal")
            }
        }
    )
}

@Composable
private fun RejectCaseDialog(
    violation: ViolationEntity,
    onDismiss: () -> Unit,
    onConfirm: (reason: String) -> Unit
) {
    var rejectionReason by remember {
        mutableStateOf("Mohon lakukan pendekatan pembinaan konseling persuasif terlebih dahulu dan koordinasikan dengan wali kelas sebelum sanksi diterapkan.")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = StatusDanger,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kembalikan / Pembinaan Ulang", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "Berikan arahan Kepala Sekolah kepada Guru BK / Tim Tatib untuk peninjauan ulang kasus ${violation.studentName}:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = rejectionReason,
                    onValueChange = { rejectionReason = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_reject_reason_input"),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(rejectionReason) },
                colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                modifier = Modifier.testTag("dialog_confirm_reject_button")
            ) {
                Text("Kirim Arahan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel_reject_button")
            ) {
                Text("Batal")
            }
        }
    )
}
