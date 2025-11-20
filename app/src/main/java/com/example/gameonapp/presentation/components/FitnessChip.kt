package com.example.gameonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import com.example.gameonapp.utils.PULSE_METER
import com.example.gameonapp.utils.formatTime

@Composable
fun FitnessChip(
    modifier: Modifier = Modifier,
    value: Int,
    icon: ImageVector,
    type: Boolean = false,
) {
    val textValue = if (type == PULSE_METER) String.format("%3d BPM", value) else String.format(
        "%3d kCal",
        value
    )
    Column(
        modifier = Modifier
            .widthIn(min = 80.dp)
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Text(
            textValue, style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary,
            ), maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FitnessTimerChip(
    modifier: Modifier = Modifier,
    timeValue: Int,
) {
    val textValue = formatTime(timeValue)
    Row(
        modifier = Modifier
            .widthIn(min = 80.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.Timer,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            textValue, style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiary,
                fontFamily = Monospace
            )
        )
    }
}