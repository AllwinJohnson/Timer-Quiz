package com.example.timer_quiz.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.timer_quiz.presentation.ui.theme.FlagsColors

@Composable
 fun FlagImage(flagUrl: String) {
    SubcomposeAsyncImage(
        model = flagUrl,
        contentDescription = "Country flag",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(140.dp, 90.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, FlagsColors.BackgroundWhite, RoundedCornerShape(8.dp)),
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = FlagsColors.OrangePrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlagsColors.BackgroundWhite),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Flag not available",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    )
}