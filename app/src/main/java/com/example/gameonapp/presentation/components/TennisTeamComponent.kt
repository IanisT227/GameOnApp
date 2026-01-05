package com.example.gameonapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import java.util.Locale

@Composable
fun TennisTeamComponent(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable(enabled = true, onClick = { onClick() })
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    )
    {
        Text(
            "home".uppercase(Locale.ROOT), style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )

        Text(
            "15".toString(), style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.ExtraBold,
            )
        )
    }
}