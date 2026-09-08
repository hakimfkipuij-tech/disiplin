package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.ui.viewmodel.SchoolDisciplineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViolationInputScreen(
    viewModel: SchoolDisciplineViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToApproval: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    var showSuccessDialog by remember { mutableStateOf(false) }

    val classOptions = listOf("X MIPA 1", "X MIPA 2", "X IPS 1", "XI MIPA 1", "XI MIPA 2", "XI IPS 1", "XII MIPA 1", "XII MIPA 2", "XII IPS 1")
    val categoryOptions = listOf(
        "Kedisiplinan & Keterlambatan",
        "Atribut & Seragam",
        "Kehadiran (Bolos)",
        "Etika & Perilaku",
        "Pelanggaran Berat"
    )
    val violationPresets = mapOf(
        "Kedisiplinan & Keterlambatan" to listOf("Terlambat Datang ke Sekolah (>15 Menit)", "Meninggalkan Upacara Bendera", "Tidak Mengikuti Apel Pagi"),
        "Atribut & Seragam" to listOf("Seragam Tidak Lengkap / Atribut Tidak Sesuai", "Mengenakan Sepatu Non-Hitam Tanpa Izin", "Rambut Tidak Rapi / Diwarnai"),
        "Kehadiran (Bolos)" to listOf("Meninggalkan KBM Tanpa Surat Izin (Bolos)", "Melompati Pagar Sekolah", "Nongkrong di Warung Saat Jam Sekolah"),
        "Etika & Perilaku" to listOf("Membawa / Menghisap Vape atau Rokok di Sekolah", "Membawa HP / Gadget saat Ujian Tanpa Izin", "Bersikap Tidak Sopan Kepada Tenaga Pendidik"),
        "Pelanggaran Berat" to listOf("Terlibat Perkelahian / Bentrok Fisik", "Membawa Senjata Tajam / Benda Berbahaya", "Melakukan Perundungan (Bullying) Fisik / Verbal")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Form Input Pelanggaran",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Pencatatan Kejadian & Rekomendasi Sanksi Siswa",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("input_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Dashboard",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.resetForm() },
                        modifier = Modifier.testTag("reset_form_menu_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Form",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner if any
            formState.errorMessage?.let { errorMsg ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StatusDangerContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = StatusDangerText
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMsg,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = StatusDangerText
                            )
                        }
                    }
                }
            }

            // Section 1: Student Information
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(PrincipalNavyContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PrincipalNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "1. Identitas Siswa",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrincipalNavyDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Student Name
                        OutlinedTextField(
                            value = formState.studentName,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(studentName = it, errorMessage = null) } },
                            label = { Text("Nama Lengkap Siswa *") },
                            placeholder = { Text("Contoh: Muhammad Ihsan") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_student_name"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Student NIS
                        OutlinedTextField(
                            value = formState.studentNis,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(studentNis = it) } },
                            label = { Text("Nomor Induk Siswa (NIS/NISN)") },
                            placeholder = { Text("Contoh: 202410312") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_student_nis"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Pilih Kelas:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(classOptions) { className ->
                                FilterChip(
                                    selected = formState.studentClass == className,
                                    onClick = { viewModel.updateFormField { state -> state.copy(studentClass = className) } },
                                    label = { Text(className) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrincipalNavy,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("chip_class_$className")
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Incident & Violation Details
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(AuthorityAmberContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddAlert,
                                    contentDescription = null,
                                    tint = AuthorityAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "2. Data & Kategori Pelanggaran",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrincipalNavyDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Kategori:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categoryOptions) { cat ->
                                FilterChip(
                                    selected = formState.category == cat,
                                    onClick = {
                                        val defaultPoints = when (cat) {
                                            "Pelanggaran Berat" -> 75
                                            "Etika & Perilaku" -> 35
                                            "Kehadiran (Bolos)" -> 25
                                            "Atribut & Seragam" -> 10
                                            else -> 15
                                        }
                                        viewModel.updateFormField { state ->
                                            state.copy(
                                                category = cat,
                                                violationPoints = defaultPoints,
                                                requiresApproval = defaultPoints >= 25 || cat == "Pelanggaran Berat"
                                            )
                                        }
                                    },
                                    label = { Text(cat) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = if (cat == "Pelanggaran Berat") StatusDanger else AuthorityAmber,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("chip_category_$cat")
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick presets for violation title
                        violationPresets[formState.category]?.let { presets ->
                            Text(
                                text = "Template Judul Cepat:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(presets) { preset ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Color(0xFFF1F5F9))
                                            .clickable {
                                                viewModel.updateFormField { state -> state.copy(violationTitle = preset) }
                                            }
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                    ) {
                                        Text(
                                            text = preset,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PrincipalNavyDark
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Violation Title
                        OutlinedTextField(
                            value = formState.violationTitle,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(violationTitle = it, errorMessage = null) } },
                            label = { Text("Judul / Jenis Pelanggaran *") },
                            placeholder = { Text("Contoh: Membawa HP saat ujian tanpa izin") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_violation_title"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Violation Points Slider & Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Bobot Poin Pelanggaran:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            formState.violationPoints >= 50 -> StatusDangerContainer
                                            formState.violationPoints >= 25 -> AuthorityAmberContainer
                                            else -> PrincipalNavyContainer
                                        }
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${formState.violationPoints} Poin",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        formState.violationPoints >= 50 -> StatusDangerText
                                        formState.violationPoints >= 25 -> Color(0xFF78350F)
                                        else -> PrincipalNavyDark
                                    }
                                )
                            }
                        }

                        Slider(
                            value = formState.violationPoints.toFloat(),
                            onValueChange = { newPts ->
                                val pts = newPts.toInt()
                                val autoEscalate = pts >= 25 || formState.category == "Pelanggaran Berat"
                                val recommendedSanction = when {
                                    pts >= 75 -> "Skorsing 5 Hari Kerja & Panggilan Orang Tua Tahap 3"
                                    pts >= 50 -> "Panggilan Orang Tua, SP-2, & Konseling Khusus BK"
                                    pts >= 25 -> "Panggilan Orang Tua & SP-1"
                                    pts >= 15 -> "Peringatan Tertulis & Tugas Edukasi"
                                    else -> "Teguran Lisan & Pembinaan Wali Kelas"
                                }
                                viewModel.updateFormField { state ->
                                    state.copy(
                                        violationPoints = pts,
                                        requiresApproval = autoEscalate,
                                        recommendedSanction = recommendedSanction
                                    )
                                }
                            },
                            valueRange = 5f..100f,
                            steps = 18,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("slider_violation_points"),
                            colors = SliderDefaults.colors(
                                thumbColor = when {
                                    formState.violationPoints >= 50 -> StatusDanger
                                    formState.violationPoints >= 25 -> AuthorityAmber
                                    else -> PrincipalNavy
                                },
                                activeTrackColor = when {
                                    formState.violationPoints >= 50 -> StatusDanger
                                    formState.violationPoints >= 25 -> AuthorityAmber
                                    else -> PrincipalNavy
                                }
                            )
                        )

                        // Preset point chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(5, 10, 20, 25, 40, 50, 75).forEach { pts ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (formState.violationPoints == pts) PrincipalNavy else Color(0xFFF1F5F9))
                                        .clickable {
                                            val autoEscalate = pts >= 25 || formState.category == "Pelanggaran Berat"
                                            val recommendedSanction = when {
                                                pts >= 75 -> "Skorsing 5 Hari Kerja & Panggilan Orang Tua Tahap 3"
                                                pts >= 50 -> "Panggilan Orang Tua, SP-2, & Konseling Khusus BK"
                                                pts >= 25 -> "Panggilan Orang Tua & SP-1"
                                                pts >= 15 -> "Peringatan Tertulis & Tugas Edukasi"
                                                else -> "Teguran Lisan & Pembinaan Wali Kelas"
                                            }
                                            viewModel.updateFormField { state ->
                                                state.copy(
                                                    violationPoints = pts,
                                                    requiresApproval = autoEscalate,
                                                    recommendedSanction = recommendedSanction
                                                )
                                            }
                                        }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$pts",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (formState.violationPoints == pts) Color.White else PrincipalNavyDark
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Location
                        OutlinedTextField(
                            value = formState.location,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(location = it) } },
                            label = { Text("Lokasi Kejadian") },
                            placeholder = { Text("Contoh: Toilet Siswa Lantai 2 / Gerbang Utama") },
                            leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_location"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Incident Time & Reporter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = formState.incidentDate,
                                onValueChange = { viewModel.updateFormField { state -> state.copy(incidentDate = it) } },
                                label = { Text("Tanggal") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_date"),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                            OutlinedTextField(
                                value = formState.incidentTime,
                                onValueChange = { viewModel.updateFormField { state -> state.copy(incidentTime = it) } },
                                label = { Text("Waktu") },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_time"),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reporter Name
                        OutlinedTextField(
                            value = formState.reporterName,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(reporterName = it) } },
                            label = { Text("Petugas / Guru Pelapor") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_reporter"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Description / Chronology
                        OutlinedTextField(
                            value = formState.description,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(description = it) } },
                            label = { Text("Kronologi & Keterangan Kejadian") },
                            placeholder = { Text("Uraikan kronologi kejadian secara objektif dan saksi yang mengetahui...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_description"),
                            minLines = 3,
                            maxLines = 5,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            // Section 3: Recommended Sanction & Escalation to Principal
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    tint = AuthorityAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "3. Rekomendasi Sanksi & Wewenang",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrincipalNavyDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = formState.recommendedSanction,
                            onValueChange = { viewModel.updateFormField { state -> state.copy(recommendedSanction = it) } },
                            label = { Text("Usulan Tindakan Sanksi Kedisiplinan") },
                            placeholder = { Text("Contoh: Pemanggilan Orang Tua & Skorsing 3 Hari") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_recommended_sanction"),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Switch: Requires Principal Approval
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (formState.requiresApproval) AuthorityAmberContainer else Color(0xFFF1F5F9)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Eskalasi Persetujuan Kepala Sekolah",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (formState.requiresApproval) Color(0xFF78350F) else PrincipalNavyDark
                                    )
                                    Text(
                                        text = if (formState.requiresApproval) {
                                            "Kasus akan masuk ke Portal Persetujuan Kepala Sekolah untuk disahkan resmi."
                                        } else {
                                            "Pelanggaran ringan diselesaikan langsung di tingkat Wali Kelas/Tatib."
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (formState.requiresApproval) Color(0xFF92400E) else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = formState.requiresApproval,
                                    onCheckedChange = { checked ->
                                        viewModel.updateFormField { state -> state.copy(requiresApproval = checked) }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = AuthorityAmber
                                    ),
                                    modifier = Modifier.testTag("switch_escalate_approval")
                                )
                            }
                        }
                    }
                }
            }

            // Section 4: Action Buttons
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            viewModel.submitNewViolation(
                                onSuccess = {
                                    showSuccessDialog = true
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_violation_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrincipalNavy
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (formState.requiresApproval) "Simpan & Ajukan ke Kepala Sekolah" else "Simpan Catatan Pelanggaran",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.resetForm() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("reset_form_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kosongkan Form")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Success Dialog on saved
    if (showSuccessDialog) {
        val wasEscalated = formState.requiresApproval
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                viewModel.clearSubmissionStatus()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = StatusSuccess,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Pelanggaran Berhasil Dicatat",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Data pelanggaran siswa atas nama ${formState.studentName} (${formState.studentClass}) telah berhasil tersimpan ke sistem.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (wasEscalated) AuthorityAmberContainer else StatusSuccessContainer)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = if (wasEscalated) {
                                "⚠️ Kasus ini memerlukan pengesahan dan telah dikirimkan ke Portal Persetujuan Kepala Sekolah."
                            } else {
                                "✅ Kasus tercatat sebagai pelanggaran ringan yang telah diselesaikan tim ketertiban."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (wasEscalated) Color(0xFF78350F) else StatusSuccessText
                        )
                    }
                }
            },
            confirmButton = {
                if (wasEscalated) {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.clearSubmissionStatus()
                            viewModel.resetForm()
                            onNavigateToApproval()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AuthorityAmber),
                        modifier = Modifier.testTag("dialog_goto_approval_button")
                    ) {
                        Text("Buka Portal Persetujuan")
                    }
                } else {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.clearSubmissionStatus()
                            viewModel.resetForm()
                            onNavigateToDashboard()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrincipalNavy),
                        modifier = Modifier.testTag("dialog_goto_dashboard_button")
                    ) {
                        Text("Kembali ke Dashboard")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.clearSubmissionStatus()
                        viewModel.resetForm()
                    },
                    modifier = Modifier.testTag("dialog_input_again_button")
                ) {
                    Text("Input Kasus Baru")
                }
            }
        )
    }
}
