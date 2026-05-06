package com.example.smartlife.ui.screen.journal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                Text("${currentMonth.month} ${currentMonth.year}")
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
                        textAlign = TextAlign.Center
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
                            .fillMaxWidth()
                            .clickable{
                                selectedDate=dateKey
                                showEditor=true
                            },
                        border = if (isToday) BorderStroke(2.dp,Color.Red) else null,
                        colors = CardDefaults.cardColors(
                            containerColor = if(isSaved) Color(0xFF90CAF9) else Color.White
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(16.dp)
                        ){
                            Text(day.toString())
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