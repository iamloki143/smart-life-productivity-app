package com.example.smartlife.ui.screen.todo

import StatItem
import TodoCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.smartlife.data.local.entity.TodoEntity
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModel
import com.example.smartlife.utils.StreakManager
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val context = LocalContext.current
    val streakManager = remember { StreakManager(context) }

    var searchQuery by remember { mutableStateOf("") }
    val todoList by viewModel.todo.collectAsState(initial = emptyList())
    var selectedTab by remember { mutableStateOf("day") }
    val tabs = listOf("day", "week", "month", "year")
    var showDialog by remember { mutableStateOf(false) }
    var editTodo by remember { mutableStateOf<TodoEntity?>(null) }
    var deleteTodo by remember { mutableStateOf<TodoEntity?>(null) }

    // Today's display date
    val todayDisplay = remember {
        SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(Date())
    }

    // Auto-remove expired tasks: delete tasks whose dueDate+dueTime has passed
    LaunchedEffect(todoList) {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val now = Date()
        todoList.forEach { todo ->
            if (todo.dueDate.isNotBlank() && todo.dueTime.isNotBlank()) {
                runCatching {
                    sdf.parse("${todo.dueDate} ${todo.dueTime}")
                }.getOrNull()?.let { due ->
                    if (due.before(now) && !todo.isDone) {
                        viewModel.deleteTodo(todo)
                    }
                }
            }
        }
    }

    val filteredList = todoList.filter {
        it.type == selectedTab && it.title.contains(searchQuery, ignoreCase = true)
    }

    val totalTasks = filteredList.size
    val completedTasks = filteredList.count { it.isDone }
    val pendingTasks = totalTasks - completedTasks
    val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks.toFloat() else 0f

    // Weekly completion for the stat card
    val weeklyTasks = todoList.filter { it.type == "week" }
    val weeklyProgress = if (weeklyTasks.isNotEmpty())
        (weeklyTasks.count { it.isDone } * 100 / weeklyTasks.size) else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus") },
                actions = {
                    Text(
                        text = todayDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { editTodo = null; showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search tasks…") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs
            TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Progress overview",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(label = "Total", value = "$totalTasks", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        StatItem(label = "Done", value = "$completedTasks", color = Color(0xFF2E7D32))
                        StatItem(label = "Pending", value = "$pendingTasks", color = Color(0xFFB71C1C))
                        StatItem(label = "Progress", value = "${(progress * 100).toInt()}%", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.15f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stat mini-cards row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Daily streak",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF7B4E1A)
                            )
                            Text(
                                "Keep it up!",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA0611E)
                            )
                        }
                        Text(
                            "🔥 ${streakManager.getCurrentStreak()}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B4E1A)
                        )
                    }
                }
                // Weekly completion
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "This week",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                "Completion",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF388E3C)
                            )
                        }
                        Text(
                            "$weeklyProgress%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Empty state
            if (filteredList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No tasks here", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Tap + to add a task and stay productive",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Task list
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredList) { todo ->
                    TodoCard(
                        todo = todo,
                        onCheckedChange = {
                            val updated = todo.copy(isDone = !todo.isDone)
                            viewModel.updateTodo(updated)
                            if (updated.isDone) streakManager.updateStreak()
                        },
                        onEdit = { editTodo = todo; showDialog = true },
                        onDelete = { deleteTodo = todo }
                    )
                }
                item { Spacer(modifier = Modifier.height(80.dp)) } // FAB clearance
            }
        }

        // Add / Edit dialog
        if (showDialog) {
            TodoDialog(
                todo = editTodo,
                onDismiss = { showDialog = false },
                onSave = { newTodo ->
                    if (editTodo == null) {
                        viewModel.addTodo(
                            TodoEntity(
                                title = newTodo.title,
                                description = newTodo.description,
                                type = newTodo.type,
                                isDone = newTodo.isDone,
                                dueDate = newTodo.dueDate,
                                dueTime = newTodo.dueTime,
                                priority = newTodo.priority
                            )
                        )
                    } else {
                        viewModel.updateTodo(
                            TodoEntity(
                                id = newTodo.id,
                                title = newTodo.title,
                                description = newTodo.description,
                                type = newTodo.type,
                                isDone = newTodo.isDone,
                                dueDate = newTodo.dueDate,
                                dueTime = newTodo.dueTime,
                                priority = newTodo.priority
                            )
                        )
                    }
                    showDialog = false
                },
                onDelete = { editTodo?.let { viewModel.deleteTodo(it) }; showDialog = false }
            )
        }

        // Delete confirm
        deleteTodo?.let { todoToDelete ->
            AlertDialog(
                onDismissRequest = { deleteTodo = null },
                title = { Text("Delete task?") },
                text = { Text("\"${todoToDelete.title}\" will be permanently removed.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.deleteTodo(todoToDelete); deleteTodo = null }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteTodo = null }) { Text("Cancel") }
                }
            )
        }
    }
}