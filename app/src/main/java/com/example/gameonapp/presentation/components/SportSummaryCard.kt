package com.example.gameonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import com.example.gameonapp.data.local.model.GameEntity
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.data.local.model.VolleyballScore
import com.example.gameonapp.utils.fetchIconForGameType
import com.example.gameonapp.utils.formatDate
import com.example.gameonapp.utils.formatTime
import com.example.gameonapp.utils.toPascalCase

@Composable
fun SportSummaryCard(
    modifier: Modifier,
    contentData: GameEntity,
    navigateToDetails: () -> Unit,
    transformation: SurfaceTransformation,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        onClick = {
            navigateToDetails()
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        transformation = transformation,
        content = { SportSummaryContent(gameData = contentData) })
}

@Composable
fun SportSummaryContent(modifier: Modifier = Modifier, gameData: GameEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = fetchIconForGameType(gameData.gameType),
            contentDescription = gameData.gameType.toString()
        )
        Spacer(Modifier.width(6.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = gameData.gameType.toString().toPascalCase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                )
            )
            Text(
                text = formatDate(gameData.matchDate),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            when (val gameScore = gameData.score) {
                is SimpleScore -> Text(
                    text = "${gameScore.home} - ${gameScore.away}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                is VolleyballScore -> Text(
                    text = gameScore.getFinalScore(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                else -> Text("Unknown score")
            }
            Text(
                text = formatTime(gameData.durationSeconds),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            )
        }
    }
}