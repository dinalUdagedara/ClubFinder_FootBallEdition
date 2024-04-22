package com.example.clubfinder_footballedition

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Leagues::class], version = 1)

abstract class LeagueDB : RoomDatabase(){
//    abstract fun leagueDao() : LeagueDAO
    abstract fun leaguesDAO(): LeagueDAO
}