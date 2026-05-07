package com.example.smartlife.ui.screen.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartlife.data.local.entity.TodoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDialog(
    todo: TodoEntity?,
    onDismiss:() -> Unit,
    onSave:(TodoEntity) -> Unit,
    onDelete:() -> Unit
) {
    var title by remember { mutableStateOf(todo?.title ?: "") }
    var desc by remember {mutableStateOf(todo?.description ?: "")}
    var type by remember { mutableStateOf(todo?.type ?: "day") }
    var errorText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(todo?.dueDate ?: "") }
    var dueTime by remember { mutableStateOf(todo?.dueTime ?: "") }

    val types=listOf("day","week","month","year")

    AlertDialog(
        onDismissRequest = onDismiss,
        title={
            Text(if (todo==null)"ADD TASK" else "EDIT TASK")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {title=it},
                    label = {Text("Title")}
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = {desc=it},
                    label = {Text("Description")}
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    placeholder = { Text("07/05/2026") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dueTime,
                    onValueChange = { dueTime = it },
                    label = { Text("Due Time") },
                    placeholder = { Text("07:30 PM") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                types.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type ==it,
                            onClick = {type = it}
                        )
                        Text(it.uppercase())
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorText,
                    color = Color.Red
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {

                    if (title.isNotBlank()){
                        val newTodo = TodoEntity(
                            id =todo?.id ?: 0,
                            title= title,
                            description = desc,
                            type=type,
                            isDone = todo?.isDone ?: false,
                            dueDate = dueDate,
                            dueTime = dueTime
                        )
                        onSave(newTodo)
                    }else{
                        errorText="Please enter the title of the task"
                    }
                }
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            Row {
                if (todo !=null){
                    TextButton(onClick = onDelete) {
                        Text("DELETE")
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("CLOSE")
                }
            }
        }
    )
}