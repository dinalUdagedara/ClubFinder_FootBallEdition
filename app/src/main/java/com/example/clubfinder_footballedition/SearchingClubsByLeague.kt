package com.example.clubfinder_footballedition


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SearchingClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Content()
                    GUI(context = applicationContext )


                }
            }
        }
    }
}



@Composable
fun GUI(context: Context) {
    var teamInfoDisplay by remember { mutableStateOf(" ") }
// the book title keyword to search for
    var keyword by remember { mutableStateOf("") }
// Creates a CoroutineScope bound to the GUI composable lifecycle
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Column() {
                Button(onClick = {
                    scope.launch {
                        val teams = fetchLeagues(keyword)
                        teamInfoDisplay = teams.joinToString("\n\n") { team ->
                            buildString {
                                append("Team ID: ${team.idTeam}\n")
                                append("Team Name: ${team.teamName}\n")
                                append("strTeamShort: ${team.strTeamShort}\n")
                                append("strAlternate: ${team.strAlternate}\n")
                                append("intFormedYear: ${team.intFormedYear}\n")
                                append("strLeague: ${team.strLeague}\n")
                                append("idLeague: ${team.idLeague}\n")
                                append("strStadium: ${team.strStadium}\n")
                                append("strKeywords: ${team.strKeywords}\n")
                                append("strStadiumThumb: ${team.strStadiumThumb}\n")
                                append("strStadiumLocation: ${team.strStadiumLocation}\n")
                                append("intStadiumCapacity: ${team.intStadiumCapacity}\n")
                                append("strWebsite: ${team.strWebsite}\n")
                                append("strTeamJersey: ${team.strTeamJersey}\n")
                                append("strTeamLogo: ${team.strTeamLogo}\n")

                            }
                        }
                    }
                }) {
                    Text("Fetch Teams")
                }
                Button(onClick = {
                    addSeacrhedLeaguesToDB(context)
                }) {
                    Text(text = " Save clubs to Database")

                }
            }

            TextField(value = keyword, onValueChange = { keyword = it })

        }
        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = teamInfoDisplay
        )
    }
}

val teamsList = mutableListOf<ClubEntity>()
suspend fun fetchLeagues(keyword : String): List<ClubEntity> {
    val encodedKeyword = URLEncoder.encode(keyword, "UTF-8")
//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$encodedKeyword"

    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=Eliteserien"
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
            teamsList.add(team)
        }
    }

    return teamsList
}


fun addSeacrhedLeaguesToDB(context: Context){


    val club1 = ClubEntity(
        idTeam = "1345",
        teamName = "arsenal",
        strTeamShort = "shortName",
        strAlternate = "alternateName",
        intFormedYear = "1997",
        strLeague ="legue1",
        idLeague = "123",
        strStadium = "Mahinda Rajapaksha",
        strKeywords = "keyword",
        strStadiumThumb = "thumb",
        strStadiumLocation = "location",
        intStadiumCapacity = "capacity",
        strWebsite = "website",
        strTeamJersey = "jersey",
        strTeamLogo = "Logo" )



    GlobalScope.launch (Dispatchers.IO){
        val db = Room.databaseBuilder(
            context.applicationContext,
            LeagueDB::class.java,"Leagues3"
        ).build()
        val ClubDao = db.clubDao()

//        ClubDao.insertAll(club1)
        for (team in teamsList) {
            ClubDao.insertAll(team)
        }


    }

}