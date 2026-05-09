package com.example.smartlife.ui.screen.journal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartlife.viewmodel.journalviewmodel.JournalViewModel
import java.util.Map.entry
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Image
import kotlin.contracts.contract
import coil.compose.AsyncImage
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEditor(
    date: String,
    viewModel: JournalViewModel,
    onClose:() -> Unit
) {
    val entryFlow =remember(date) {viewModel.getEntry(date) }
    val context = LocalContext.current
    val entry by entryFlow.collectAsState(null)
    var text by remember { mutableStateOf("") }
    var images by remember { mutableStateOf(listOf<ImageItem>()) }
    var isEditMode by remember { mutableStateOf(false) }
    var selectedImageIndex by remember {
        mutableStateOf<Int?>(null)
    }


    val launcher =rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            images = images+ ImageItem(it)
        }
    }
    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(entry) {
        val currentEntry = entry
        if (!isInitialized && currentEntry != null) {
            text = currentEntry.content ?: ""
            images = currentEntry.images
                ?.split("||SEP||")
                ?.filter { s -> s.isNotEmpty() }
                ?.map { s ->
                    val parts = s.split("|", limit = 4)
                    ImageItem(
                        uri = Uri.parse(parts[0]),
                        x = parts.getOrNull(1)?.toFloatOrNull() ?: 0f,
                        y = parts.getOrNull(2)?.toFloatOrNull() ?: 0f,
                        scale = parts.getOrNull(3)?.toFloatOrNull() ?: 1f
                    )
                } ?: emptyList()
            isInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text="Journal",
                            style= MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text=date,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isEditMode=! isEditMode
                        if (!isEditMode){
                            selectedImageIndex =null
                        }
                    }) {
                        if (isEditMode){
                            Icon(Icons.Default.Done,contentDescription = null)
                        }else{
                            Icon(Icons.Default.Edit,contentDescription = null)
                        }
                    }
                    IconButton(onClick = {
                        launcher.launch(arrayOf("image/*"))
                    }) {
                        Icon(Icons.Default.Image, contentDescription = "Add Image")
                    }
                    val canSave = text.trim().isNotEmpty() || images.isNotEmpty()

                    IconButton(
                        enabled = canSave,
                        onClick = {

                            val imageString = images.joinToString("||SEP||") {
                                "${it.uri}|${it.x}|${it.y}|${it.scale}"
                            }

                            viewModel.saveEntry(
                                date,
                                text.trim(),
                                imageString
                            )

                            onClose()
                        }
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "close")
                    }

                }
            )
        }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF5)),
                modifier = Modifier.fillMaxSize().padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFFBF5))
                        .padding(horizontal = 20.dp, vertical=16.dp)
                ){
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val lineSpacing=60f
                        var y= lineSpacing
                        while(y <size.height){
                            drawLine(
                                color = Color(0x22000000),
                                start = Offset(0f,y),
                                end = Offset(size.width,y),
                                strokeWidth = 1f
                            )
                            y += lineSpacing
                        }
                    }
                    BasicTextField(
                        value = text,
                        onValueChange = {
                            if (!isEditMode) {
                                text = it
                            }
                        },

                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            color = Color(0xFF1C1C1C)
                        ),

                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                            .zIndex(1f),

                        enabled = !isEditMode,
                        decorationBox = { innerTextField ->

                            Box {

                                if (text.isEmpty()) {
                                    Text(
                                        text = "Write your thoughts...",
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }

                                innerTextField()
                            }
                        }
                    )
                    images.forEachIndexed {index,item ->
                        AsyncImage(
                            model = item.uri,
                            contentDescription = null,
                            modifier = Modifier
                                .zIndex(if (isEditMode) 2f else 0f)
                                .offset{
                                    IntOffset(item.x.roundToInt(),item.y.roundToInt())
                                }
                                .pointerInput(isEditMode){
                                    detectTapGestures(
                                        onLongPress = {
                                            if (isEditMode){
                                                selectedImageIndex=index
                                            }
                                        }
                                    )
                                }.pointerInput(isEditMode){
                                    if (isEditMode){
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            images=images.toMutableList().also {
                                                val current = it[index]
                                                it[index]=current.copy(
                                                    x=current.x + pan.x ,
                                                    y = current.y + pan.y,
                                                    scale = (current.scale * zoom).coerceIn(0.5f,3f)
                                                )
                                            }
                                        }
                                    }
                                }.border(
                                    width =
                                        if (isEditMode && selectedImageIndex == index)
                                            3.dp
                                        else
                                            0.dp,

                                    color = Color.Red,

                                    shape = RoundedCornerShape(12.dp)
                                )
                                    .size((150 * item.scale).dp)

                        )
                    }
                    if (isEditMode && selectedImageIndex != null) {
                        FloatingActionButton(
                            onClick = {
                                val index = selectedImageIndex
                                if (index != null && index >= 0 && index < images.size) {
                                    images = images.toMutableList().also { it.removeAt(index) }
                                }
                                selectedImageIndex = null
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .zIndex(3f)  // ← must be higher than BasicTextField's zIndex(1f)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Image")
                        }
                    }
                }
            }
        }
    }
}