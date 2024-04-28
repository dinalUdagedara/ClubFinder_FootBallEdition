package com.example.clubfinder_footballedition

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clubfinder_footballedition.ClubEntity
import com.example.clubfinder_footballedition.Leagues

@Dao
interface ClubDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll( vararg clubs: ClubEntity)



}
