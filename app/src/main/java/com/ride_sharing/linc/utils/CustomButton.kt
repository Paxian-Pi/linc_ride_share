package com.ride_sharing.linc.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    mainText: String,
    subText: String,
    mainTextColor: Color = Color.White,
    subTextColor: Color = Color.White,
    containerColor: Color,
    iconName: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .defaultMinSize(minWidth = 5.dp, minHeight = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssetImage(
                iconName = iconName,
                width = 35.dp,
                height = 35.dp
            )
            Column {
                Text(
                    mainText,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    color = mainTextColor,
                    fontWeight = FontWeight.W600
                )
                Text(
                    subText,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    color = subTextColor,
                )
            }
        }
    }
}