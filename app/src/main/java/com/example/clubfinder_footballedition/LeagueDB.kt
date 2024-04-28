
package com.example.clubfinder_footballedition


import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [Leagues::class, ClubEntity::class], version = 1)
abstract class LeagueDB : RoomDatabase() {
    abstract fun leaguesDAO(): LeagueDAO
    abstract fun clubDao(): ClubDao
}