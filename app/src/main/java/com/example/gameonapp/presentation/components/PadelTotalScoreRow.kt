package com.example.gameonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.gameonapp.data.local.model.PadelMatchState
import com.example.gameonapp.data.local.model.PadelScore
import com.example.gameonapp.data.local.model.PadelServePosition
import com.example.gameonapp.data.local.model.PadelTiebreakState
import com.example.gameonapp.data.local.model.SetResult
import com.example.gameonapp.utils.HOME

@Composable
fun PadelTotalScoreRow(
    modifier: Modifier = Modifier,
    padelMatchState: PadelMatchState,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ServeIndicatorComponent(isHomePlayerServing = padelMatchState.servingIsHome)
        Spacer(modifier = Modifier.width(2.dp))
        ServePositionComponent(position = padelMatchState.servePosition)
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            PadelPlayerScoreComponent(
                playerScore = padelMatchState.homeScore,
                completedSets = padelMatchState.completedSets,
                tiebreak = padelMatchState.tiebreak,
                isStarPoint = padelMatchState.isStarPoint
            )
            Spacer(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(color = Color.White)

            )
            PadelPlayerScoreComponent(
                playerScore = padelMatchState.awayScore,
                completedSets = padelMatchState.completedSets,
                tiebreak = padelMatchState.tiebreak,
                isStarPoint = padelMatchState.isStarPoint
            )
        }
    }
}

@Composable
fun PadelPlayerScoreComponent(
    modifier: Modifier = Modifier,
    playerScore: PadelScore,
    completedSets: List<SetResult>,
    tiebreak: PadelTiebreakState?,
    isStarPoint: Boolean
) {
    val isHome = playerScore.name == HOME

    Row(
        modifier = modifier.padding(start = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.weight(1f),
            maxLines = 1,
            text = playerScore.display,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.width(14.dp))

        // Completed sets
        completedSets.forEach { set ->
            val gamesInSet = if (isHome) set.homeGames else set.awayGames
            Text(
                text = gamesInSet.toString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isHome) {
                        if (set.homePlayerWon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    } else {
                        if (!set.homePlayerWon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    }
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        // Current set games
        Text(
            text = playerScore.games.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.width(6.dp))

        // Current game points — tiebreak / golden point / normal
        val currentPointsText = when {
            tiebreak != null -> if (isHome) tiebreak.homePoints.toString() else tiebreak.awayPoints.toString()
            isStarPoint -> "SP"
            else -> ""
        }
        Text(
            text = currentPointsText,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = if (isStarPoint) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
fun ServePositionComponent(
    modifier: Modifier = Modifier,
    position: PadelServePosition
) {
    Text(
        modifier = modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        text = when (position) {
            PadelServePosition.RIGHT -> "R"
            PadelServePosition.LEFT -> "L"
        },
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
        )
    )
}
