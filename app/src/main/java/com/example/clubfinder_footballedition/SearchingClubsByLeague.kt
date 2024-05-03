package com.example.clubfinder_footballedition


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    var teamInfoDisplay by rememberSaveable { mutableStateOf(" ") }
    var keyword by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)) {

        TextField(
            value = keyword,
            onValueChange = { keyword = it },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF135D66),
                cursorColor = Color(0xFF135D66)
            )
            )



        Spacer(modifier = Modifier.height(25.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(horizontal = 8.dp)
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                ,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                    contentColor = Color.White),


                    onClick = {
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
            Spacer(modifier = Modifier.height(35.dp))


        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                contentColor = Color.White),

            onClick = {
            addSearchedLeaguesToDB(context)
        }) {
            Text("Save clubs to Database")
        }
        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(teamInfoDisplay.lines()) { line ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = 8.dp
                ) {
                    Text(
                        text = teamInfoDisplay,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

val teamsList = mutableListOf<ClubEntity>()
suspend fun fetchLeagues(keyword : String): List<ClubEntity> {
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
            teamsList.add(team)
        }
    }

    return teamsList
}


fun addSearchedLeaguesToDB(context: Context){


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