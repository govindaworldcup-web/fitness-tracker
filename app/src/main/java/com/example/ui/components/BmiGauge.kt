package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BmiResult
import com.example.ui.theme.BmiNormalColor
import com.example.ui.theme.BmiObeseColor
import com.example.ui.theme.BmiOverweightColor
import com.example.ui.theme.BmiUnderweightColor

@Composable
fun BmiGaugeCard(
    bmiResult: BmiResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bmi_gauge_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Body Mass Index (BMI)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Formula: Weight (kg) / Height (m)²",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(bmiResult.color.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = bmiResult.category,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = bmiResult.color
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // BMI Big Score
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = String.format("%.1f", bmiResult.bmi),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 38.sp
                    ),
                    color = bmiResult.color
                )
                Text(
                    text = " kg/m²",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Continuous Visual Color Bar Gauge with Pointer
            BmiVisualBar(currentBmi = bmiResult.bmi.toFloat())

            Spacer(modifier = Modifier.height(12.dp))

            // Healthy weight span
            Text(
                text = "Healthy weight range: ${bmiResult.minHealthyWeightKg} kg – ${bmiResult.maxHealthyWeightKg} kg",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = bmiResult.advice,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BmiVisualBar(
    currentBmi: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
        ) {
            val totalWidth = size.width
            val barHeight = 10.dp.toPx()
            val barY = 14.dp.toPx()

            // BMI range from 15 to 40 for scale
            val minScale = 15f
            val maxScale = 40f
            val clampedBmi = currentBmi.coerceIn(minScale, maxScale)
            val indicatorFraction = (clampedBmi - minScale) / (maxScale - minScale)

            // Segment widths
            // Underweight: 15 to 18.5 -> (3.5 / 25) = 14%
            // Normal: 18.5 to 25 -> (6.5 / 25) = 26%
            // Overweight: 25 to 30 -> (5 / 25) = 20%
            // Obese: 30 to 40 -> (10 / 25) = 40%
            val seg1 = ((18.5f - 15f) / 25f) * totalWidth
            val seg2 = ((25f - 18.5f) / 25f) * totalWidth
            val seg3 = ((30f - 25f) / 25f) * totalWidth
            val seg4 = totalWidth - (seg1 + seg2 + seg3)

            // Draw 4 segments
            drawRoundRect(
                color = BmiUnderweightColor,
                topLeft = Offset(0f, barY),
                size = Size(seg1, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            drawRect(
                color = BmiNormalColor,
                topLeft = Offset(seg1, barY),
                size = Size(seg2, barHeight)
            )
            drawRect(
                color = BmiOverweightColor,
                topLeft = Offset(seg1 + seg2, barY),
                size = Size(seg3, barHeight)
            )
            drawRoundRect(
                color = BmiObeseColor,
                topLeft = Offset(seg1 + seg2 + seg3, barY),
                size = Size(seg4, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // Indicator Needle / Circle
            val needleX = (indicatorFraction * totalWidth).coerceIn(4.dp.toPx(), totalWidth - 4.dp.toPx())
            drawCircle(
                color = Color.White,
                radius = 7.dp.toPx(),
                center = Offset(needleX, barY + barHeight / 2)
            )
            drawCircle(
                color = Color(0xFF1E293B),
                radius = 4.dp.toPx(),
                center = Offset(needleX, barY + barHeight / 2)
            )
        }

        // Labels underneath
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("< 18.5", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("18.5 - 24.9", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("25 - 29.9", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("30+", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
