package com.example.clubfinder_footballedition

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SearchForClubWebService : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    SearchForClubWebServiceGUI()
                }
            }
        }
    }
}

@Composable
fun SearchForClubWebServiceGUI(){

    var leagueInfo by remember { mutableStateOf(" ") }
    var teamsInfo by remember { mutableStateOf("") }

    var searchTerm by remember { mutableStateOf("") }
    var keyword by remember { mutableStateOf("") }

    // Creates a CoroutineScope bound to the GUI composable lifecycle
    val scope = rememberCoroutineScope()



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = searchTerm, onValueChange = { searchTerm = it } )
        Button(onClick = {
            scope.launch {

                val allLeagues = fetchAllLeagues(keyword)
                leagueInfo = allLeagues.joinToString("\n\n") { league ->
                    buildString {
                        append("idLeague: ${league.idLeague}\n")
                        append("strLeague: ${league.strLeague}\n")
                        append("strSport: ${league.strSport}\n")
                        append("strLeagueAlternate: ${league.strLeagueAlternate}\n")


                    }
                }
//                leagueList.forEach{league ->
//
//                    val allTeams = fetchAllClubs(league.strLeague)
//                }

            }
        }) {
            Text("Fetch Leagues")
        }

        Button(onClick = {

            //To Display fetched TeamInfo

            scope.launch {

                leagueList.forEach{league ->

                    val allTeams = fetchAllClubs(league.strLeague)
                    val leagueTeamsInfo = allTeams.joinToString("\n\n") { team ->
                        buildString {
                            append("Team ID: ${team.idTeam}\n")
                            append("Team Name: ${team.teamName}\n")
                        }

                    }
                    teamsInfo += leagueTeamsInfo + "\n\n" // Add teams info for the current league to the accumulated teamsInfo

                }
                Log.d("teamNamesinList","$teamsInfo")


            }


        }) {
            Text("Fetch Teams")
        }




        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = teamsInfo
        )
        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = leagueInfo
        )


    }
}


val leagueList = mutableListOf<Leagues>()

suspend fun fetchAllLeagues(keyword : String): List<Leagues> {

//    val encodedKeyword2 = URLEncoder.encode(keyword, "UTF-8")
//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$encodedKeyword2"

    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=$keyword"


//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=Eliteserien"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection


    withContext(Dispatchers.IO) {
        val bf = BufferedReader(InputStreamReader(con.inputStream))
        val stb = StringBuilder()
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }

        val json = JSONObject(stb.toString())
        val jsonArray = json.getJSONArray("leagues")

        for (i in 0 until jsonArray.length()) {
            val leagueObject = jsonArray.getJSONObject(i)
            val league = Leagues(
                idLeague = leagueObject.getString("idLeague"),
                strLeague = leagueObject.getString("strLeague"),
                strSport = leagueObject.getString("strSport"),
                strLeagueAlternate = leagueObject.getString("strLeagueAlternate")

            )
            leagueList.add(league)
        }
    }

    return leagueList
}

suspend fun fetchAllClubs(keyword : String): List<ClubEntity> {
    Log.d("Passed Param","League Name: $keyword")
    val teamsList = mutableListOf<ClubEntity>()

    val encodedKeyword2 = URLEncoder.encode(keyword, "UTF-8")
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$encodedKeyword2"

//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English%20Premier%20League"
//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=$keyword"


//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=Eliteserien"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection


    withContext(Dispatchers.IO) {
        val bf = BufferedReader(InputStreamReader(con.inputStream))
        val stb = StringBuilder()
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }

        val json = JSONObject(stb.toString())
        val jsonArray = json.getJSONArray("teams")

        for (i in 0 until jsonArray.length()) {
            val leagueObject = jsonArray.getJSONObject(i)
            val team = ClubEntity(
                idTeam = leagueObject.getString("idTeam"),
                teamName = leagueObject.getString("strTeam"),
                strTeamShort = leagueObject.getString("strTeamShort"),
                strAlternate = leagueObject.getString("strAlternate"),
                intFormedYear = leagueObject.getString("intFormedYear"),
                strLeague = leagueObject.getString("strLeague"),
                idLeague = leagueObject.getString("idLeague"),
                strStadium = leagueObject.getString("strStadium"),
                strKeywords = leagueObject.getString("strKeywords"),
                strStadiumThumb = leagueObject.getString("strStadiumThumb"),
                strStadiumLocation = leagueObject.getString("strStadiumLocation"),
                intStadiumCapacity = leagueObject.getString("strLeague"),
                strWebsite = leagueObject.getString("strWebsite"),
                strTeamJersey = leagueObject.getString("strTeamJersey"),
                strTeamLogo = leagueObject.getString("strTeamLogo")


            )
            Log.d("teamName","${team.teamName}")
            teamsList.add(team)



        }

    }

    return teamsList
    Log.d("fetchClub","Done")
}

