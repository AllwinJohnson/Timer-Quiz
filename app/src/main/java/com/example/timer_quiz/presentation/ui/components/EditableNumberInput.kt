package com.example.timer_quiz.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditableNumberInput(
    value: Int,
    onValueChange: (Int) -> Unit,
    maxValue: Int = 59,
    modifier: Modifier = Modifier
) {
    var textValue by remember(value) { mutableStateOf(value.toString().padStart(2, '0')) }

    Box(
        modifier = modifier
            .size(60.dp, 50.dp)
            .background(
                Color.White,
                RoundedCornerShape(6.dp)
            )
            .border(
                1.dp,
                Color(0xFFE0E0E0),
                RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = { newValue ->
                // Filter only digits and limit to 2 characters
                val filteredValue = newValue.filter { it.isDigit() }.take(2)
                textValue = filteredValue

                // Convert to int and validate range
                val intValue = filteredValue.toIntOrNull() ?: 0
                if (intValue <= maxValue) {
                    onValueChange(intValue)
                }
            },
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        ) { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (textValue.isEmpty()) {
                    Text(
                        text = "00",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF999999),
                        textAlign = TextAlign.Center
                    )
                }
                innerTextField()
            }
        }
    }
}
