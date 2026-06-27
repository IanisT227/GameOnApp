package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.gameonapp.R

@Composable
fun SaveDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        visible = isVisible,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.yes),
                onClickAction = { onConfirmClick() })
        },
        dismissButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.no),
                onClickAction = { onDismiss() })
        },
        title = { Text(text = stringResource(id = R.string.end_game)) },
        icon = { Icons.Outlined.Save },
        text = { Text(text = stringResource(id = R.string.end_game_alert)) },
        verticalArrangement = Arrangement.SpaceEvenly,
        content = {}
    )
}

@Composable
fun DeleteGameDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        visible = isVisible,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.yes),
                onClickAction = { onConfirmClick() })
        },
        dismissButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.no),
                onClickAction = { onDismiss() })
        },
        title = { Text(text = stringResource(id = R.string.delete_game)) },
        icon = { Icons.Outlined.Save },
        text = { Text(text = stringResource(id = R.string.remove_session_alert)) },
        verticalArrangement = Arrangement.SpaceEvenly,
        content = {}
    )
}

@Composable
fun EndGameDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        visible = isVisible,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.yes),
                onClickAction = { onConfirmClick() })
        },
        dismissButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.no),
                onClickAction = { onDismiss() })
        },
        title = { Text(text = stringResource(id = R.string.game_finished)) },
        icon = { Icons.Outlined.Save },
        text = { Text(text = stringResource(id = R.string.finished_game_alert)) },
        verticalArrangement = Arrangement.SpaceEvenly,
        content = {}
    )
}

@Composable
fun ExitGameDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    AlertDialog(
        visible = isVisible,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.yes),
                onClickAction = { onConfirmClick() })
        },
        dismissButton = {
            DialogButton(
                buttonText = stringResource(id = R.string.no),
                onClickAction = { onDismiss() })
        },
        title = { Text(text = stringResource(id = R.string.cancel_game)) },
        icon = { Icons.Outlined.Close },
        text = { Text(text = stringResource(id = R.string.cancel_game_alert)) },
        verticalArrangement = Arrangement.SpaceEvenly,
        content = {}
    )
}

@Composable
fun DialogButton(buttonText: String, onClickAction: () -> Unit) {
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