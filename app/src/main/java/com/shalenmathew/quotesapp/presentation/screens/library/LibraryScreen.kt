package com.shalenmathew.quotesapp.presentation.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.CollectionType
import com.shalenmathew.quotesapp.presentation.screens.bottom_nav.Screen
import com.shalenmathew.quotesapp.presentation.screens.custom_quote.util.DeleteConfirmationDialog
import com.shalenmathew.quotesapp.presentation.screens.library.util.LibraryEvent
import com.shalenmathew.quotesapp.presentation.theme.GIFont
import com.shalenmathew.quotesapp.presentation.theme.customBlack
import com.shalenmathew.quotesapp.presentation.theme.customGrey
import com.shalenmathew.quotesapp.presentation.theme.customGrey2
import com.shalenmathew.quotesapp.presentation.viewmodel.LibraryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    paddingValues: PaddingValues,
    navHost: NavHostController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newCollectionName by remember { mutableStateOf("") }
    var editingCollection by remember { mutableStateOf<Collection?>(null) }
    var collectionToManage by remember { mutableStateOf<Collection?>(null) }
    var collectionToDelete by remember { mutableStateOf<Collection?>(null) }

    val scope = rememberCoroutineScope()

    val systemCollections = listOf(
        Collection(id = CollectionType.ID_FAV, name = "Favorites"),
        Collection(id = CollectionType.ID_CUSTOM, name = "Custom")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Library",
                fontSize = 35.sp,
                fontFamily = GIFont,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(systemCollections) { collection ->
                    LibraryCard(
                        name = collection.name,
                        onClick = {
                            navHost.navigate(Screen.CollectionDetail.route + "/${collection.id}")
                        }
                    )
                }
                items(state.collections) { collection ->
                    LibraryCard(
                        name = collection.name,
                        onMoreClick = { collectionToManage = collection },
                        onClick = {
                            navHost.navigate(Screen.CollectionDetail.route + "/${collection.id}")
                        }
                    )
                }
            }
        }

        if (collectionToManage != null) {
            ManageCollectionBottomSheet(
                collection = collectionToManage!!,
                onDismiss = { collectionToManage = null },
                onEdit = {
                    editingCollection = it
                    newCollectionName = it.name
                    showEditDialog = true
                    collectionToManage = null
                },
                onDelete = {
                    collectionToDelete = it
                    collectionToManage = null
                }
            )
        }

        if (collectionToDelete != null) {
            DeleteConfirmationDialog(
                title = "Delete Collection?",
                onConfirm = {
                    viewModel.onEvent(LibraryEvent.DeleteCollection(collectionToDelete!!))
                    collectionToDelete = null
                },
                onDismiss = { collectionToDelete = null }
            )
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color.White,
            contentColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Collection")
        }

        if (showAddDialog) {
            CollectionNameDialog(
                title = "New Collection",
                value = newCollectionName,
                onValueChange = { newCollectionName = it },
                onConfirm = {
                    viewModel.onEvent(LibraryEvent.AddCollection(newCollectionName))
                    newCollectionName = ""
                    showAddDialog = false
                },
                onDismiss = {
                    newCollectionName = ""
                    showAddDialog = false
                }
            )
        }

        if (showEditDialog && editingCollection != null) {
            CollectionNameDialog(
                title = "Edit Collection",
                value = newCollectionName,
                onValueChange = { newCollectionName = it },
                onConfirm = {
                    viewModel.onEvent(LibraryEvent.UpdateCollection(editingCollection!!.copy(name = newCollectionName)))
                    newCollectionName = ""
                    showEditDialog = false
                    editingCollection = null
                },
                onDismiss = {
                    newCollectionName = ""
                    showEditDialog = false
                    editingCollection = null
                }
            )
        }
    }
}

@Composable
fun CollectionNameDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = customGrey2,
        title = { 
            Text(
                title, 
                color = Color.White, 
                fontFamily = GIFont
            ) 
        },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Enter collection name...", color = Color.Gray) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (value.isNotBlank()) {
                        onConfirm()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCollectionBottomSheet(
    collection: Collection,
    onDismiss: () -> Unit,
    onEdit: (Collection) -> Unit,
    onDelete: (Collection) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = customGrey2
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 8.dp)
        ) {
            Text(
                text = collection.name,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = GIFont,
                fontWeight = FontWeight.Bold
            )
            
            BottomSheetItem(
                text = "Edit Name",
                icon = Icons.Default.Edit,
                onClick = { onEdit(collection) }
            )
            
            BottomSheetItem(
                text = "Delete Collection",
                icon = Icons.Rounded.Delete,
                color = Color.Red.copy(alpha = 0.8f),
                onClick = { onDelete(collection) }
            )
        }
    }
}

@Composable
fun BottomSheetItem(
    text: String,
    icon: ImageVector,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = color,
            fontSize = 18.sp,
            fontFamily = GIFont
        )
    }
}

@Composable
fun LibraryCard(
    name: String,
    onMoreClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(customGrey2)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                fontFamily = GIFont,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onMoreClick != null) {
                    IconButton(onClick = onMoreClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
