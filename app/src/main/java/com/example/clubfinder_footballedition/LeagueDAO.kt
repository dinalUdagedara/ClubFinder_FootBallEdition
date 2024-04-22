package com.example.clubfinder_footballedition

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LeagueDAO {
    @Query ("select * from leagues")
        fun getAll():List<Leagues>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg leagues: Leagues)  //Duplicates are Allowed


}