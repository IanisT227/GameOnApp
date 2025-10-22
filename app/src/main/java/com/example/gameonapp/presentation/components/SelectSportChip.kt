package com.example.gameonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.SurfaceTransformation

@Composable
fun SelectSportChip(modifier: Modifier = Modifier, transformation: SurfaceTransformation) {
    OutlinedButton(
        modifier = modifier,
        onClick = {},
        enabled = true,
        label = {
            Text(
                text = "Fotbal", maxLines = 1, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        icon = {
            Icon(
                Icons.Outlined.Call,
                contentDescription = "call",
                modifier = Modifier
                    .size(ChipDefaults.IconSize)
                    .wrapContentSize(align = Alignment.Center),
            )
        },
        transformation = transformation,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,

        ),
        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary)
    )
}