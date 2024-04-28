package com.example.clubfinder_footballedition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "League Table")
data class Leagues(

    @PrimaryKey val idLeague: String,
    val strLeague: String,
    val strSport: String?,
    val strLeagueAlternate: String?
)
