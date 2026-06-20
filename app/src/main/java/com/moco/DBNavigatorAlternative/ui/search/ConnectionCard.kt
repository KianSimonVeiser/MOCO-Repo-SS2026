package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConnectionCard(
    connection: ConnectionData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${connection.depTime} -> ${connection.arrTime}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (connection.isLate) Color.Red else Color.Black
                    )

                    ScoreBadge(
                        score = connection.score
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    connection.trains.forEach { train ->
                        TrainBadge(
                            name = train.name,
                            color = train.color
                        )
                    }
                }

                if (connection.showBindingHint) {
                    BindingHint()
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE0E0E0)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun ScoreBadge(
    score: Double
) {
    val backgroundColor = when {
        score >= 8.0 -> Color(0xFF76B82A)
        score >= 5.0 -> Color(0xFFFFD700)
        else -> Color(0xFFE2104E)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = score.toString(),
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 4.dp
            ),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TrainBadge(
    name: String,
    color: Color
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun BindingHint() {
    Surface(
        color = Color(0xFF76B82A).copy(alpha = 0.8f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = "Zugbindung zu 94% aufgehoben",
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}