package com.example.clubfinder_footballedition


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainButtons(context1 = applicationContext,context2 = this@MainActivity)

                }
            }
        }
    }
}



@Composable
fun MainButtons(context1: Context,context2: Context){


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF77B0AA)
    )
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.coverimage5),
                contentDescription = "Cover Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Button(
                    onClick = {
                              addLeaguesToDB(context1)
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                        contentColor = Color.White)

                ) {
                    Text(text = "Add Leagues to DB")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        val navigate = Intent(context2, SearchingClubsByLeague::class.java)
                        context2.startActivity(navigate)
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), // Set a fixed height for all buttons
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                        contentColor = Color.White
                        )
                ) {
                    Text(text = "Search for Clubs by League")
                }
                Spacer(modifier = Modifier.height(4.dp)) // Add space between buttons
                Button(
                    onClick = {
                        val navigate2 = Intent(context2,SearchForClubs::class.java)
                        context2.startActivity(navigate2)
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp), // Set a fixed height for all buttons
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                        contentColor = Color.White)
                ) {
                    Text(text = "Search for Clubs")
                }
            }

        }
    }



}

fun addLeaguesToDB(context:Context){



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
            LeagueDB::class.java,"Leagues2"
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


