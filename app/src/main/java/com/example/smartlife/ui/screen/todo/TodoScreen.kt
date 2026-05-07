package com.example.smartlife.ui.screen.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartlife.data.local.entity.TodoEntity
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {

    var searchQuery by remember { mutableStateOf("") }

    val todoList by viewModel.todo.collectAsState(initial = emptyList())

    var selectedTab by remember { mutableStateOf("day") }

    val tabs = listOf("day", "week", "month", "year")

    var showDialog by remember { mutableStateOf(false) }

    var editTodo by remember { mutableStateOf<TodoEntity?>(null) }

    var deleteTodo by remember { mutableStateOf<TodoEntity?>(null) }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Focus")
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    editTodo = null
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text("Search Task")
                },

                singleLine = true,

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab)
            ) {

                tabs.forEach { tab ->

                    Tab(
                        selected = selectedTab == tab,

                        onClick = {
                            selectedTab = tab
                        },

                        text = {
                            Text(tab.uppercase())
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val filteredList = todoList.filter {

                it.type == selectedTab &&
                        it.title.contains(searchQuery, ignoreCase = true)
            }

            if (filteredList.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "No Tasks Found",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Add a task to stay productive"
                    )
                }
            }

            LazyColumn {

                items(filteredList) { todo ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),

                        shape = RoundedCornerShape(24.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = todo.title,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            text = todo.priority,
                                            color =
                                                when (todo.priority) {
                                                    "HIGH" -> Color.Red
                                                    "MEDIUM" -> Color(0xFFFF9800)
                                                    else -> Color(0xFF4CAF50)
                                                },
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (todo.description.isNotBlank()) {

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = todo.description,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = "Due Date",
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Text(
                                            text = todo.dueDate,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = "Due Time",
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Text(
                                            text = todo.dueTime,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Label,
                                            contentDescription = "Task Type",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )

                                        Spacer(modifier = Modifier.width(6.dp))

                                        Text(
                                            text = todo.type.uppercase(),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = " ${todo.type.uppercase()}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Checkbox(
                                    checked = todo.isDone,

                                    onCheckedChange = {

                                        viewModel.updateTodo(
                                            todo.copy(
                                                isDone = !todo.isDone
                                            )
                                        )
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row {

                                IconButton(
                                    onClick = {
                                        editTodo = todo
                                        showDialog = true
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        deleteTodo = todo
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showDialog) {

                TodoDialog(

                    todo = editTodo,

                    onDismiss = {
                        showDialog = false
                    },

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

                    onDelete = {

                        editTodo?.let {

                            viewModel.deleteTodo(it)
                        }

                        showDialog = false
                    }
                )
            }

            deleteTodo?.let { todoToDelete ->

                AlertDialog(

                    onDismissRequest = {
                        deleteTodo = null
                    },

                    title = {
                        Text("Delete Task?")
                    },

                    text = {
                        Text("Are you sure you want to delete this task?")
                    },

                    confirmButton = {

                        TextButton(
                            onClick = {

                                viewModel.deleteTodo(todoToDelete)

                                deleteTodo = null
                            }
                        ) {
                            Text("Yes")
                        }
                    },

                    dismissButton = {

                        TextButton(
                            onClick = {
                                deleteTodo = null
                            }
                        ) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}