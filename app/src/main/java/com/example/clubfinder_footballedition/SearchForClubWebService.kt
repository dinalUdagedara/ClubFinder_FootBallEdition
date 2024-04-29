package com.example.clubfinder_footballedition

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
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

    var clubInfoDisplay by remember { mutableStateOf(" ") }
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

                val retrievedClubs = fetchClubs(keyword)
                clubInfoDisplay = retrievedClubs.joinToString("\n\n") { club ->
                    buildString {
                        append("Team ID: ${club.idTeam}\n")
                        append("Team Name: ${club.teamName}\n")
                        append("strTeamShort: ${club.strTeamShort}\n")
                        append("strAlternate: ${club.strAlternate}\n")
                        append("intFormedYear: ${club.intFormedYear}\n")
                        append("strLeague: ${club.strLeague}\n")
                        append("idLeague: ${club.idLeague}\n")
                        append("strStadium: ${club.strStadium}\n")
                        append("strKeywords: ${club.strKeywords}\n")
                        append("strStadiumThumb: ${club.strStadiumThumb}\n")
                        append("strStadiumLocation: ${club.strStadiumLocation}\n")
                        append("intStadiumCapacity: ${club.intStadiumCapacity}\n")
                        append("strWebsite: ${club.strWebsite}\n")
                        append("strTeamJersey: ${club.strTeamJersey}\n")
                        append("strTeamLogo: ${club.strTeamLogo}\n")

                    }
                }
            }
        }) {
            Text("Look Up")
        }
//        Text(text = teamsList2)
        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = clubInfoDisplay
        )
    }
}


val teamsList2 = mutableListOf<ClubEntity>()
suspend fun fetchClubs(keyword : String): List<ClubEntity> {
    val encodedKeyword = URLEncoder.encode(keyword, "UTF-8")
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$encodedKeyword"

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
            teamsList2.add(team)
        }
    }

    return teamsList2
}