package com.example.smartlife.ui.screen.journal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartlife.viewmodel.journalviewmodel.JournalViewModel
import java.time.YearMonth
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(viewModel: JournalViewModel) {

    var selectedDate by remember { mutableStateOf("") }
    var showEditor by remember { mutableStateOf(false) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth =currentMonth.lengthOfMonth()

    val startDayOffset = (firstDayOfMonth.dayOfWeek.value %7)
    val savedDates by viewModel.getAllEntryDates().collectAsState(emptyList())
    val today = LocalDate.now()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Focus")}
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    currentMonth =currentMonth.minusMonths(1)
                }) {
                    Icon(Icons.Default.ArrowLeft, contentDescription = "Back")
                }
                Text(text="${currentMonth.month} ${currentMonth.year}", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = {
                    currentMonth= currentMonth.plusMonths(1)
                }
                ) {
                    Icon(Icons.Default.ArrowRight, contentDescription = "forward")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("SUN","MON","TUE","WED","THU","FRI","SAT").forEach {
                    Text(
                        text = it,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxSize()
            ) {
                items(startDayOffset){
                    Box(modifier = Modifier.padding(4.dp))
                }
                items(daysInMonth){index ->
                    val day =index +1
                    val isToday =
                        day == today.dayOfMonth &&
                                currentMonth.monthValue ==today.monthValue &&
                                currentMonth.year == today.year
                    val dateKey = "$day-${currentMonth.monthValue}-${currentMonth.year}"
                    val isSaved = savedDates.contains(dateKey)
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clickable{
                                selectedDate=dateKey
                                showEditor=true
                            },
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        border = if (isToday) BorderStroke(2.dp,Color(0xFF1E88E5)) else null,
                        colors = CardDefaults.cardColors(
                            containerColor =
                                when {
                                    isToday ->
                                        Color(0xFF1E88E5)
                                    isSaved ->
                                        Color(0xFFB3E5FC)
                                    else ->
                                        Color(0xFFF5F5F5)
                                }
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(16.dp)
                        ){
                            Text(
                                text=day.toString(),
                                color =
                                    if (isToday) Color.White else Color(0xFF1C1C1C)
                                )
                        }
                    }
                }
            }
        }
    }

    if (showEditor && selectedDate.isNotEmpty()){
        JournalEditor(
            date =selectedDate,
            viewModel =viewModel,
            onClose ={showEditor=false}
        )
    }

}