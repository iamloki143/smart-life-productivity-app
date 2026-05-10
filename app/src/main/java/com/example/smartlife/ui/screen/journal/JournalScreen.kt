package com.example.smartlife.ui.screen.journal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartlife.viewmodel.journalviewmodel.JournalViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(viewModel: JournalViewModel) {

    var selectedDate by remember { mutableStateOf("") }
    var showEditor by remember { mutableStateOf(false) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val startDayOffset = firstDayOfMonth.dayOfWeek.value % 7

    val savedDates by viewModel.getAllEntryDates().collectAsState(emptyList())
    val today = LocalDate.now()

    val monthName = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val consistencyPct = if (daysInMonth > 0)
        savedDates.count {
            it.endsWith("-${currentMonth.monthValue}-${currentMonth.year}")
        } * 100 / daysInMonth
    else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Journal") },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(99.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = "$monthName ${currentMonth.year}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            // Month navigator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Previous month",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "$monthName ${currentMonth.year}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Next month",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Day-of-week headers
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Calendar grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(startDayOffset) {
                    Box(modifier = Modifier.aspectRatio(1f))
                }

                items(daysInMonth) { index ->
                    val day = index + 1
                    val isToday = day == today.dayOfMonth &&
                            currentMonth.monthValue == today.monthValue &&
                            currentMonth.year == today.year
                    val dateKey = "$day-${currentMonth.monthValue}-${currentMonth.year}"
                    val isSaved = savedDates.contains(dateKey)

                    val bgColor = when {
                        isToday -> MaterialTheme.colorScheme.primary
                        isSaved -> MaterialTheme.colorScheme.primaryContainer
                        else    -> Color.Transparent
                    }
                    val textColor = when {
                        isToday -> MaterialTheme.colorScheme.onPrimary
                        isSaved -> MaterialTheme.colorScheme.onPrimaryContainer
                        else    -> MaterialTheme.colorScheme.onSurface
                    }
                    val border = if (isSaved && !isToday)
                        BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                    else null

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(bgColor)
                            .then(
                                if (border != null)
                                    Modifier.border(border.width, border.brush, CircleShape)
                                else Modifier
                            )
                            .clickable {
                                selectedDate = dateKey
                                showEditor = true
                            }
                    ) {
                        Text(
                            text = day.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isToday || isSaved) FontWeight.Medium else FontWeight.Normal,
                            color = textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Month stats bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    JournalStatItem(
                        value = "$daysInMonth",
                        label = "Days"
                    )
                    JournalStatItem(
                        value = "${savedDates.count { it.endsWith("-${currentMonth.monthValue}-${currentMonth.year}") }}",
                        label = "Entries",
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                    JournalStatItem(
                        value = "$consistencyPct%",
                        label = "Consistency",
                        valueColor = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }

    if (showEditor && selectedDate.isNotEmpty()) {
        JournalEditor(
            date = selectedDate,
            viewModel = viewModel,
            onClose = { showEditor = false }
        )
    }
}

@Composable
private fun JournalStatItem(
    value: String,
    label: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}