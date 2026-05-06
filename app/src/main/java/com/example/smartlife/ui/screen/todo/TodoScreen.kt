package com.example.smartlife.ui.screen.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import com.example.smartlife.data.local.entity.TodoEntity
import com.example.smartlife.viewmodel.todoViewModel.TodoViewModel
import kotlin.collections.listOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val todoList by viewModel.todo.collectAsState(initial = emptyList())
    var selectedTab by remember { mutableStateOf("day") }
    val tabs=listOf("day","week","month","year")
    var showDialog by remember { mutableStateOf(false) }
    var editTodo by remember { mutableStateOf<TodoEntity?>(null) }
    var deleteTodo by remember { mutableStateOf<TodoEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Focus")}
            )
        },
        floatingActionButton ={
            FloatingActionButton(onClick = {
                editTodo = null
                showDialog=true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {padding ->
        Column (
            modifier = Modifier.fillMaxSize().padding(padding),
        ){
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
                                    isDone = newTodo.isDone
                                )
                            )
                        } else {
                            viewModel.updateTodo(
                                TodoEntity(
                                    id = newTodo.id,
                                    title = newTodo.title,
                                    description = newTodo.description,
                                    type = newTodo.type,
                                    isDone = newTodo.isDone
                                )
                            )
                            }

                        showDialog = false
                    },
                    onDelete = {
                        editTodo?.let {
                            viewModel.deleteTodo(
                                TodoEntity(
                                    id = it.id,
                                    title = it.title,
                                    description = it.description,
                                    type = it.type,
                                    isDone = it.isDone
                                )
                            )
                        }
                        showDialog = false
                    }
                )
            }
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {searchQuery=it},
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                placeholder = { Text("Search Task")},
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TabRow(selectedTabIndex = tabs.indexOf(selectedTab)) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selectedTab== tab,
                        onClick = {selectedTab=tab},
                        text = {Text(tab.uppercase())}
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            val filteredList = todoList.filter {
                it.type==selectedTab && it.title.contains(searchQuery,true)
            }
            LazyColumn {
                items(filteredList){todo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical=6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = todo.isDone,
                                onCheckedChange = {
                                    viewModel.updateTodo(
                                        TodoEntity(
                                            id = todo.id,
                                            title = todo.title,
                                            description = todo.description,
                                            type = todo.type,
                                            isDone = ! todo.isDone
                                        )
                                    )
                                }
                            )
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = todo.title,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = todo.description
                                )
                            }
                            IconButton(
                                onClick = {
                                    editTodo=todo
                                    showDialog = true
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "edit")
                            }
                            IconButton(
                                onClick = {
                                    deleteTodo= todo
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "delete")
                            }
                            deleteTodo?.let{ todoToDelete ->
                                AlertDialog(
                                    onDismissRequest = { deleteTodo=null},
                                    title = {Text("Delete Task? ")},
                                    confirmButton = {
                                        TextButton(onClick = {
                                            viewModel.deleteTodo(todoToDelete)
                                            deleteTodo=null
                                        }) {
                                            Text("Yes")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = {
                                            deleteTodo=null
                                        }) {
                                            Text("No")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}