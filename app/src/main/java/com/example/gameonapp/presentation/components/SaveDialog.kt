package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

@Composable
fun SaveDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        visible = isVisible,
        onDismissRequest = { onDismiss() },
        confirmButton = { DialogButton(buttonText = "Yes", onClickAction = { onConfirmClick() }) },
        dismissButton = { DialogButton(buttonText = "No", onClickAction = { onDismiss() }) },
        title = { Text("End game") },
        icon = { Icons.Outlined.Save },
        text = { Text("Are you sure you want to finish and save your session?") },
        verticalArrangement = Arrangement.SpaceEvenly,
        content = {}
    )
}

@Composable
fun DeleteGameDialog(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        visible = isVisible,
        onDismissRequest = { onDismiss() },
        confirmButton = { DialogButton(buttonText = "Yes", onClickAction = { onConfirmClick() }) },
        dismissButton = { DialogButton(buttonText = "No", onClickAction = { onDismiss() }) },
        title = { Text("Delete game") },
        icon = { Icons.Outlined.Save },
        text = { Text("Are you sure you want to remove this session?") },
        verticalArrangement = Arrangement.SpaceEvenly,
        content = {}
    )
}

@Composable
fun DialogButton(modifier: Modifier = Modifier, buttonText: String, onClickAction: () -> Unit) {
    Button(
        modifier = Modifier.width(80.dp),
        onClick = { onClickAction() },
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                buttonText, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}