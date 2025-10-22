package com.example.gameonapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text

@Composable
fun MainScreenButton(
    onClickMethod: () -> Unit,
    icon: ImageVector,
    buttonText: String,
    transformation: SurfaceTransformation
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        onClick = {
            onClickMethod()
        },
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        ButtonContent(icon = icon, buttonText = buttonText)
    }
}

@Composable
fun MainScreenOutlinedButon(
    onClickMethod: () -> Unit,
    icon: ImageVector,
    buttonText: String,
    transformation: SurfaceTransformation
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        onClick = {
            onClickMethod()
        },
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        transformation = transformation,
        ) {
        ButtonContent(icon = icon, buttonText = buttonText)
    }
}

@Composable
fun ButtonContent(
    icon: ImageVector,
    buttonText: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon, contentDescription = buttonText
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            buttonText,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}
