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
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
fun Greeting(name: String) {

    Text(text = "Hello $name!")
}

@Composable
fun Content (){

    var clubName by rememberSaveable{
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = clubName,
            onValueChange = {
                clubName = it
            },
            label = { Text(text = "Enter League Name")}
            )


        Button(onClick = { /*TODO*/ }) {
            Text(text = "Retrieve Clubs")
//            GUI()
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = " Save clubs to Database")
        }
    }


}

@Composable
fun GUI(context: Context) {
    var bookInfoDisplay by remember { mutableStateOf(" ") }
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
                        bookInfoDisplay = fetchBooks(keyword)
                    }
                }) {
                    Text("Fetch Books")
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
            text = bookInfoDisplay
        )


    }
}

suspend fun fetchBooks(keyword: String): String {
//val url_string = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=25"
    val url_string = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&maxResults=25"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
// collecting all the JSON string
    var stb = StringBuilder()
// run the code of the launched coroutine in a new thread
    withContext(Dispatchers.IO) {
        var bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) { // keep reading until no more lines of text
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    val allBooks = parseJSON(stb)
    return allBooks
}







fun parseJSON(stb: StringBuilder): String {
// this contains the full JSON returned by the Web Service
    val json = JSONObject(stb.toString())
// Information about all the books extracted by this function
    var allBooks = StringBuilder()
    var jsonArray: JSONArray = json.getJSONArray("items")
// extract all the books from the JSON array
    for (i in 0..jsonArray.length() - 1) {
        val book: JSONObject = jsonArray[i] as JSONObject // this is a json object
// extract the title
        val volInfo = book["volumeInfo"] as JSONObject
        val title = volInfo["title"] as String
        allBooks.append("${i + 1}) \"$title\" ")
// extract all the authors
        try { // in case there is no author in the info
            val authors = volInfo["authors"] as JSONArray
            allBooks.append("authors: ")
            for (i in 0..authors.length() - 1)
                allBooks.append(authors[i] as String + ", ")
        } catch (jen: JSONException) {
// missing author in the information received
        }
        allBooks.append("\n\n")
    }
    return allBooks.toString()
}


fun addSeacrhedLeaguesToDB(context: Context){



    val league1 = Leagues(idLeague = "4328", strLeague = "English Premier League", strSport = "Soccer", strLeagueAlternate = "Premier League, EPL")
    val league2 = Leagues(idLeague = "4329", strLeague = "English League Championship", strSport = "Soccer", strLeagueAlternate = "Championship")
    val league3 = Leagues(idLeague = "4330", strLeague = "Scottish Premier League", strSport = "Soccer", strLeagueAlternate = "Scottish Premiership, SPFL")
    val league4 = Leagues(idLeague = "4331", strLeague = "German Bundesliga", strSport = "Soccer", strLeagueAlternate = "Bundesliga, FuÃŸball-Bundesliga")
    val league5 = Leagues(idLeague = "4332", strLeague = "Italian Serie A", strSport = "Soccer", strLeagueAlternate = "Serie A")
    val league6 = Leagues(idLeague = "4334", strLeague = "French Ligue 1", strSport = "Soccer", strLeagueAlternate = "Ligue 1 Conforama")
    val league7 = Leagues(idLeague = "4335", strLeague = "Spanish La Liga", strSport = "Soccer", strLeagueAlternate = "LaLiga Santander, La Liga")
    val league8 = Leagues(idLeague = "4336", strLeague = "Greek Superleague Greece", strSport = "Soccer", strLeagueAlternate = "")
    val league9 = Leagues(idLeague = "4337", strLeague = "Dutch Eredivisie", strSport = "Soccer", strLeagueAlternate = "Eredivisie")
    val league10 = Leagues(idLeague = "4338", strLeague = "Belgian First Division A", strSport = "Soccer", strLeagueAlternate = "Jupiler Pro League")
    val league11 = Leagues(idLeague = "4339", strLeague = "Turkish Super Lig", strSport = "Soccer", strLeagueAlternate = "Super Lig")
    val league12 = Leagues(idLeague = "4340", strLeague = "Danish Superliga", strSport = "Soccer", strLeagueAlternate = "")
    val league13 = Leagues(idLeague = "4344", strLeague = "Portuguese Primeira Liga", strSport = "Soccer", strLeagueAlternate = "Liga NOS")
    val league14 = Leagues(idLeague = "4346", strLeague = "American Major League Soccer", strSport = "Soccer", strLeagueAlternate = "MLS, Major League Soccer")
    val league15 = Leagues(idLeague = "4347", strLeague = "Swedish Allsvenskan", strSport = "Soccer", strLeagueAlternate = "Fotbollsallsvenskan")
    val league16 = Leagues(idLeague = "4350", strLeague = "Mexican Primera League", strSport = "Soccer", strLeagueAlternate = "Liga MX")
    val league17 = Leagues(idLeague = "4351", strLeague = "Brazilian Serie A", strSport = "Soccer", strLeagueAlternate = "")
    val league18 = Leagues(idLeague = "4354", strLeague = "Ukrainian Premier League", strSport = "Soccer", strLeagueAlternate = "")
    val league19 = Leagues(idLeague = "4355", strLeague = "Russian Football Premier League", strSport = "Soccer", strLeagueAlternate = "Ð§ÐµÐ¼Ð¿Ð¸Ð¾Ð½Ð°Ñ‚ Ð Ð¾ÑÑÐ¸Ð¸ Ð¿Ð¾ Ñ„ÑƒÑ‚Ð±Ð¾Ð»Ñƒ")
    val league20 = Leagues(idLeague = "4356", strLeague = "Australian A-League", strSport = "Soccer", strLeagueAlternate = "A-League")
    val league21 = Leagues(idLeague = "4358", strLeague = "Norwegian Eliteserien", strSport = "Soccer", strLeagueAlternate = "Eliteserien")
    val league22 = Leagues(idLeague = "4359", strLeague = "Chinese Super League", strSport = "Soccer", strLeagueAlternate = "")

    GlobalScope.launch (Dispatchers.IO){
        val db = Room.databaseBuilder(
            context.applicationContext,
            LeagueDB::class.java,"Leagues"
        ).build()
        val leaguesDAO = db.leaguesDAO()



        leaguesDAO.insertAll(league1)
        leaguesDAO.insertAll(league2)
        leaguesDAO.insertAll(league3)
        leaguesDAO.insertAll(league4)
        leaguesDAO.insertAll(league5)
        leaguesDAO.insertAll(league6)
        leaguesDAO.insertAll(league7)
        leaguesDAO.insertAll(league8)
        leaguesDAO.insertAll(league9)
        leaguesDAO.insertAll(league10)
        leaguesDAO.insertAll(league11)
        leaguesDAO.insertAll(league12)
        leaguesDAO.insertAll(league13)
        leaguesDAO.insertAll(league14)
        leaguesDAO.insertAll(league15)
        leaguesDAO.insertAll(league16)
        leaguesDAO.insertAll(league17)
        leaguesDAO.insertAll(league18)
        leaguesDAO.insertAll(league19)
        leaguesDAO.insertAll(league20)
        leaguesDAO.insertAll(league21)
        leaguesDAO.insertAll(league22)

    }
}