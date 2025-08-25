package com.example.timer_quiz.domain.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Question(
    val answerId: Int,
    val countries: List<Country>,
    val countryCode: String,
    val flagUrl: String = "https://countryflagsapi.netlify.app/flag/${countryCode}.svg"
) : Parcelable {
    fun getCorrectCountry(): Country {
        return countries.first { it.id == answerId }
    }

    fun isCorrectAnswer(selectedCountryId: Int): Boolean {
        return selectedCountryId == answerId
    }
}