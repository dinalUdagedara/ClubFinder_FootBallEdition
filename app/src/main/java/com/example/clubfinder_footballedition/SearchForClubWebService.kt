package com.example.clubfinder_footballedition

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
val leagueList = mutableListOf<Leagues>()
class SearchForClubWebService : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    SearchForClubWebServiceGUI()
                }
            }
        }
    }

}



@Composable
fun SearchForClubWebServiceGUI(){

    val fetchedAllTeams = mutableMapOf<String,String>()

    var leagueInfo by remember { mutableStateOf(" ") }
    var teamsInfo by remember { mutableStateOf("") }

    var searchTerm by remember { mutableStateOf("") }
    val keyword by remember { mutableStateOf("") }

    // Creates a CoroutineScope bound to the GUI composable lifecycle
    val scope = rememberCoroutineScope()

    var allTeams: MutableMap<String, String>
    var jerseyList = mutableListOf<Triple<String, String, String>>()


Surface(
    modifier = Modifier.fillMaxSize()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        TextField(value = searchTerm, onValueChange = { searchTerm = it } )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp)
            ,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                contentColor = Color.White),


            onClick = {

            //To Display fetched TeamInfo

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


                val teamsInfoStringBuilder = StringBuilder()
                leagueList.forEach{league ->

                    allTeams = fetchAllClubs(league.strLeague,searchTerm)
                    if (allTeams != null){
                        fetchedAllTeams.putAll(allTeams)
                    }


                    allTeams.forEach { (key, value) ->
                        teamsInfoStringBuilder.append("Team Name: $value\n\n")
                        Log.d("allTeams InFUn","$allTeams")

                    }

                    val teamsInfoString = teamsInfoStringBuilder.toString()
                    teamsInfo = teamsInfoString

                    Log.d("allTeams fetched","$fetchedAllTeams")

                }

                fetchedAllTeams.forEach{ (key,value)->
                    Log.d("FetchedAllTEams", value)
                    jerseyList = lookupJerseys(key,value)
                }

                Log.d("Jersey List","$jerseyList")
                Log.d("teamNamesInList", teamsInfo)


            }

                Log.d("Status","Finished")


        }) {
            Text("Fetch Teams")
        }




        Text(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            text = teamsInfo
        )



        jerseysList.forEach { (teamName, season, jerseyURL) ->
            Log.d("list","$teamName,$season,$jerseyURL")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                JerseyImage(jerseyURL) // Display club logo

                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between logo and name
                Text(text = "Name: $teamName $season")
            }
        }

//        JerseyImage(jerseyUrl ="https://www.thesportsdb.com/images/media/team/jersey/igciz51598887447.png" )

//        Text(
//            modifier = Modifier.verticalScroll(rememberScrollState()),
//            text = leagueInfo
//        )


    }
}

}




suspend fun fetchAllLeagues(keyword : String): List<Leagues> {

//    val encodedKeyword2 = URLEncoder.encode(keyword, "UTF-8")
//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$encodedKeyword2"

    val urlString =  "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=$keyword"


//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=Eliteserien"
    val url = URL(urlString)
    val con: HttpURLConnection = withContext(Dispatchers.IO) {
        url.openConnection()
    } as HttpURLConnection


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

suspend fun fetchAllClubs(keyword : String,searchTerm:String): MutableMap<String,String> {
    Log.d("Passed Param","League Name: $keyword")
    val teamsList = mutableListOf<ClubEntity>()
    val teamMap = mutableMapOf<String,String>()

    val encodedKeyword2 = withContext(Dispatchers.IO) {
        URLEncoder.encode(keyword, "UTF-8")
    }
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$encodedKeyword2"

//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English%20Premier%20League"
//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=$keyword"


//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=Eliteserien"
    val url = URL(urlString)
    val con: HttpURLConnection = withContext(Dispatchers.IO) {
        url.openConnection()
    } as HttpURLConnection


    withContext(Dispatchers.IO) {
        try {
            val bf = BufferedReader(InputStreamReader(con.inputStream))
            val stb = StringBuilder()
            var line: String? = bf.readLine()
            while (line != null) {
                stb.append(line + "\n")
                line = bf.readLine()
            }

            val json = JSONObject(stb.toString())
            if (json.has("teams")) {
                val jsonArray = json.getJSONArray("teams")

                for (i in 0 until jsonArray.length()) {
                    val leagueObject = jsonArray.getJSONObject(i)
                    val idTeam = leagueObject.getString("idTeam")
                    val teamName1 = leagueObject.getString("strTeam")

                    if (teamName1.contains(searchTerm)) {
                        teamMap[idTeam] = teamName1
                    }
                }
            } else {
                Log.d("FetchClubsError", "No 'teams' field found in the JSON response")
            }


        } catch (e: Exception) {
            Log.e("FetchClubsError", "Error fetching clubs: ${e.message}")
        }
    }



    return teamMap
    Log.d("fetchClub","Done")
}


val jerseysList = mutableListOf<Triple<String, String, String>>()

suspend fun lookupJerseys(teamID : String, teamName: String) :MutableList<Triple<String, String, String>> {



    val url_string = "https://www.thesportsdb.com/api/v1/json/3/lookupequipment.php?id=$teamID"

//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English%20Premier%20League"
//    val url_string =  "https://www.thesportsdb.com/api/v1/json/3/searchteams.php?t=$keyword"


//    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=Eliteserien"
    val url = URL(url_string)
    val con: HttpURLConnection = withContext(Dispatchers.IO) {
        url.openConnection()
    } as HttpURLConnection



    withContext(Dispatchers.IO) {
        val bf = BufferedReader(InputStreamReader(con.inputStream))
        val stb = StringBuilder()
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }

        val json = JSONObject(stb.toString())
        val jsonArray = json.optJSONArray("equipment") ?: JSONArray()


        for (i in 0 until jsonArray.length()) {
            val jerseyObject = jsonArray.getJSONObject(i)

            val strSeason = jerseyObject.getString("strSeason")
            val strEquipment = jerseyObject.getString("strEquipment")

            val teamName = teamName

            Log.d("Jersey Info ", strEquipment)
            jerseysList.add(Triple(teamName, strSeason, strEquipment))




        }

        Log.d("in JerseyFun","$jerseysList")
    }


    return jerseysList

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun JerseyImage(jerseyUrl: String){
    Log.d("JerseyImage", jerseyUrl)

    val painter = rememberImagePainter(
        data = jerseyUrl,
        builder = {
            // You can configure image loading options here
        }
    )
    Image(
        painter = painter,
        contentDescription = null, // Provide appropriate content description
        modifier = Modifier.size(50.dp) // Adjust size as needed
    )
}