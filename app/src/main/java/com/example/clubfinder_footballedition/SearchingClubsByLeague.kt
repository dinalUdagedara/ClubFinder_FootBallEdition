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
//
//suspend fun fetchBooks(keyword: String): String {
////val url_string = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=25"
//    val url_string = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&maxResults=25"
//    val url = URL(url_string)
//    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
//// collecting all the JSON string
//    var stb = StringBuilder()
//// run the code of the launched coroutine in a new thread
//    withContext(Dispatchers.IO) {
//        var bf = BufferedReader(InputStreamReader(con.inputStream))
//        var line: String? = bf.readLine()
//        while (line != null) { // keep reading until no more lines of text
//            stb.append(line + "\n")
//            line = bf.readLine()
//        }
//    }
//    val allBooks = parseJSON(stb)
//    return allBooks
//}
//

data class Team(
    val idTeam: String,
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
            LeagueDB::class.java,"Leagues2"
        ).build()
        val ClubDao = db.clubDao()

//        ClubDao.insertAll(club1)
        for (team in teamsList) {
            ClubDao.insertAll(team)
        }


    }


//    val league1 = Leagues(idLeague = "4328", strLeague = "English Premier League", strSport = "Soccer", strLeagueAlternate = "Premier League, EPL")
//    val league2 = Leagues(idLeague = "4329", strLeague = "English League Championship", strSport = "Soccer", strLeagueAlternate = "Championship")
//    val league3 = Leagues(idLeague = "4330", strLeague = "Scottish Premier League", strSport = "Soccer", strLeagueAlternate = "Scottish Premiership, SPFL")
//    val league4 = Leagues(idLeague = "4331", strLeague = "German Bundesliga", strSport = "Soccer", strLeagueAlternate = "Bundesliga, FuÃŸball-Bundesliga")
//    val league5 = Leagues(idLeague = "4332", strLeague = "Italian Serie A", strSport = "Soccer", strLeagueAlternate = "Serie A")
//    val league6 = Leagues(idLeague = "4334", strLeague = "French Ligue 1", strSport = "Soccer", strLeagueAlternate = "Ligue 1 Conforama")
//    val league7 = Leagues(idLeague = "4335", strLeague = "Spanish La Liga", strSport = "Soccer", strLeagueAlternate = "LaLiga Santander, La Liga")
//    val league8 = Leagues(idLeague = "4336", strLeague = "Greek Superleague Greece", strSport = "Soccer", strLeagueAlternate = "")
//    val league9 = Leagues(idLeague = "4337", strLeague = "Dutch Eredivisie", strSport = "Soccer", strLeagueAlternate = "Eredivisie")
//    val league10 = Leagues(idLeague = "4338", strLeague = "Belgian First Division A", strSport = "Soccer", strLeagueAlternate = "Jupiler Pro League")
//    val league11 = Leagues(idLeague = "4339", strLeague = "Turkish Super Lig", strSport = "Soccer", strLeagueAlternate = "Super Lig")
//    val league12 = Leagues(idLeague = "4340", strLeague = "Danish Superliga", strSport = "Soccer", strLeagueAlternate = "")
//    val league13 = Leagues(idLeague = "4344", strLeague = "Portuguese Primeira Liga", strSport = "Soccer", strLeagueAlternate = "Liga NOS")
//    val league14 = Leagues(idLeague = "4346", strLeague = "American Major League Soccer", strSport = "Soccer", strLeagueAlternate = "MLS, Major League Soccer")
//    val league15 = Leagues(idLeague = "4347", strLeague = "Swedish Allsvenskan", strSport = "Soccer", strLeagueAlternate = "Fotbollsallsvenskan")
//    val league16 = Leagues(idLeague = "4350", strLeague = "Mexican Primera League", strSport = "Soccer", strLeagueAlternate = "Liga MX")
//    val league17 = Leagues(idLeague = "4351", strLeague = "Brazilian Serie A", strSport = "Soccer", strLeagueAlternate = "")
//    val league18 = Leagues(idLeague = "4354", strLeague = "Ukrainian Premier League", strSport = "Soccer", strLeagueAlternate = "")
//    val league19 = Leagues(idLeague = "4355", strLeague = "Russian Football Premier League", strSport = "Soccer", strLeagueAlternate = "Ð§ÐµÐ¼Ð¿Ð¸Ð¾Ð½Ð°Ñ‚ Ð Ð¾ÑÑÐ¸Ð¸ Ð¿Ð¾ Ñ„ÑƒÑ‚Ð±Ð¾Ð»Ñƒ")
//    val league20 = Leagues(idLeague = "4356", strLeague = "Australian A-League", strSport = "Soccer", strLeagueAlternate = "A-League")
//    val league21 = Leagues(idLeague = "4358", strLeague = "Norwegian Eliteserien", strSport = "Soccer", strLeagueAlternate = "Eliteserien")
//    val league22 = Leagues(idLeague = "4359", strLeague = "Chinese Super League", strSport = "Soccer", strLeagueAlternate = "")
//
//    GlobalScope.launch (Dispatchers.IO){
//        val db = Room.databaseBuilder(
//            context.applicationContext,
//            LeagueDB::class.java,"Leagues"
//        ).build()
//        val leaguesDAO = db.leaguesDAO()
//
//
//
//        leaguesDAO.insertAll(league1)
//        leaguesDAO.insertAll(league2)
//        leaguesDAO.insertAll(league3)
//        leaguesDAO.insertAll(league4)
//        leaguesDAO.insertAll(league5)
//        leaguesDAO.insertAll(league6)
//        leaguesDAO.insertAll(league7)
//        leaguesDAO.insertAll(league8)
//        leaguesDAO.insertAll(league9)
//        leaguesDAO.insertAll(league10)
//        leaguesDAO.insertAll(league11)
//        leaguesDAO.insertAll(league12)
//        leaguesDAO.insertAll(league13)
//        leaguesDAO.insertAll(league14)
//        leaguesDAO.insertAll(league15)
//        leaguesDAO.insertAll(league16)
//        leaguesDAO.insertAll(league17)
//        leaguesDAO.insertAll(league18)
//        leaguesDAO.insertAll(league19)
//        leaguesDAO.insertAll(league20)
//        leaguesDAO.insertAll(league21)
//        leaguesDAO.insertAll(league22)
//
//    }
}