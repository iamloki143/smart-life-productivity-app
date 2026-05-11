package com.example.smartlife.ui.screen.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDialog(
    todo: TodoEntity?,
    onDismiss:() -> Unit,
    onSave:(TodoEntity) -> Unit,
    onDelete:() -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(todo?.title ?: "") }
    var desc by remember {mutableStateOf(todo?.description ?: "")}
    var type by remember { mutableStateOf(todo?.type ?: "day") }
    var errorText by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(todo?.dueDate ?: "") }
    var dueTime by remember { mutableStateOf(todo?.dueTime ?: "") }
    var priority by remember {
        mutableStateOf(todo?.priority ?: "LOW")
    }
    val priorities = listOf("LOW", "MEDIUM", "HIGH")

    val types=listOf("day","week","month","year")
    val calendar = Calendar.getInstance()

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
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Due Date") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {

                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->

                                        calendar.set(
                                            Calendar.YEAR,
                                            year
                                        )

                                        calendar.set(
                                            Calendar.MONTH,
                                            month
                                        )

                                        calendar.set(
                                            Calendar.DAY_OF_MONTH,
                                            dayOfMonth
                                        )

                                        dueDate = SimpleDateFormat(
                                            "dd/MM/yyyy",
                                            Locale.getDefault()
                                        ).format(calendar.time)

                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)

                                ).show()
                            }
                        ) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = dueTime,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Due Time") },
                    readOnly = true,
                    trailingIcon = {

                        IconButton(
                            onClick = {

                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->

                                        calendar.set(
                                            Calendar.HOUR_OF_DAY,
                                            hour
                                        )

                                        calendar.set(
                                            Calendar.MINUTE,
                                            minute
                                        )

                                        dueTime = SimpleDateFormat(
                                            "hh:mm a",
                                            Locale.getDefault()
                                        ).format(calendar.time)

                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    false

                                ).show()
                            }
                        ) {

                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null
                            )
                        }
                    }
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

                Text("Priority")

                priorities.forEach {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = priority == it,
                            onClick = { priority = it }
                        )

                        Text(it)
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
                            dueTime = dueTime,
                            priority = priority
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