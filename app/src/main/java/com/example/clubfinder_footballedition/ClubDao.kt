package com.example.clubfinder_footballedition

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ClubDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll( vararg clubs: ClubEntity)

//    @Query("SELECT * FROM Clubs Table WHERE teamName LIKE '%' || :searchQuery || '%' OR strLeague LIKE '%' || :searchQuery || '%'")
//    suspend fun searchClubs(searchQuery: String) : String

     @Query("SELECT * FROM clubTable")
     suspend fun searchClubsTesting(): List<ClubEntity>

    @Query("SELECT * FROM clubTable WHERE lower(teamName) LIKE '%' || lower(:searchQuery) || '%' OR lower(strLeague) LIKE '%' || lower(:searchQuery) || '%'")
    suspend fun searchClubs(searchQuery: String): List<ClubEntity>


    companion object


}
