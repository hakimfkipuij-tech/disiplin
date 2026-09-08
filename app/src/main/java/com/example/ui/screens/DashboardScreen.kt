package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.ViolationEntity
import com.example.ui.theme.AuthorityAmber
import com.example.ui.theme.AuthorityAmberContainer
import com.example.ui.theme.PrincipalNavy
import com.example.ui.theme.PrincipalNavyDark
import com.example.ui.theme.PrincipalNavyLight
import com.example.ui.theme.PrincipalNavyContainer
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: SchoolDisciplineViewModel,
    onNavigateToApproval: () -> Unit,
    onNavigateToInput: () -> Unit,
    modifier: Modifier = Modifier
) {
    val violations by viewModel.allViolations.collectAsStateWithLifecycle()
    val pendingApprovals by viewModel.pendingApprovals.collectAsStateWithLifecycle()
    val selectedGradeFilter by viewModel.dashboardGradeFilter.collectAsStateWithLifecycle()

    val pendingCount = pendingApprovals.size
    val totalCount = violations.size
    val approvedCount = violations.count { it.status == "APPROVED" }
    val criticalCount = violations.count { it.violationPoints >= 40 || it.category == "Pelanggaran Berat" }
    val resolutionRate = if (totalCount > 0) (approvedCount * 100 / totalCount) else 100

    val filteredViolations = violations.filter { violation ->
        when (selectedGradeFilter) {
            "Semua" -> true
            "Perlu Persetujuan" -> violation.status == "PENDING"
            "Kelas X" -> violation.studentClass.startsWith("X ")
            "Kelas XI" -> violation.studentClass.startsWith("XI ")
            "Kelas XII" -> violation.studentClass.startsWith("XII ")
            else -> true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SMA Negeri 1 Teladan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Portal Pengawasan Disiplin Siswa",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrincipalNavy
                ),
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .clickable { onNavigateToApproval() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("top_bar_pending_badge_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Menunggu Persetujuan",
                                tint = if (pendingCount > 0) AuthorityAmber else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            if (pendingCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$pendingCount Kasus",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
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
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 1100.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            // Section 1: Principal Welcome & Identity Card
            item {
                PrincipalHeaderCard()
            }

            // Section 2: Critical Urgent Notification for Pending Approvals
            if (pendingCount > 0) {
                item {
                    PendingApprovalUrgentBanner(
                        pendingCount = pendingCount,
                        onReviewClicked = onNavigateToApproval
                    )
                }
            }

            // Section 3: Metric KPI Grid
            item {
                MetricCardsGrid(
                    totalCount = totalCount,
                    pendingCount = pendingCount,
                    criticalCount = criticalCount,
                    resolutionRate = resolutionRate,
                    onPendingClick = onNavigateToApproval
                )
            }

            // Section 4: Quick Navigation Action Buttons
            item {
                QuickActionSection(
                    pendingCount = pendingCount,
                    onNavigateToApproval = onNavigateToApproval,
                    onNavigateToInput = onNavigateToInput
                )
            }

            // Section 5: Violation Categories Distribution
            item {
                CategoryDistributionCard(violations = violations)
            }

            // Section 6: Filter chips for violation list
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daftar Kasus Pelanggaran",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${filteredViolations.size} Kasus",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("Semua", "Perlu Persetujuan", "Kelas X", "Kelas XI", "Kelas XII")
                        items(filters) { filter ->
                            FilterChip(
                                selected = selectedGradeFilter == filter,
                                onClick = { viewModel.setDashboardGradeFilter(filter) },
                                label = { Text(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrincipalNavy,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("filter_chip_$filter")
                            )
                        }
                    }
                }
            }

            // Section 7: List of Violations
            if (filteredViolations.isEmpty()) {
                item {
                    EmptyViolationCard(filter = selectedGradeFilter)
                }
            } else {
                items(filteredViolations, key = { it.id }) { violation ->
                    ViolationSummaryCard(
                        violation = violation,
                        onCardClick = {
                            if (violation.status == "PENDING") {
                                onNavigateToApproval()
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        }
    }
}

@Composable
private fun PrincipalHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrincipalNavyDark,
                            PrincipalNavy
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(AuthorityAmber)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Drs. H. Mulyadi, M.Pd",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Kepala Sekolah • Pembina Tk. I",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuthorityAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tahun Ajaran 2024/2025 • Semester Genap",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PendingApprovalUrgentBanner(
    pendingCount: Int,
    onReviewClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("urgent_approval_banner"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = StatusWarningContainer
        ),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(StatusWarning, AuthorityAmber)))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(StatusWarning),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Perlu Keputusan Kepala Sekolah",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = StatusWarningText
                    )
                    Text(
                        text = "$pendingCount kasus pelanggaran berat/sedang menunggu persetujuan sanksi resmi Anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusWarningText
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onReviewClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("banner_review_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StatusWarning
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Buka Portal Persetujuan ($pendingCount)",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun MetricCardsGrid(
    totalCount: Int,
    pendingCount: Int,
    criticalCount: Int,
    resolutionRate: Int,
    onPendingClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600

    if (isWideScreen) {
        // Desktop / Tablet layout: All 4 cards in a single responsive row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                title = "Total Kasus",
                value = "$totalCount",
                subtitle = "Bulan Ini",
                icon = Icons.AutoMirrored.Filled.Assignment,
                iconTint = PrincipalNavy,
                containerColor = Color.White,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "Menunggu Acc",
                value = "$pendingCount",
                subtitle = if (pendingCount > 0) "Perlu Tindakan!" else "Semua Tuntas",
                icon = Icons.Default.Gavel,
                iconTint = AuthorityAmber,
                containerColor = if (pendingCount > 0) AuthorityAmberContainer else Color.White,
                valueColor = if (pendingCount > 0) Color(0xFF78350F) else PrincipalNavy,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onPendingClick() }
                    .testTag("metric_pending_approval_card")
            )

            MetricCard(
                title = "Kasus Berat",
                value = "$criticalCount",
                subtitle = "Poin >= 40",
                icon = Icons.Default.Warning,
                iconTint = StatusDanger,
                containerColor = Color.White,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "Tingkat Disiplin",
                value = "$resolutionRate%",
                subtitle = "Terselesaikan",
                icon = Icons.Default.CheckCircle,
                iconTint = StatusSuccess,
                containerColor = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        // Mobile layout: 2x2 grid
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total Kasus",
                    value = "$totalCount",
                    subtitle = "Bulan Ini",
                    icon = Icons.AutoMirrored.Filled.Assignment,
                    iconTint = PrincipalNavy,
                    containerColor = Color.White,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Menunggu Acc",
                    value = "$pendingCount",
                    subtitle = if (pendingCount > 0) "Perlu Tindakan!" else "Semua Tuntas",
                    icon = Icons.Default.Gavel,
                    iconTint = AuthorityAmber,
                    containerColor = if (pendingCount > 0) AuthorityAmberContainer else Color.White,
                    valueColor = if (pendingCount > 0) Color(0xFF78350F) else PrincipalNavy,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onPendingClick() }
                        .testTag("metric_pending_approval_card")
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Kasus Berat",
                    value = "$criticalCount",
                    subtitle = "Poin >= 40",
                    icon = Icons.Default.Warning,
                    iconTint = StatusDanger,
                    containerColor = Color.White,
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Tingkat Disiplin",
                    value = "$resolutionRate%",
                    subtitle = "Terselesaikan",
                    icon = Icons.Default.CheckCircle,
                    iconTint = StatusSuccess,
                    containerColor = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
    valueColor: Color = PrincipalNavyDark
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(iconTint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun QuickActionSection(
    pendingCount: Int,
    onNavigateToApproval: () -> Unit,
    onNavigateToInput: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Akses Menu Utama",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Button 1: Portal Persetujuan
                Button(
                    onClick = onNavigateToApproval,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("quick_nav_to_approval_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrincipalNavy
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Portal Approval",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (pendingCount > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(AuthorityAmber),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$pendingCount",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                // Button 2: Form Input Pelanggaran
                OutlinedButton(
                    onClick = onNavigateToInput,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("quick_nav_to_input_button"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrincipalNavy
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Input Kasus Baru",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryDistributionCard(violations: List<ViolationEntity>) {
    val total = violations.size.coerceAtLeast(1)
    val categories = listOf(
        "Kedisiplinan & Keterlambatan",
        "Atribut & Seragam",
        "Kehadiran (Bolos)",
        "Etika & Perilaku",
        "Pelanggaran Berat"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Sebaran Kategori Pelanggaran",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            categories.forEach { category ->
                val count = violations.count { it.category == category }
                val progress = count.toFloat() / total
                val color = when (category) {
                    "Pelanggaran Berat" -> StatusDanger
                    "Etika & Perilaku" -> AuthorityAmber
                    "Kehadiran (Bolos)" -> StatusWarning
                    "Kedisiplinan & Keterlambatan" -> PrincipalNavyLight
                    else -> Color(0xFF64748B)
                }

                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$count kasus",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = color
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = color,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }
        }
    }
}

@Composable
private fun ViolationSummaryCard(
    violation: ViolationEntity,
    onCardClick: () -> Unit
) {
    val isPending = violation.status == "PENDING"
    val isApproved = violation.status == "APPROVED"
    val isRejected = violation.status == "REJECTED"

    val statusContainer = when {
        isPending -> StatusWarningContainer
        isApproved -> StatusSuccessContainer
        else -> StatusDangerContainer
    }

    val statusText = when {
        isPending -> StatusWarningText
        isApproved -> StatusSuccessText
        else -> StatusDangerText
    }

    val statusLabel = when {
        isPending -> "Menunggu Persetujuan Kepsek"
        isApproved -> "Disetujui Kepsek"
        else -> "Ditolak / Pembinaan Ulang"
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("violation_card_${violation.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Student Name & Class
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = violation.studentName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrincipalNavyDark
                    )
                    Text(
                        text = "${violation.studentClass} • NIS: ${violation.studentNis}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Points Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (violation.violationPoints >= 40) StatusDangerContainer else PrincipalNavyContainer
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${violation.violationPoints} Poin",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (violation.violationPoints >= 40) StatusDangerText else PrincipalNavyDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = violation.violationTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Rekomendasi Sanksi: ${violation.recommendedSanction}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusContainer)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = statusText
                    )
                }

                Text(
                    text = "${violation.incidentDate} • ${violation.incidentTime}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyViolationCard(filter: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = StatusSuccess,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tidak Ada Pelanggaran",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = PrincipalNavyDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tidak ada catatan pelanggaran untuk filter '$filter'.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
