package com.example.clubfinder_footballedition

import android.content.Context
import android.os.Bundle
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
import androidx.room.Room
import coil.compose.rememberImagePainter
import com.example.clubfinder_footballedition.ui.theme.ClubFinder_FootBallEditionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchForClubs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClubFinder_FootBallEditionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GUI_Club_Search(context = applicationContext)
                }
            }
        }
    }
}

@Composable
fun GUI_Club_Search(context:Context){


    @Composable
    fun ClubLogo(logoUrl: String) {
        val painter = rememberImagePainter(
            data = logoUrl,
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

    val db = Room.databaseBuilder(
        context.applicationContext,
        LeagueDB::class.java, "Leagues3"
    ).build()

// Access the DAO object
    val ClubDao = db.clubDao()

    var searchTerm by remember { mutableStateOf("") }

    var allClubs by remember { mutableStateOf<List<ClubEntity>>(emptyList()) }

    fun showClubInfo(searchTerm:String){
        GlobalScope.launch  (Dispatchers.IO){
            val clubNames = withContext(Dispatchers.IO){
                ClubDao.searchClubs(searchTerm)
            }
            allClubs = clubNames

        }
    }









    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Column(
                modifier = Modifier
                    .fillMaxSize(),

                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(35.dp))

                TextField(value = searchTerm, onValueChange = { searchTerm = it })

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF135D66),
                        contentColor = Color.White),

                    onClick = {

                    showClubInfo(searchTerm)

                }) {
                    Text("Fetch Teams")
                }
            }




        }

        allClubs.forEach { club ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                ClubLogo(club.strTeamLogo) // Display club logo

                Spacer(modifier = Modifier.width(8.dp)) // Add spacing between logo and name
                Text(text = club.teamName,
                    modifier = Modifier
                        .fillMaxSize(0.4f)
                )
            }
        }





    }
}







