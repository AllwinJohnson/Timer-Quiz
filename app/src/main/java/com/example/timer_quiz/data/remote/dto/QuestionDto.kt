package com.example.timer_quiz.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuestionResponseDto(
    @SerializedName("questions")
    val questions: List<QuestionDto>
)

data class QuestionDto(
    @SerializedName("answer_id")
    val answerId: Int,
    @SerializedName("countries")
    val countries: List<CountryDto>,
    @SerializedName("country_code")
    val countryCode: String
)

data class CountryDto(
    @SerializedName("country_name")
    val countryName: String,
    @SerializedName("id")
    val id: Int
)