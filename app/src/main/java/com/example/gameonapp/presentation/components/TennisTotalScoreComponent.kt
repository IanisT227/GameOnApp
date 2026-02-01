package com.example.gameonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

@Composable
fun TennisTotalScoreComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ServeIndicatorComponent(isServing = false)
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            TennisPlayerScoreComponent()
            Spacer(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(color = Color.White)

            )
            TennisPlayerScoreComponent()
        }
    }
}

@Composable
fun TennisPlayerScoreComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            "Home".uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            "6", style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            "6", style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            "6", style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
        )


    }
}

@Composable
fun ServeIndicatorComponent(modifier: Modifier = Modifier, isServing: Boolean) {
    Column {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = "",
            tint = if (!isServing) Color.Transparent else Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = "",
            tint = if (isServing) Color.Transparent else Color.White
        )
    }
}