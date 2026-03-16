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
import com.example.gameonapp.data.local.model.SetResult
import com.example.gameonapp.data.local.model.TennisMatchState
import com.example.gameonapp.data.local.model.TennisScore
import com.example.gameonapp.utils.HOME

@Composable
fun TennisTotalScoreRow(
    modifier: Modifier = Modifier,
    tennisMatchState: TennisMatchState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ServeIndicatorComponent(isHomePlayerServing = tennisMatchState.servingIsHome)
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            TennisPlayerScoreComponent(
                playerScore = tennisMatchState.homeScore,
                completedSets = tennisMatchState.completedSets,
                isTiebreak = tennisMatchState.isTiebreak,
                tieBreakPoints = tennisMatchState.homeTiebreakPoints
            )
            Spacer(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(color = Color.White)

            )
            TennisPlayerScoreComponent(
                playerScore = tennisMatchState.awayScore,
                completedSets = tennisMatchState.completedSets,
                isTiebreak = tennisMatchState.isTiebreak,
                tieBreakPoints = tennisMatchState.awayTiebreakPoints
            )
        }
    }
}

@Composable
fun TennisPlayerScoreComponent(
    modifier: Modifier = Modifier,
    playerScore: TennisScore,
    completedSets: List<SetResult>,
    tieBreakPoints: Int = 0,
    isTiebreak: Boolean = false
) {
    Row(
        modifier = Modifier.padding(start = 6.dp),
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
        completedSets.forEach { set ->
            val gamesInSet = if (playerScore.name == HOME) set.homeGames else set.awayGames
            Text(
                text = gamesInSet.toString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (playerScore.name == HOME) {
                        if (set.homePlayerWon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    } else {
                        if (!set.homePlayerWon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    }
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = playerScore.games.toString(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        if (isTiebreak) {
            Spacer(modifier = Modifier.width(6.dp))
            val currentPointsText = tieBreakPoints.toString()
            Text(
                text = currentPointsText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun ServeIndicatorComponent(modifier: Modifier = Modifier, isHomePlayerServing: Boolean) {
    Column {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = "",
            tint = if (isHomePlayerServing) Color.White else Color.Transparent
        )
        Spacer(modifier = Modifier.height(10.dp))
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Icons.Default.FiberManualRecord,
            contentDescription = "",
            tint = if (!isHomePlayerServing) Color.White else Color.Transparent
        )
    }
}