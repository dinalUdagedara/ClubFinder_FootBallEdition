package com.example.clubfinder_footballedition

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clubTable")
data class ClubEntity(
    @PrimaryKey val idTeam: String,
    val teamName: String,
    val strTeamShort: String,
    val strAlternate: String,
    val intFormedYear: String,
    val strLeague: String,
    val idLeague: String,
    val strStadium: String,
    val strKeywords: String,
    val strStadiumThumb: String,
    val strStadiumLocation: String,
    val intStadiumCapacity: String,
    val strWebsite: String,
    val strTeamJersey: String,
    val strTeamLogo: String
)