package com.example.smartlife.ui.screen.profile

import StatsCard
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartlife.data.reposatery.DatabaseProvider
import com.example.smartlife.utils.StreakManager
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModel
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {

    val context = LocalContext.current

    val db = DatabaseProvider.getDB(context)
    val dao = db.todoDao()

    val todoViewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(dao)
    )

    val todoList by todoViewModel.todo.collectAsState()

    val streakManager = remember { StreakManager(context) }

    val totalTasks = todoList.size
    val completedTasks = todoList.count { it.isDone }
    val pendingTasks = totalTasks - completedTasks

    val progress =
        if (totalTasks > 0)
            completedTasks.toFloat() / totalTasks
        else 0f

    val streak = streakManager.getCurrentStreak()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(180.dp)
                    ) {

                        Canvas(modifier = Modifier.fillMaxSize()) {

                            drawArc(
                                color = Color.LightGray.copy(alpha = 0.3f),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(
                                    width = 22f,
                                    cap = StrokeCap.Round
                                )
                            )

                            drawArc(
                                color = Color(0xFF4CAF50),
                                startAngle = -90f,
                                sweepAngle = 360 * progress,
                                useCenter = false,
                                style = Stroke(
                                    width = 22f,
                                    cap = StrokeCap.Round
                                )
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Completed",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Your productivity is growing 🚀",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    StatsCard(
                        title = "Tasks",
                        value = "$totalTasks",
                        color = Color(0xFF2196F3)
                    )
                }

                item {
                    StatsCard(
                        title = "Completed",
                        value = "$completedTasks",
                        color = Color(0xFF4CAF50)
                    )
                }

                item {
                    StatsCard(
                        title = "Pending",
                        value = "$pendingTasks",
                        color = Color(0xFFF44336)
                    )
                }

                item {
                    StatsCard(
                        title = "Streak",
                        value = "🔥 $streak",
                        color = Color(0xFFFF9800)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Weekly Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = "Performance",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Your productivity flow this week",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Card(
                            shape = RoundedCornerShape(50),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.15f)
                            )
                        ) {

                            Text(
                                text = "↑ 18%",
                                modifier = Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 8.dp
                                ),
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    val weeklyData = listOf(2, 5, 3, 8, 4, 10, 7)

                    val weekDays = listOf(
                        "Mon",
                        "Tue",
                        "Wed",
                        "Thu",
                        "Fri",
                        "Sat",
                        "Sun"
                    )

                    val maxValue = weeklyData.max()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {

                        weeklyData.forEachIndexed { index, value ->

                            val isHighest = value == maxValue

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "$value",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHighest)
                                        Color(0xFF4CAF50)
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .width(42.dp)
                                        .height((40 + value * 16).dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(
                                            if (isHighest)
                                                Color(0xFF4CAF50)
                                            else
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                                        )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = weekDays[index],
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isHighest)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {

                                Text(
                                    text = "Best Day",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Saturday",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {

                                Text(
                                    text = "10 Tasks",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Peak productivity 🚀",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
