package com.example.timer_quiz.domain.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Country(
    val id: Int,
    val name: String
) : Parcelable
