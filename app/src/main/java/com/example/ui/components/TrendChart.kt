package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodLogEntry
import com.example.util.DateUtils
import kotlin.math.max

data class DayTrend(
    val isoDate: String,
    val dayLabel: String,
    val calories: Int
)

@Composable
fun TrendChartCard(
    allEntries: List<FoodLogEntry>,
    targetCalories: Int,
    onDaySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val last7Days = DateUtils.getLast7DaysIso()
    val dayTrends = last7Days.map { date ->
        val total = allEntries.filter { it.date == date }.sumOf { it.calories }
        val label = if (date == DateUtils.getTodayIso()) "Today" else DateUtils.getShortDayLabel(date)
        DayTrend(date, label, total)
    }

    val maxCalorieInView = max(
        targetCalories.toFloat() * 1.25f,
        (dayTrends.maxOfOrNull { it.calories } ?: 0).toFloat() * 1.15f
    ).coerceAtLeast(1000f)

    val averageCal = if (dayTrends.isNotEmpty()) {
        (dayTrends.sumOf { it.calories } / 7)
    } else 0

    val onTargetDays = dayTrends.count { it.calories in (targetCalories - 200)..(targetCalories + 200) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("trend_chart_card"),
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
                        text = "7-Day Calorie Trend",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Target: $targetCalories kcal/day",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Avg: $averageCal kcal",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chart Canvas
            val primaryColor = MaterialTheme.colorScheme.primary
            val targetLineColor = MaterialTheme.colorScheme.tertiary
            val overColor = Color(0xFFEF4444)
            val underColor = Color(0xFF38BDF8)
            val emptyColor = MaterialTheme.colorScheme.surfaceVariant

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 24.dp.toPx()
                    val chartHeight = height - bottomPadding

                    // Target line Y
                    val targetY = chartHeight - (targetCalories / maxCalorieInView) * chartHeight
                    if (targetY in 0f..chartHeight) {
                        drawLine(
                            color = targetLineColor,
                            start = Offset(0f, targetY),
                            end = Offset(width, targetY),
                            strokeWidth = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }

                    // Draw 7 Bars
                    val numBars = dayTrends.size
                    val slotWidth = width / numBars
                    val barWidth = slotWidth * 0.45f

                    dayTrends.forEachIndexed { index, trend ->
                        val barCenterX = (index * slotWidth) + (slotWidth / 2f)
                        val barLeft = barCenterX - (barWidth / 2f)

                        val barHeight = if (trend.calories > 0) {
                            (trend.calories / maxCalorieInView) * chartHeight
                        } else {
                            4.dp.toPx() // Minimal stub for 0-entry day
                        }

                        val barTop = chartHeight - barHeight

                        val barColor = when {
                            trend.calories == 0 -> emptyColor
                            trend.calories > targetCalories + 250 -> overColor
                            trend.calories < targetCalories - 400 -> underColor
                            else -> primaryColor
                        }

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(barLeft, barTop),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )
                    }
                }

                // Overlay row for labels & tap handlers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    dayTrends.forEach { trend ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onDaySelect(trend.isoDate) }
                                .padding(vertical = 2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = trend.dayLabel,
                                fontSize = 11.sp,
                                fontWeight = if (trend.dayLabel == "Today") FontWeight.Bold else FontWeight.Normal,
                                color = if (trend.dayLabel == "Today") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Legend & Summary Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "• Target line: $targetCalories kcal",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "• On Target: $onTargetDays / 7 days",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
